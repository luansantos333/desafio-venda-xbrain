package com.desafiovendaxbrain.controller;

import com.desafiovendaxbrain.dto.SaleDTO;
import com.desafiovendaxbrain.repository.SaleRepository;
import com.desafiovendaxbrain.utils.SaleDTOFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.Instant;

@SpringBootTest
@AutoConfigureMockMvc
public class SaleControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        saleRepository.deleteAll();
    }

    @Test
    public void shouldCreateSaleSuccessfully() throws Exception {


        SaleDTO defaultValidDTO = SaleDTOFactory.getCustomValidDTO(null, Instant.parse("2025-10-20T00:00:00Z"), BigDecimal.valueOf(100.0),
                1L, "Pedrinho");


        mockMvc.perform(MockMvcRequestBuilders.post("/api/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(defaultValidDTO))).andExpect(MockMvcResultMatchers.status().isCreated()).
                andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sellerId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.saleDate").value("2025-10-20T00:00:00Z"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sellerName").value("Pedrinho"));

    }

    @Test
    public void shouldThrow404BadRequestWhenNameHasMoreThan100Characters() throws Exception {


        SaleDTO customInvalidDTO = SaleDTOFactory.getCustomInvalidDTO(null, Instant.parse("2025-10-20T00:00:00Z"), BigDecimal.valueOf(100.0),
                1L, RandomStringUtils.random(120, true, false));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/sales").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customInvalidDTO))).
                andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void shouldThrow404BadRequestWhenNameIsBlank() throws Exception {

        SaleDTO customInvalidDTO = SaleDTOFactory.getCustomInvalidDTO(null, Instant.parse("2025-10-20T00:00:00Z"), BigDecimal.valueOf(100.0),
                1L, "");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/sales").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customInvalidDTO))).
                andExpect(MockMvcResultMatchers.status().isBadRequest());

    }


    @Test
    public void shouldThrow404BadRequestWhenSaleDateIsAfterToday() throws Exception {

        SaleDTO customInvalidDTO = SaleDTOFactory.getCustomInvalidDTO(null, Instant.parse("2025-10-28T00:00:00Z"), BigDecimal.valueOf(100.0),
                1L, RandomStringUtils.random(120, true, false));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/sales").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(customInvalidDTO))).andExpect(MockMvcResultMatchers.status().isBadRequest());


    }

    @Test
    public void shouldThrow404BadRequestWhenSaleDateIsNull() throws Exception {


        SaleDTO customInvalidDTO = SaleDTOFactory.getCustomInvalidDTO(null, null, BigDecimal.valueOf(100.0),
                1L, "Pedrinho");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/sales").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(customInvalidDTO))).andExpect(MockMvcResultMatchers.status().isBadRequest());


    }


    @Test
    public void shouldThrow404BadRequestWhenAmountIsLowerOrEqualToZero() throws Exception {


        SaleDTO invalidDTOWithNegativeValue = SaleDTOFactory.getCustomInvalidDTO(null, Instant.parse("2025-10-26T00:00:00Z"), BigDecimal.valueOf(-1),
                1L, "Pedrinho");

        SaleDTO invalidDTOWithZeroValue = SaleDTOFactory.getCustomInvalidDTO(null, Instant.parse("2025-10-26T00:00:00Z"), BigDecimal.valueOf(0),
                1L, "Pedrinho");


        mockMvc.perform(MockMvcRequestBuilders.post("/api/sales").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(invalidDTOWithNegativeValue))).andExpect(MockMvcResultMatchers.status().isBadRequest());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/sales").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(invalidDTOWithZeroValue))).andExpect(MockMvcResultMatchers.status().isBadRequest());


    }

    @Test
    public void shouldThrow404BadRequestWhenAmountIsNull() throws Exception {


        SaleDTO invalidDTOWithNullAmount = SaleDTOFactory.getCustomInvalidDTO(null, Instant.parse("2025-10-26T00:00:00Z"), null,
                1L, "Pedrinho");


        mockMvc.perform(MockMvcRequestBuilders.post("/api/sales").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(invalidDTOWithNullAmount))).andExpect(MockMvcResultMatchers.status().isBadRequest());




    }

    @Test
    public void shouldThrow404BadRequestWhenSellerIdIsNull () throws Exception {


        SaleDTO invalidDTOWithNullSellerId = SaleDTOFactory.getCustomInvalidDTO(null, Instant.parse("2025-10-26T00:00:00Z"), BigDecimal.valueOf(100),
                null, "Pedrinho");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/sales").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(invalidDTOWithNullSellerId))).andExpect(MockMvcResultMatchers.status().isBadRequest());





    }


}

