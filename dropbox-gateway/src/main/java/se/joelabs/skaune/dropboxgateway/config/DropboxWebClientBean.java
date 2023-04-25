package se.joelabs.skaune.dropboxgateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class DropboxWebClientBean {
  @Value("${dropbox.access-token:dummy-token}")
  private String accessToken;

  @Bean
  WebClient dropboxWebClient() {
    return WebClient.builder()
      .baseUrl("https://content.dropboxapi.com/2")
      .defaultHeader("Authorization", "Bearer %s".formatted(accessToken))
      .build();
  }
}
