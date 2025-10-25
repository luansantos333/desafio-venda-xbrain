package com.desafiovendaxbrain.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record SaleDTO (Long id, Instant saleDate, BigDecimal salePrice,Long sellerId, String sellerName) {
}
