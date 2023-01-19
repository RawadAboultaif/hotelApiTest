package br.com.hotelaria.dto.payment;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class PaymentRequest {

    private String method;
    private String card;
}
