package br.com.hotelaria.dto.rent;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class RentRequest {

    private String checkIn;
    private String checkOut;
}
