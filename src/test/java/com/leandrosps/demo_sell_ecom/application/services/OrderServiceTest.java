package com.leandrosps.demo_sell_ecom.application.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.leandrosps.demo_sell_ecom.application.services.OrderService.ItemInputs;
import com.leandrosps.demo_sell_ecom.db.CouponRepository;
import com.leandrosps.demo_sell_ecom.db.OrderRepository;
import com.leandrosps.demo_sell_ecom.domain.Address;
import com.leandrosps.demo_sell_ecom.domain.MyCoupon;
import com.leandrosps.demo_sell_ecom.dtos.ResonseBody;
import com.leandrosps.demo_sell_ecom.errors.NotFoundEx;
import com.leandrosps.demo_sell_ecom.geteways.AdressGeteWay;
import com.leandrosps.demo_sell_ecom.geteways.MyClock;
import com.leandrosps.demo_sell_ecom.geteways.PaymentGeteWay;
import com.leandrosps.demo_sell_ecom.query.OrderQueryService;

@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Tag("integration")
public class OrderServiceTest {
    
    @Container
    @ServiceConnection
    static final MySQLContainer<?> mysqldb;

    static {
        mysqldb = new MySQLContainer<>("mysql:latest");
        mysqldb.withDatabaseName("testdb").withUsername("testuser").withPassword("testpass")
                .withInitScripts("schema.sql", "data.sql");
        mysqldb.start();
    }

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private OrderQueryService orderQueryService;

    /* Mocks */
    @InjectMocks
    @Autowired
    private OrderService orderService;

    @Autowired
    private MyClock clock;

    @Mock
    private AdressGeteWay adressGeteWay;

    @Mock
    private PaymentGeteWay paymentGeteWay;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Mockito.when(adressGeteWay.getAdress("36300008")).thenReturn(new Address("MG", "Belo horizonte", "36300008"));
        this.orderRepository.deleteAll();
        this.couponRepository.deleteAll();
    }

    @AfterAll
    static void closeCon() {
        mysqldb.close();
    }

    @Test
    void testMySqlConn() {
        assertThat(mysqldb.isCreated()).isTrue();
        assertThat(mysqldb.isRunning()).isTrue();
    }

    private static String client_id_db = "1fef5e47-5ab0-4391-b0a0-49592e977578";

    @Test
    void shouldPlaceAnOrderAndConsult() {
        List<ItemInputs> itemsInput = new ArrayList<>();
        itemsInput.add(new ItemInputs("284791a5-5a40-4a31-a60c-d2df68997569", 3));
        var createOrderOutput = this.orderService.placeOrder("joao@exemplo.com.br", itemsInput, "36300008", null);
        assertInstanceOf(UUID.class, UUID.fromString(createOrderOutput));
        assertThat(createOrderOutput).isNotEmpty();

        var output = orderQueryService.getOrdersOfAClient(client_id_db, "WAITING_PAYMENT");

        assertAll("Gruped Assertions of Client's Order", () -> {
            assertEquals(output.size(), 1);
            var result = output.get(0);

            assertThat(result.orderTotal()).isEqualTo(630);
            assertThat(result.clientEmail()).isEqualTo("joao@exemplo.com.br");
            assertThat(result.clientId()).isEqualTo(client_id_db);
            assertThat(result.orderCreatedAt()).isInstanceOf(LocalDateTime.class);
            assertThat(result.orderId()).isEqualTo(createOrderOutput);
            assertThat(result.orderStatus()).isEqualTo("WAITING_PAYMENT");
            assertThat(result.orderStatus()).isNotEqualTo("PAID");
            assertThat(result.orderItems().size()).isEqualTo(1);
        });
    }

    @Test
    void shouldCalculateTheOrderWithStateFessAndCouponDiscount() {
        Mockito.when(adressGeteWay.getAdress("36300008")).thenReturn(new Address("SP", "Itaquera", null));
        List<ItemInputs> itemsInput = new ArrayList<>();
        itemsInput.add(new ItemInputs("284791a5-5a40-4a31-a60c-d2df68997569", 3));

        var order_id = this.orderService.placeOrder("joao@exemplo.com.br", itemsInput, "36300008", null);
        var order = this.orderRepository.getOrder(order_id);
        assertEquals(661, order.getTotal());
        assertEquals(1, order.getOrderItems().size());
        var orderItem = order.getOrderItems().get(0);
        assertNotNull(orderItem.id());
        assertEquals("284791a5-5a40-4a31-a60c-d2df68997569", orderItem.productId());
        assertEquals("WAITING_PAYMENT", order.getStatus());
        assertTrue(order.getClientEmail().equals("joao@exemplo.com.br"));
    }

    @Test
    void shouldNotBeAbleToPlaceAnOrderWithInvalidProduct() {
        Mockito.when(adressGeteWay.getAdress("36300008")).thenReturn(new Address("SP", "Itaquera", null));
        List<ItemInputs> itemsInput = new ArrayList<>();
        itemsInput.add(new ItemInputs("Invalid_product_id", 12));
        var ex = assertThrows(NotFoundEx.class,
                () -> this.orderService.placeOrder("joao@exemplo.com.br", itemsInput, "36300008", null));
        assertEquals("Product not found!", ex.getMessage());
    }

    @Test
    void shouldBeAbleToAddAnCouponToTheOrder() {
        this.clock.setCurrentDate(LocalDateTime.parse("2025-02-15T00:00:00"));
        this.couponRepository.persiste(MyCoupon.create(null, 10, 2, LocalDate.of(2025, 3, 14), clock.getCurrentDate().toLocalDate()));

        Mockito.when(paymentGeteWay.execut("62887c55-38b2-4099-9e0c-1674756ea315"))
                .thenReturn(new ResonseBody(200, "accept", "62887c55-38b2-4099-9e0c-1674756ea315"));

        List<ItemInputs> itemsInput = new ArrayList<>(
                List.of(new ItemInputs("284791a5-5a40-4a31-a60c-d2df68997569", 5)));

        var id = assertDoesNotThrow(
                () -> this.orderService.placeOrder("joao@exemplo.com.br", itemsInput, "36300008", "SAVE10"));

        var usedCouponAfter = couponRepository.findById("SAVE10").get();
        var order = this.orderQueryService.getOrderById(id);

        assertEquals("WAITING_PAYMENT", order.getStatus());
        assertEquals(1, usedCouponAfter.getUsed());
        assertThat(order.getTotal()).isEqualTo(945);
        assertEquals("SAVE10", order.getCoupon());
    }

    @Test
    void shouldBeAbleToGetAnOrder() {
        Mockito.when(paymentGeteWay.execut("62887c55-38b2-4099-9e0c-1674756ea315"))
                .thenReturn(new ResonseBody(200, "accept", "62887c55-38b2-4099-9e0c-1674756ea315"));
        List<ItemInputs> itemsInput = new ArrayList<>();
        itemsInput.add(new ItemInputs("284791a5-5a40-4a31-a60c-d2df68997569", 3));
        var order_id = this.orderService.placeOrder("joao@exemplo.com.br", itemsInput, "36300008", null);
        var order = this.orderRepository.getOrder(order_id);

        this.orderService.makePayment(order.getId(), "62887c55-38b2-4099-9e0c-1674756ea315");

        assertEquals(1, order.getOrderItems().size());
        var orderItem = order.getOrderItems().get(0);

        assertNotNull(orderItem.id());
        assertEquals("284791a5-5a40-4a31-a60c-d2df68997569", orderItem.productId());
        assertEquals("WAITING_PAYMENT", order.getStatus());
        assertEquals(630, order.getTotal());
        assertTrue(order.getClientEmail().equals("joao@exemplo.com.br"));
    }

    @Test
    void shouldProcessAnPaymentAndChangeTheStatusToPaid() {
        Mockito.when(adressGeteWay.getAdress("36300008")).thenReturn(new Address("SP", "Itaquera", null));
        Mockito.when(paymentGeteWay.execut("62887c55-38b2-4099-9e0c-1674756ea315"))
                .thenReturn(new ResonseBody(200, "accept", "62887c55-38b2-4099-9e0c-1674756ea315"));

        List<ItemInputs> itemsInput = new ArrayList<>();
        itemsInput.add(new ItemInputs("284791a5-5a40-4a31-a60c-d2df68997569", 3));

        var order_id = assertDoesNotThrow(
                () -> this.orderService.placeOrder("joao@exemplo.com.br", itemsInput, "36300008", null));
        this.orderService.makePayment(order_id, "62887c55-38b2-4099-9e0c-1674756ea315");
        var order = this.orderRepository.getOrder(order_id);
        assertEquals("PAID", order.getStatus());
    }

    @Test
    void shouldProcessAnRefusedPaymentAndChangeTheStatusToRecussed() {
        this.clock.setCurrentDate(LocalDateTime.parse("2025-02-13T00:00:00"));
        this.couponRepository.persiste(MyCoupon.create(null, 10, 2, LocalDate.of(2025, 2, 14), clock.getCurrentDate().toLocalDate()));
        Mockito.when(paymentGeteWay.execut("62887c55-38b2-4099-9e0c-1674756ea315"))
                .thenReturn(new ResonseBody(400, "recussed", "62887c55-38b2-4099-9e0c-1674756ea315"));

        List<ItemInputs> itemsInput = new ArrayList<>();
        itemsInput.add(new ItemInputs("284791a5-5a40-4a31-a60c-d2df68997569", 3));

        var order_id = assertDoesNotThrow(
                () -> this.orderService.placeOrder("joao@exemplo.com.br", itemsInput, "36300008", "SAVE10"));

        var usedCouponBefore = couponRepository.findById("SAVE10").get();
        assertEquals(1, usedCouponBefore.getUsed());

        this.orderService.makePayment(order_id, "62887c55-38b2-4099-9e0c-1674756ea315");

        var order = this.orderRepository.getOrder(order_id);
        var usedCouponAfter = couponRepository.findById("SAVE10").get();

        assertEquals(0, usedCouponAfter.getUsed()); /* Coupon Usage Returns To 0 After a Refussed payments */
        assertEquals("RECUSSED", order.getStatus());
    }
}
