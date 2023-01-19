package br.com.hotelaria.data.factory;

import br.com.hotelaria.dto.employee.EmployeeRequest;
import br.com.hotelaria.utils.Utils;

public class EmployeeFactory {

    public static EmployeeRequest employeeCompleto() {
        return criarEmployee();
    }

    private static EmployeeRequest criarEmployee() {
        return EmployeeRequest.builder()
                .name(Utils.faker.name().fullName())
                .role(Utils.faker.job().seniority())
                .remuneration(Utils.faker.number().randomDouble(2, 800, 30000))
                .email(Utils.faker.internet().emailAddress())
                .phone(Utils.faker.phoneNumber().cellPhone())
                .socialSecurityNumber(Utils.faker.cpf().valid(false))
                .build();
    }
}
