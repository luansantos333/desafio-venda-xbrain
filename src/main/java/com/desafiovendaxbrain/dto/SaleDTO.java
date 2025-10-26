package com.desafiovendaxbrain.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.Instant;

public record SaleDTO (Long id,
                       @NotNull(message = "You cannot leave the sale date empty!")
                       @PastOrPresent @NotNull (message = "You cannot make a sale with a future date.") Instant saleDate,
                       @Positive @NotNull (message = "The amount needs to be higher than 0") BigDecimal amount,
                       @NotNull (message = "You cannot leave the seller id field empty!") Long sellerId,
                       @NotBlank (message = "You need to enter the seller name!") @Size(max = 100, message = "You cannot enter a value higher than 100 characters!") String sellerName) {
}
