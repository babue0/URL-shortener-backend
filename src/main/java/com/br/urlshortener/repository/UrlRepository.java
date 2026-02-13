package com.br.urlshortener.repository;

import com.br.urlshortener.entity.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<UrlEntity, Long> {

  Optional<UrlEntity> findByShortCode(String shortCode);
}
