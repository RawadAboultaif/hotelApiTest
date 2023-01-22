package br.com.hotelaria.dto.unit;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class UnitRequest {

    private String name;
    private Double price;
    private Integer limitGuest;
}
