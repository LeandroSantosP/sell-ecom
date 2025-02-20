package com.leandrosps.demo_sell_ecom.geteways;

import com.leandrosps.demo_sell_ecom.domain.Address;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import com.leandrosps.demo_sell_ecom.errors.InvalidAddress;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface AdressGeteWay {
    Address getAdress(String code);
}

@Primary
@Component
class FakeHttpClientGeteway implements AdressGeteWay {

    @Override
    public Address getAdress(String code) {
        return new Address("MG", "Belo horizonte", code);
    }
}

@Component
class HttpClientGeteway implements AdressGeteWay {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientGeteway.class);

    private HttpClient client = HttpClient.newHttpClient();

    @Override
    public Address getAdress(String code) {
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(new URI("https://viacep.com.br/ws/" + code + "/json/"))
                    .GET().version(HttpClient.Version.HTTP_2).build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 400) {
                logger.error("Request failed with status code: " + response.statusCode());
                throw new InvalidAddress(code);
            }

            JSONObject body = new JSONObject(response.body());

            if ((body.has("erro") && body.getString("erro").equals("true")) || response.statusCode() != 200) {
                logger.error(response.body());
                logger.error("Request failed with status code: " + response.statusCode());
                throw new InvalidAddress(code);
            }


            return new Address(body.getString("uf"), body.getString("localidade"), body.getString("cep"));
        } catch (URISyntaxException | IOException | InterruptedException e) {
            logger.error("An error occurred while getting client address", e);
            throw new RuntimeException("Failed to fetch address", e);
        } catch (InvalidAddress e) {
            throw e;
        }
    }
}
