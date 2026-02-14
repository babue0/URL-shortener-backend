package com.br.urlshortener.controller;

import com.br.urlshortener.service.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
public class UrlController {

  private final UrlService urlService;

  public UrlController(UrlService urlService) {
    this.urlService = urlService;
  }

  @PostMapping("/api/shorten")
  public ResponseEntity<String> shortenUrl(@RequestBody String originalUrl) {
    String shortCode = urlService.shortenUrl(originalUrl);
    String finalUrl = "http://localhost:8080/" + shortCode;
    return ResponseEntity.ok(finalUrl);
  }

  @GetMapping("/{shortCode}")
  public ResponseEntity<Void> redirect(@PathVariable String shortCode) {

    System.out.println("🔍 O navegador pediu o código: [" + shortCode + "]");



    if ("favicon.ico".equals(shortCode) || "robots.txt".equals(shortCode)) {
      System.out.println("🚫 Bloqueando requisição automática: " + shortCode);
      return ResponseEntity.notFound().build();
    }

    String originalUrl = urlService.getOriginalUrl(shortCode);

    System.out.println("✅ Redirecionando para: " + originalUrl);

    return ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create(originalUrl))
            .build();
  }
}