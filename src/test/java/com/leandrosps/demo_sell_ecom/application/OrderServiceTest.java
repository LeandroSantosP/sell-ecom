package com.leandrosps.demo_sell_ecom.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.leandrosps.demo_sell_ecom.application.OrderService.ItemInputs;
import com.leandrosps.demo_sell_ecom.db.OrderRepository;
import com.leandrosps.demo_sell_ecom.errors.NotFoundEx;
import com.leandrosps.demo_sell_ecom.query.OrderQueryService;

// 15
//  
@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Tag("integration")
public class OrderServiceTest {
    @Container
    @ServiceConnection
    static final MySQLContainer mysqldb;

    static {
        mysqldb = new MySQLContainer("mysql:latest");
        mysqldb.withDatabaseName("testdb").withUsername("testuser").withPassword("testpass")
                .withInitScripts("schema.sql", "data.sql");
        mysqldb.start();
    }

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderQueryService orderQueryService;

    @Test
    void testMySqlConn() {
        assertThat(mysqldb.isCreated()).isTrue();
        assertThat(mysqldb.isRunning()).isTrue();
    }

    @AfterAll
    static void closeCon() {
        mysqldb.close();
    }

    @BeforeEach
    void cleanOrdersTable() {
        this.orderRepository.deleteAll();
    }

    private static String client_id_db = "1fef5e47-5ab0-4391-b0a0-49592e977578";

    private String PAYMENT_API_AUTH_TOKEN = "can be a jwt!";

    @Test
    void shouldPlaceAnOrderAndConsult() {
        // English project

        // Add Cupom to the order [x]
        // Add Payment Paypal fake Gateway moke apit: []
        // https://util.devi.tools/api/v2/authorize

        // client registed in the database
        List<ItemInputs> itemsInput = new ArrayList<>();
        itemsInput.add(new ItemInputs("284791a5-5a40-4a31-a60c-d2df68997569", 3));
        var createOrderOutput = this.orderService.placeOrder("joao@exemplo.com.br", itemsInput, "", "36300008", null);
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
            assertThat(result.orderStatus()).isNotEqualTo("CONFIRMED");
            assertThat(result.orderItems().size()).isEqualTo(1);
        });
    }

    @Test
    void shouldNotBeAbleToPlaceAnOrderWithInvalidProduct() {
        List<ItemInputs> itemsInput = new ArrayList<>();
        itemsInput.add(new ItemInputs("Invalid_product_id", 12));
        var ex = assertThrows(NotFoundEx.class,
                () -> this.orderService.placeOrder("joao@exemplo.com.br", itemsInput, "", "36300008", null));
        assertEquals("Product not found!", ex.getMessage());
    }

    @Test
    @Tag("currnet")
    void shouldBeAbleToAddAnCouponToTheOrder() {
        List<ItemInputs> itemsInput = new ArrayList<>();
        itemsInput.add(new ItemInputs("284791a5-5a40-4a31-a60c-d2df68997569", 5));
        var result = assertDoesNotThrow(() -> this.orderService.placeOrder("joao@exemplo.com.br", itemsInput, "", "36300008", "SAVE10"));
        
    }
}
