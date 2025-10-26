package com.desafiovendaxbrain.utils;

import com.desafiovendaxbrain.dto.SaleDTO;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;


public record SaleDTOFactory(Long id, Instant saleDate, BigDecimal salePrice, Long sellerId, String sellerName)  {



    public static SaleDTO getCustomInvalidDTO(Long id, Instant saleDate, BigDecimal salePrice, Long sellerId, String sellerName) {

        return new SaleDTO(id, saleDate, salePrice, sellerId, sellerName);

    }

    public static SaleDTO getCustomValidDTO(Long id, Instant saleDate, BigDecimal salePrice, Long sellerId, String sellerName) {


        return new  SaleDTO(id, saleDate, salePrice, sellerId, sellerName);

    }


    public static SaleDTO getDefaultValidDTO() {


        return new  SaleDTO(1L, Instant.now(), BigDecimal.valueOf(100.0), 1L, "Pedrinho");

    }


    public static SaleDTO getDefaultInvalidDTO() {


        return new  SaleDTO(2L, Instant.now().plus(1L, ChronoUnit.DAYS), BigDecimal.valueOf(0), 2L, "Joãozinho");

    }

}
