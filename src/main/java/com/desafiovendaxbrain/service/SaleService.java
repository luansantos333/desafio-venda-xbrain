package com.desafiovendaxbrain.service;

import com.desafiovendaxbrain.dto.SaleDTO;
import com.desafiovendaxbrain.dto.SellerDTO;
import com.desafiovendaxbrain.model.Sale;
import com.desafiovendaxbrain.repository.SaleRepository;
import com.desafiovendaxbrain.repository.projection.SellerProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class SaleService {

    private final static Logger logger = LoggerFactory.getLogger(SaleService.class);
    private final SaleRepository saleRepository;

    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }


    public SaleDTO createSale(SaleDTO dto) {

        Sale sale = new Sale();
        mapDTOToEntity(sale, dto);

        Sale persistedSaleEntity = saleRepository.save(sale);

        return new SaleDTO(persistedSaleEntity.getId(), persistedSaleEntity.getSaleDate(),
                persistedSaleEntity.getAmount(), persistedSaleEntity.getSellerId(), persistedSaleEntity.getSellerName());

    }

    public List<SellerDTO> getSellerStatisticsByPeriod(Instant startDate, Instant endDate) {

        if (endDate == null) {
            logger.info("Since you did not enter an end date, it will default to the standard value...");
            endDate = Instant.now();
            logger.info("Defaulted end date to {}", endDate);
        }

        Long days = Duration.between(startDate, endDate).toDays() + 1;

        if (days <=0) {

            throw new ArithmeticException("Days must be greater than zero");
        }

        List<SellerProjection> sales = saleRepository.findSellerStatisticsByPeriod(startDate, endDate, days).orElseThrow(() -> {

        return new NoSuchElementException ("No selling found on the given period!");

        });

        return sales.stream().
                map(x -> new SellerDTO(x.getSellerName(), x.getTotalSales(), x.getAverageSalesByDay()))
                .collect(Collectors.toList());
    }


    private void mapDTOToEntity(Sale entity, SaleDTO dto) {

        entity.setAmount(dto.salePrice());
        entity.setSellerId(dto.sellerId());
        entity.setSellerName(dto.sellerName());
        entity.setSaleDate(dto.saleDate());


    }


}
