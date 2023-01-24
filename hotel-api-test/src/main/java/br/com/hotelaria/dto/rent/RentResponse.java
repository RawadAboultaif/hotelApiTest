package br.com.hotelaria.dto.rent;

import br.com.hotelaria.dto.guest.GuestResponse;
import br.com.hotelaria.dto.unit.UnitResponse;
import lombok.Data;

@Data
public class RentResponse {

    private Integer id;
    private String checkIn;
    private String checkOut;
    private Double totalPrice;
    private GuestResponse guest;
    private UnitResponse unit;
}
