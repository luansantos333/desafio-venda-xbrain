package com.desafiovendaxbrain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.Instant;

public record SaleDTO (Long id,
                       @NotEmpty (message = "You cannot leave the sale date empty!")
                       @PastOrPresent (message = "You cannot make a sale with a future date.") Instant saleDate,
                       @Positive (message = "The amount needs to be higher than 0") BigDecimal salePrice,
                       @NotEmpty (message = "You cannot leave the seller id field empty!") Long sellerId,
                       @NotEmpty (message = "You need to enter the seller name!") String sellerName) {
}
