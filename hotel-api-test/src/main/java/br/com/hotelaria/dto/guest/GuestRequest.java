package br.com.hotelaria.dto.guest;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class GuestRequest {

    private String name;
    private String socialSecurityNumber;
    private String dateOfBirth;
    private String email;
    private String phone;
}
