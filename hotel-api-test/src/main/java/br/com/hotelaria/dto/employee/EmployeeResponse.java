package br.com.hotelaria.dto.employee;

import br.com.hotelaria.dto.address.AddressResponse;
import lombok.Data;

import java.util.List;

@Data
public class EmployeeResponse {
    private Integer id;
    private String name;
    private String role;
    private Double remuneration;
    private String workschedule;
    private String email;
    private String phone;
    private String socialSecurityNumber;
    private List<AddressResponse> employeeAddress;

}
