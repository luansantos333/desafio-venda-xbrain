package com.desafiovendaxbrain.repository;

import com.desafiovendaxbrain.model.Sale;
import com.desafiovendaxbrain.repository.projection.SellerProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query ("""
            SELECT seller.sellerName AS sellerName, COUNT(seller) AS totalSales, COUNT(seller.id)/:days  AS averageSalesByDay FROM Sale seller WHERE seller.saleDate BETWEEN :start AND :end GROUP BY seller.sellerName
            """)
    List<SellerProjection> searchBySellPeriod (Instant start, Instant end, Long days);




}
