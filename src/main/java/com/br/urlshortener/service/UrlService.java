package com.br.urlshortener.service;

import com.br.urlshortener.entity.UrlEntity;
import com.br.urlshortener.repository.UrlRepository;
import com.br.urlshortener.util.Base62Encoder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class UrlService {

  private final UrlRepository urlRepository;
  private final Base62Encoder base62Encoder;
  private final StringRedisTemplate redisTemplate;

  public UrlService(UrlRepository urlRepository, Base62Encoder base62Encoder, StringRedisTemplate redisTemplate) {
    this.urlRepository = urlRepository;
    this.base62Encoder = base62Encoder;
    this.redisTemplate = redisTemplate;
  }

  private String shortenUrl (String originalUrl) {
    UrlEntity urlEntity = new UrlEntity();
    urlEntity.setOriginalUrl(originalUrl);
    UrlEntity savedEntity = urlRepository.save(urlEntity);

    String shortCode = base62Encoder.encode(savedEntity.getId());

    savedEntity.setShortCode(shortCode);
    urlRepository.save(savedEntity);

    redisTemplate.opsForValue().set(shortCode, originalUrl, Duration.ofHours(24));

    return shortCode;
  }


  private String getOriginalUrl (String shortCode) {

    String cachedUrl = redisTemplate.opsForValue().get(shortCode);
    if (cachedUrl != null){
      System.out.println("Cache Hit!(Redis)");
      return cachedUrl;
    }

    System.out.println("Cache miss(Searching now in database)");
    UrlEntity urlEntity = urlRepository.findByShortCode(shortCode).orElseThrow(() -> new RuntimeException("Url not found"));

    redisTemplate.opsForValue().set(shortCode, urlEntity.getOriginalUrl(), Duration.ofHours(24));

    return urlEntity.getOriginalUrl();
  }
}
