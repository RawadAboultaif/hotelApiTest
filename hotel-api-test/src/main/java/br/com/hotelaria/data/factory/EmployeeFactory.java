package br.com.hotelaria.data.factory;

import br.com.hotelaria.dto.employee.EmployeeRequest;
import br.com.hotelaria.utils.Utils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;


import java.time.LocalDate;
import java.time.LocalTime;

public class EmployeeFactory {

    public static EmployeeRequest employeeCompleto() {
        return criarEmployee();
    }

    public static EmployeeRequest employeeComTodosOsCamposVazios() {
        EmployeeRequest employeeComTodosOsCamposVazios = criarEmployee();
        employeeComTodosOsCamposVazios.setName(StringUtils.EMPTY);
        employeeComTodosOsCamposVazios.setRole(StringUtils.EMPTY);
        employeeComTodosOsCamposVazios.setRemuneration(null);
        employeeComTodosOsCamposVazios.setWorkschedule(StringUtils.EMPTY);
        employeeComTodosOsCamposVazios.setEmail(StringUtils.EMPTY);
        employeeComTodosOsCamposVazios.setPhone(StringUtils.EMPTY);
        employeeComTodosOsCamposVazios.setSocialSecurityNumber(StringUtils.EMPTY);

        return employeeComTodosOsCamposVazios;
    }

    private static EmployeeRequest criarEmployee() {
        return EmployeeRequest.builder()
                .name(Utils.faker.name().fullName())
                .role(Utils.faker.job().seniority())
                .remuneration(Utils.faker.number().randomDouble(2, 800, 30000))
                .workschedule("08:00 - 16:00")
                .email(Utils.faker.internet().emailAddress())
                .phone(Utils.faker.phoneNumber().cellPhone())
                .socialSecurityNumber(Utils.faker.cpf().valid(false))
                .build();
    }
}
