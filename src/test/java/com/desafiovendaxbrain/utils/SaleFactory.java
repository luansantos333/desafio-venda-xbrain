package com.desafiovendaxbrain.utils;

import com.desafiovendaxbrain.model.Sale;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public record SaleFactory (Long id, Instant saleDate, BigDecimal amount, Long sellerId, String sellerName) {

    public static Sale getDefaultValidSale () {

        Sale s = new Sale();
        s.setId(1L);
        s.setSellerId(1L);
        s.setAmount(BigDecimal.valueOf(100.0));
        s.setSaleDate(Instant.now());
        s.setSellerName("Pedrinho");
        return s;

    }

    public static Sale getDefaultInvalidSale () {

        Sale s = new Sale();
        s.setId(2L);
        s.setSellerId(2L);
        s.setAmount(BigDecimal.valueOf(0));
        s.setSaleDate(Instant.now().plus(1L, ChronoUnit.DAYS));
        s.setSellerName("Joãozinho");
        return s;

    }



}
