package com.desafiovendaxbrain.service;

import com.desafiovendaxbrain.dto.SaleDTO;
import com.desafiovendaxbrain.dto.SellerDTO;
import com.desafiovendaxbrain.model.Sale;
import com.desafiovendaxbrain.repository.SaleRepository;
import com.desafiovendaxbrain.repository.projection.SellerProjection;
import com.desafiovendaxbrain.utils.SaleDTOFactory;
import com.desafiovendaxbrain.utils.SaleFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class SaleServiceTest {

    @Mock
    private SaleRepository saleRepository;
    @InjectMocks
    private SaleService saleService;
    private SaleDTO saleDTO;
    private Sale sale;

    @BeforeEach
    public void setup() {

        saleDTO = SaleDTOFactory.getDefaultValidDTO();
        sale = SaleFactory.getDefaultValidSale();

    }

    @Test
    public void assertThatWhenDTOIsValidReturnWithValidIDInCreateService() {

        Mockito.when(saleRepository.save(any(Sale.class))).thenReturn(sale);
        SaleDTO savedSale = saleService.createSale(saleDTO);
        Assertions.assertThat(savedSale).isNotNull();
        Assertions.assertThat(savedSale.id()).isEqualTo(1L);
        Assertions.assertThat(savedSale.sellerName()).isEqualTo("Pedrinho");
        Assertions.assertThat(savedSale.salePrice()).isEqualTo(BigDecimal.valueOf(100.0));

        Mockito.verify(saleRepository, Mockito.times(1)).save(any(Sale.class));


    }

    @Test
    public void assertThatShouldPropagateExceptionWhenRepositoryFail() {

        Mockito.when(saleRepository.save(any(Sale.class))).thenThrow(new RuntimeException("Database connection error"));

        Assertions.assertThatThrownBy(() -> {

            saleService.createSale(saleDTO);
        }).isInstanceOf(RuntimeException.class).hasMessage("Database connection error");

        Mockito.verify(saleRepository, Mockito.times(1)).save(any(Sale.class));


    }

    @Test
    public void assertThatWhenEverythingIsValidReturnStaticsByPeriod() {

        SellerProjection seller1 = Mockito.mock(SellerProjection.class);
        SellerProjection seller2 = Mockito.mock(SellerProjection.class);

        Mockito.when(seller1.getSellerName()).thenReturn("Pedrinho");
        Mockito.when(seller1.getTotalSales()).thenReturn(2L);
        Mockito.when(seller1.getAverageSalesByDay()).thenReturn(1.0);

        Mockito.when(seller2.getSellerName()).thenReturn("Joãozinho");
        Mockito.when(seller2.getTotalSales()).thenReturn(4L);
        Mockito.when(seller2.getAverageSalesByDay()).thenReturn(2.0);


        Mockito.when(saleRepository.findSellerStatisticsByPeriod(any(Instant.class), any(Instant.class), any(Long.class))).thenReturn(Optional.of(List.of(seller1, seller2)));

        List<SellerDTO> sellerStatisticsByPeriod = saleService.getSellerStatisticsByPeriod(Instant.parse("2025-10-20T00:00:00Z"), Instant.parse("2025-10-21T00:00:00Z"));
        Assertions.assertThat(sellerStatisticsByPeriod).isNotEmpty();
        Assertions.assertThat(sellerStatisticsByPeriod).hasSize(2);
        Assertions.assertThat(sellerStatisticsByPeriod.get(0).sellerName()).isEqualTo("Pedrinho");
        Assertions.assertThat(sellerStatisticsByPeriod.get(1).sellerName()).isEqualTo("Joãozinho");

        Mockito.verify(saleRepository, Mockito.times(1)).findSellerStatisticsByPeriod(any(Instant.class), any(Instant.class), any(Long.class));

    }


    @Test
    public void assertThatWhenEndDateIsNullImplDefaultValue() {

        SellerProjection mockedProjection = Mockito.mock(SellerProjection.class);
        Mockito.when(mockedProjection.getSellerName()).thenReturn("Pedrinho");
        Mockito.when(mockedProjection.getTotalSales()).thenReturn(2L);
        Mockito.when(mockedProjection.getAverageSalesByDay()).thenReturn(1.0);


        Mockito.when(saleRepository.findSellerStatisticsByPeriod(any(Instant.class), any(Instant.class), any(Long.class))).thenReturn(Optional.of(List.of(mockedProjection)));

        Instant start = Instant.parse("2025-10-20T00:00:00Z");
        Instant end = null;

        List<SellerDTO> sellerStatisticsByPeriod = saleService.getSellerStatisticsByPeriod(start, end);

        Assertions.assertThat(sellerStatisticsByPeriod).isNotNull();
        Assertions.assertThat(sellerStatisticsByPeriod.size()).isEqualTo(1);
        Assertions.assertThat(sellerStatisticsByPeriod.get(0).sellerName()).isEqualTo("Pedrinho");
        Assertions.assertThat(sellerStatisticsByPeriod.get(0).totalSales()).isEqualTo(2L);
        Assertions.assertThat(sellerStatisticsByPeriod.get(0).averageDailySales()).isEqualTo(1.0);

        Mockito.verify(saleRepository, Mockito.times(1)).findSellerStatisticsByPeriod(any(Instant.class), any(Instant.class), any(Long.class));

    }

    @Test
    public void assertThatWhenDaysIsEqualToZeroImplDefaultValue() {

        SellerProjection mockedProjection = Mockito.mock(SellerProjection.class);
        Mockito.when(mockedProjection.getSellerName()).thenReturn("Pedrinho");
        Mockito.when(mockedProjection.getTotalSales()).thenReturn(2L);
        Mockito.when(mockedProjection.getAverageSalesByDay()).thenReturn(1.0);

        Mockito.when(saleRepository.findSellerStatisticsByPeriod(any(Instant.class), any(Instant.class), any(Long.class))).thenReturn(Optional.of(List.of(mockedProjection)));
        Instant start = Instant.parse("2025-10-20T00:00:00Z");
        Instant end = Instant.parse("2025-10-20T00:00:00Z");

        Assertions.assertThatNoException().isThrownBy(() -> saleService.getSellerStatisticsByPeriod(start, end));

    }


    @Test
    public void assertThatWhenNoSellingFoundThrowNoSuchElementException() {

        Mockito.when(saleRepository.findSellerStatisticsByPeriod(any(Instant.class), any(Instant.class), any(Long.class))).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> {


            saleService.getSellerStatisticsByPeriod(Instant.now(), null);

        }).isInstanceOf(NoSuchElementException.class);


    }

    @Test
    public void assertThatGetSellerStatisticsByPeriodThrowsArithmeticExceptionWhenDurationIsNegative() {

        Instant start = Instant.parse("2025-10-20T00:00:00Z");
        Instant end = Instant.parse("2025-10-15T00:00:00Z");
        Assertions.assertThatThrownBy(() -> {
            saleService.getSellerStatisticsByPeriod(start, end);
        }).isInstanceOf(ArithmeticException.class);

    }


}
