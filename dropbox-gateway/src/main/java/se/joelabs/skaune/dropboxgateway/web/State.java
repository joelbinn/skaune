package se.joelabs.skaune.dropboxgateway.web;

import lombok.With;

import java.time.LocalDateTime;

@With
public record State(LocalDateTime timestamp, String status, String data) {
}
