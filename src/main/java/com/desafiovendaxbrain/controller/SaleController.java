package com.desafiovendaxbrain.controller;

import com.desafiovendaxbrain.dto.SaleDTO;
import com.desafiovendaxbrain.dto.SellerDTO;
import com.desafiovendaxbrain.service.SaleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping ("/api/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping
    public ResponseEntity<SaleDTO> sell(@RequestBody SaleDTO dto) {

        SaleDTO saleDTO = saleService.makeNewSell(dto);
        HttpStatus status = HttpStatus.CREATED;

        return ResponseEntity.status(status).body(saleDTO);
    }

    @GetMapping
    public ResponseEntity<List<SellerDTO>> searchSellingDataByTimePeriod(@RequestParam (required = true)Instant start, @RequestParam (required = false) Instant end) {


        List<SellerDTO> sellersByPeriod = saleService.getSellersByPeriod(start, end);
        HttpStatus status = HttpStatus.OK;

        return ResponseEntity.status(status).body(sellersByPeriod);
    }



}
