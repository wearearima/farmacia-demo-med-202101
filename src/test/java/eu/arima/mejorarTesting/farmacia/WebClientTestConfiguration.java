package eu.arima.mejorarTesting.farmacia;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@TestConfiguration
public class WebClientTestConfiguration {

    String almacenServerUrl = "http://localhost:9091";

    @Bean
    public WebClient almacenWebClient() {
        return WebClient.builder().baseUrl(almacenServerUrl).build();
    }
}
