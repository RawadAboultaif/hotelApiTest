package br.com.hotelaria.dto.rent;

import lombok.*;

import java.time.LocalDate;

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
