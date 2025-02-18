package com.leandrosps.demo_sell_ecom.geteways;

import java.lang.System.Logger;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.leandrosps.demo_sell_ecom.dtos.ResonseBody;
import com.leandrosps.demo_sell_ecom.errors.GetewayServerError;

import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpClient;

public interface PaymentGeteWay {
	public ResonseBody execut(String token);
}

@Slf4j
@Component
class MyHttpClinet implements PaymentGeteWay {

	private HttpClient client = HttpClient.newHttpClient();

	private String base_url = "http://localhost:8091";

	@Override
	public ResonseBody execut(String token) {
		try {
			HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers
					.ofString(new JSONObject().put("token", token).toString());

			HttpRequest request = HttpRequest.newBuilder().uri(new URI(base_url + "/process_payment"))
					.header("Content-Type", "application/json").POST(bodyPublisher).version(HttpClient.Version.HTTP_2)
					.build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() == 400) {
				log.error("Request failed with status code: " + response.statusCode());
			}

			JSONObject body = new JSONObject(response.body());
			return new ResonseBody(body.getInt("status_code"), body.getString("status"), body.getString("content"));
		} catch (Exception e) {
			throw new GetewayServerError("PaymentGeteWay", e.getMessage());
		}
	}
}