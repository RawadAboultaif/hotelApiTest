package br.com.hotelaria.dto.unit;

import lombok.Data;

@Data
public class UnitResponse {

    private Integer id;
    private String name;
    private Double price;
    private Integer limitGuest;
    private String status;
}
