package br.com.hotelaria.dto.payment;

import lombok.Data;

@Data
public class PaymentResponse {

    private Integer id;
    private String method;
    private String card;
}
