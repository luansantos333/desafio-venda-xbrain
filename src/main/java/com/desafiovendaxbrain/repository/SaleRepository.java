package com.desafiovendaxbrain.repository;

import com.desafiovendaxbrain.model.Sale;
import com.desafiovendaxbrain.repository.projection.SellerProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query ("""
            SELECT s.sellerName AS sellerName, COUNT(s) AS totalSales, COUNT(s.id)/:days  AS averageSalesByDay FROM Sale s WHERE s.saleDate BETWEEN :start AND :end GROUP BY s.sellerName
            """)
    Optional<List<SellerProjection>> findSellerStatisticsByPeriod(Instant startDate, Instant endDate, Long days);




}
