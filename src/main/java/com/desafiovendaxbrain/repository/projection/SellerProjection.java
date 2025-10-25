package com.desafiovendaxbrain.repository.projection;

public interface SellerProjection {

    String getSellerName();
    Long getTotalSales();
    Double getAverageSalesByDay();

}
