package com.desafiovendaxbrain.repository;

import com.desafiovendaxbrain.model.Sale;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

@DataJpaTest
@ActiveProfiles("test")
public class SaleRepositoryIT {


    @Autowired
    private SaleRepository saleRepository;


    @BeforeEach
    public void setup() {


        saleRepository.deleteAll();


    }


    @Test
    public void shouldFindSaleStaticsForAGivenPeriod() {
        Instant start = Instant.parse("2025-10-18T00:00:00.00Z");
        Instant end = Instant.parse("2025-10-21T00:00:00.00Z");
        Duration duration = Duration.between(start, end);


        saleRepository.save(createSale(Instant.parse("2025-10-18T00:00:00.00Z"), BigDecimal.valueOf(100.0), 1L, "Pedrinho"));
        saleRepository.save(createSale(Instant.parse("2025-10-19T00:00:00.00Z"), BigDecimal.valueOf(100.0), 1L, "Pedrinho"));
        saleRepository.save(createSale(Instant.parse("2025-10-20T00:00:00.00Z"), BigDecimal.valueOf(100.0), 1L, "Pedrinho"));
        saleRepository.save(createSale(Instant.parse("2025-10-18T00:00:00.00Z"), BigDecimal.valueOf(200.0), 2L, "Joãozinho"));
        saleRepository.save(createSale(Instant.parse("2025-10-19T00:00:00.00Z"), BigDecimal.valueOf(200.0), 2L, "Joãozinho"));


        Assertions.assertThat(saleRepository.findSellerStatisticsByPeriod(start, end, duration.toDays()).get()).isNotEmpty();
        Assertions.assertThat(saleRepository.findSellerStatisticsByPeriod(start, end, duration.toDays()).get()).hasSize(2);
        Assertions.assertThat(saleRepository.findSellerStatisticsByPeriod(start, end, duration.toDays()).get().get(0).getSellerName()).isEqualTo("Joãozinho");
        Assertions.assertThat(saleRepository.findSellerStatisticsByPeriod(start, end, duration.toDays()).get().get(0).getTotalSales()).isEqualTo(2);


        Assertions.assertThat(saleRepository.findSellerStatisticsByPeriod(start, end, duration.toDays()).get().get(1).getSellerName()).isEqualTo("Pedrinho");
        Assertions.assertThat(saleRepository.findSellerStatisticsByPeriod(start, end, duration.toDays()).get().get(1).getTotalSales()).isEqualTo(3);


    }


    @Test
    public void assertThatShouldReturnEmptyListWhenNoSaleFoundInAGivenPeriod() {
        Instant start = Instant.parse("2025-10-20T00:00:00.00Z");
        Instant end = Instant.parse("2025-10-21T00:00:00.00Z");
        Duration duration = Duration.between(start, end);

        saleRepository.save(createSale(Instant.parse("2025-10-22T00:00:00.00Z"), BigDecimal.valueOf(100.0), 1L, "Pedrinho"));
        saleRepository.save(createSale(Instant.parse("2025-10-23T00:00:00.00Z"), BigDecimal.valueOf(100.0), 1L, "Pedrinho"));
        saleRepository.save(createSale(Instant.parse("2025-10-24T00:00:00.00Z"), BigDecimal.valueOf(100.0), 1L, "Pedrinho"));

        Assertions.assertThat(saleRepository.findSellerStatisticsByPeriod(start, end, duration.toDays()).get().isEmpty()).isTrue();


    }


    @Test
    public void assertThatShouldFilterSalesByPeriodCorrectly () {

        Instant start = Instant.parse("2025-10-20T00:00:00.00Z");
        Instant end = Instant.parse("2025-10-25T00:00:00.00Z");
        Duration duration = Duration.between(start, end);

        saleRepository.save(createSale(Instant.parse("2025-10-21T00:00:00.00Z"), BigDecimal.valueOf(100.0), 1L, "Pedrinho"));
        saleRepository.save(createSale(Instant.parse("2025-10-22T00:00:00.00Z"), BigDecimal.valueOf(100.0), 1L, "Pedrinho"));
        saleRepository.save(createSale(Instant.parse("2025-10-26T00:00:00.00Z"), BigDecimal.valueOf(100.0), 1L, "Pedrinho"));
        saleRepository.save(createSale(Instant.parse("2025-10-27T00:00:00.00Z"), BigDecimal.valueOf(100.0), 1L, "Pedrinho"));

        Assertions.assertThat(saleRepository.findSellerStatisticsByPeriod(start, end, duration.toDays()).get().get(0).getTotalSales()).isEqualTo(2);



    }


    private Sale createSale(Instant saleDate, BigDecimal amount, Long sellerId, String sellerName) {

        Sale sale = new Sale();
        sale.setSaleDate(saleDate);
        sale.setAmount(amount);
        sale.setSellerId(sellerId);
        sale.setSellerName(sellerName);
        return sale;
    }


}
