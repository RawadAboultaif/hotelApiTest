package br.com.hotelaria.dto.payment;

import lombok.Data;

@Data
public class PaymentResponse {

    private Long id;
    private String method;
    private String card;
}
