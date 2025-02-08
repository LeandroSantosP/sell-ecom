package com.leandrosps.demo_sell_ecom.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import com.leandrosps.demo_sell_ecom.application.OrderService.ItemInputs;
import com.leandrosps.demo_sell_ecom.db.OrderDbModel;
import com.leandrosps.demo_sell_ecom.db.OrderRepository;

@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderServiceTest {

  @Container
  @ServiceConnection
  static final MySQLContainer mysqldb;

  static {
    mysqldb = new MySQLContainer("mysql:latest");
    mysqldb.withDatabaseName("testdb")
        .withUsername("testuser")
        .withPassword("testpass")
        .withInitScripts("schema.sql", "data.sql");
    mysqldb.start();
  }

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private OrderService orderService;

  @Test
  void testMySqlConn() {
    assertThat(mysqldb.isCreated()).isTrue();
    assertThat(mysqldb.isRunning()).isTrue();
  }

  @BeforeEach
  void setUp() {
  }

  @AfterAll
  static void closeCon(){
    mysqldb.close();
  }

  @Test
  void testPlaceOrder() {
    List<ItemInputs> itemsInput = new ArrayList<>();
    itemsInput.add(new ItemInputs("284791a5-5a40-4a31-a60c-d2df68997569", 3));
    var createOrderOutput = this.orderService.placeOrder("joao@exemplo.com.br", itemsInput, "");
    assertInstanceOf(UUID.class, UUID.fromString(createOrderOutput));
    assertThat(createOrderOutput).isNotEmpty();

 /*    var output = this.orderService.getOrder(createOrderOutput);
    assertEquals("joao@exemplo.com.br", output.clientEmail());
    assertEquals(3009, output.total());
    assertEquals("wating_payment", output.status()); */
  }
}
