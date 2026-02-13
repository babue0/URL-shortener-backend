package com.br.urlshortener.util;

import org.springframework.stereotype.Component;

@Component
public class Base62Encoder {

  private static final String ALLOWED_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final int BASE = ALLOWED_CHARACTERS.length();

  public String encode(long input){
    StringBuilder encodedString = new StringBuilder();

    if (input == 0){
      return String.valueOf(ALLOWED_CHARACTERS.charAt(0));
    }

    while (input > 0){
      int remainder = (int) (input % BASE);
      encodedString.append(ALLOWED_CHARACTERS.charAt(remainder));
      input = input /BASE;
    }

    return encodedString.reverse().toString();
  }

  public long decode(String input){
    char[] characters = input.toCharArray();
    long lenght = characters.length;
    long decoded = 0;

    for (int i = 0; i < lenght; i++) {
      decoded += (long) (ALLOWED_CHARACTERS.indexOf(characters[i]) * Math.pow(BASE, lenght -1 -i));
    }

    return decoded;

  }
}
