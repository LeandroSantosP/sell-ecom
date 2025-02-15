package com.leandrosps.demo_sell_ecom.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
class ProcessePaymentBody {
	String token;
}

/* The Ideia is simulate an patment Getway which or application uses */
@Slf4j
@Component
public class PaymentGetewayServer {

	static record ReponseBodyPaymentProcess(int status_code, String token) {
	}

	public static void main(String[] args) throws IOException {
		HttpServer server = HttpServer.create(new InetSocketAddress(8091), 0);
		server.start();
		System.out.println("Server is Running on port -> " + 8091);
		server.createContext("/", (exchange) -> {
			String requestBody = null;
			try (InputStream inputStream = exchange.getRequestBody();
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
				requestBody = reader.lines().collect(Collectors.joining("\n"));
				inputStream.close();
				reader.close();
			} catch (Exception e) {
				log.error("Error on reader the request body!", e);
			}
			log.info("OK2");

			if (requestBody == null) {
				throw new RuntimeException("Invalid requestbody!");
			}

			var mapper = new ObjectMapper();
			var body = mapper.readValue(requestBody, ProcessePaymentBody.class);

			log.info("OK2" + requestBody);

			switch (exchange.getRequestMethod()) {
			case "POST":
				try (OutputStream os = exchange.getResponseBody()) {
					log.info("OK3" + requestBody);

					String path = exchange.getRequestURI().getPath();
					if (path.equals("/process_payment")) {
						var response = mapper.writeValueAsBytes(new ReponseBodyPaymentProcess(200, body.token));
						exchange.sendResponseHeaders(200, response.length);
						os.write(response);
						os.close();
					}
				} catch (Exception e) {
					log.error("Error on writer the resonse body!", e);
				}
				break;
			default:
				break;
			}
		});
	}

}
