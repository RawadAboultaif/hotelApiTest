package br.com.hotelaria.dto.employee;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class EmployeeRequest {

    private String name;
    private String role;
    private Double remuneration;
    private String workschedule;
    private String email;
    private String phone;
    private String socialSecurityNumber;
}
