package br.com.hotelaria.dto.guest;


import br.com.hotelaria.dto.address.AddressResponse;
import br.com.hotelaria.dto.payment.PaymentResponse;
import lombok.Data;

import java.util.List;


@Data
public class GuestResponse {

    private Integer id;
    private String name;
    private String socialSecurityNumber;
    private String dateOfBirth;
    private String email;
    private String phone;
    private List<AddressResponse> guestAddress;
    private List<PaymentResponse> guestPayment;
}
