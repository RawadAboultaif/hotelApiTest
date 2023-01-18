package br.com.hotelaria.dto.address;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class AddressRequest {

    private String streetName;
    private String number;
    private String complement;
    private String city;
    private String state;
    private String zipcode;
    private String country;
}
