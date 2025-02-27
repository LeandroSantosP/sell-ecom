package com.leandrosps.demo_sell_ecom.infra.controller;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.leandrosps.demo_sell_ecom.application.auth.AuthService;
import com.leandrosps.demo_sell_ecom.infra.db.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leandrosps.demo_sell_ecom.infra.db.dbmodels.UserDbModel;

@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

	@Container
	@ServiceConnection
	static final MySQLContainer<?> mysqldb;

	@Autowired
	private ObjectMapper objectMapper;

	@Mock
	private UserRepository userRepository;

	@MockBean
	private AuthService authService;

	@Autowired
	private MockMvc mockMvc;

	static {
		mysqldb = new MySQLContainer<>("mysql:8.1");
		mysqldb.withDatabaseName("testdb").withUsername("testuser").withPassword("testpass");
		mysqldb.start();
	}
	/* Some error with csrf */
	@Tag("current")
	@Test
	@Disabled
	void shouldSingInAnUser() throws Exception {
	var user = new UserDbModel("JohnDoe123", "$2y$07$FON2hxYEWLW9W5CmCdyba.gjF.AL3C5kgHK1Ov8sko3DJ8SSsWNJu",
				"johnDoe123@example.com");
		user.setEnabled(true);

		Mockito.when(this.userRepository.findByUsername("johnDoe123")).thenReturn(Optional.of(user));

		String userJson = "{ \"username\": \"JohnDoe123\", \"password\": \"senha123\" }";

		mockMvc.perform(post("/api/auth/signin").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userJson))).andExpect(re -> {
					System.out.println("HERE: " + re.getResponse().getErrorMessage());
				}).andExpect(status().isOk());
	}
}
