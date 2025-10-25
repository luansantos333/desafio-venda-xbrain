package com.desafiovendaxbrain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter

@Entity
@Table (name = "tb_venda")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant saleDate;
    @Column(precision = 10, scale = 2)
    private BigDecimal amount;
    private Long sellerId;
    @Column(length = 100)
    private String sellerName;

    public Sale(Long id, Instant saleDate, BigDecimal amount, Long sellerId, String sellerName) {
        this.id = id;
        this.saleDate = saleDate;
        this.amount = amount;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
    }

    public Sale() {
    }
}
