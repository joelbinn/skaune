package se.joelabs.skaune.dropboxgateway.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerErrorException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.function.Function;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/dropbox")
public class DropBoxController {
  public static final String DROPBOX_API_ARG_HEADER = "Dropbox-API-Arg";
  private final WebClient dropboxWebClient;
  private final ObjectMapper objectMapper;

  @GetMapping(path = "current-state", produces = MediaType.APPLICATION_JSON_VALUE)
  Mono<State> getCurrentState() {
    return dropboxWebClient.post()
      .uri("/files/download")
      .header(DROPBOX_API_ARG_HEADER, "{\"path\":\"/skaune/current-state.json\"}")
      .accept(MediaType.APPLICATION_OCTET_STREAM)
      .retrieve()
      .bodyToMono(byte[].class)
      .map(toState());
  }

  @PutMapping("current-state")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  Mono<Void> getCurrentState(@RequestBody State state) {
    return dropboxWebClient.post()
      .uri("/files/upload")
      .header(DROPBOX_API_ARG_HEADER, "{\"autorename\":false,\"mode\":\"overwrite\",\"mute\":true,\"path\":\"/skaune/current-state.json\",\"strict_conflict\":false}")
      .contentType(MediaType.APPLICATION_OCTET_STREAM)
      .bodyValue(toByteArray(state.withTimestamp(LocalDateTime.now())))
      .retrieve()
      .bodyToMono(Void.class);
  }

  @GetMapping(path = "desired-state", produces = MediaType.APPLICATION_JSON_VALUE)
  Mono<State> getDesiredState() {
    return dropboxWebClient.post()
      .uri("/files/download")
      .header(DROPBOX_API_ARG_HEADER, "{\"path\":\"/skaune/desired-state.json\"}")
      .accept(MediaType.APPLICATION_OCTET_STREAM)
      .retrieve()
      .bodyToMono(byte[].class)
      .map(toState());
  }

  @PutMapping("desired-state")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  Mono<Void> getDesiredState(@RequestBody State state) {
    return dropboxWebClient.post()
      .uri("/files/upload")
      .header(DROPBOX_API_ARG_HEADER, "{\"autorename\":false,\"mode\":\"overwrite\",\"mute\":true,\"path\":\"/skaune/desired-state.json\",\"strict_conflict\":false}")
      .contentType(MediaType.APPLICATION_OCTET_STREAM)
      .bodyValue(toByteArray(state.withTimestamp(LocalDateTime.now())))
      .retrieve()
      .bodyToMono(Void.class);
  }


  private byte[] toByteArray(State state) {
    try {
      return objectMapper.writeValueAsBytes(state);
    } catch (JsonProcessingException e) {
      throw new ServerErrorException("Failed to create byte[] from Status object", e);
    }
  }

  private Function<byte[], State> toState() {
    return bytes -> {
      try {
        return objectMapper.readValue(bytes, State.class);
      } catch (IOException e) {
        throw new ServerErrorException("Failed to create Status object from byte[]", e);
      }
    };
  }
}
