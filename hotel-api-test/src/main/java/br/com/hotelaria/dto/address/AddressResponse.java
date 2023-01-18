package br.com.hotelaria.dto.address;

import lombok.Data;

@Data
public class AddressResponse {

    private Long id;
    private String streetName;
    private String number;
    private String complement;
    private String city;
    private String state;
    private String zipcode;
    private String country;
}
