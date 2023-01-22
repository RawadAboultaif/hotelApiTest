package br.com.hotelaria.tests.employee;

import br.com.hotelaria.client.EmployeeClient;
import br.com.hotelaria.data.changeless.ValuesData;
import br.com.hotelaria.data.factory.EmployeeFactory;
import br.com.hotelaria.dto.employee.EmployeeRequest;
import br.com.hotelaria.dto.employee.EmployeeResponse;
import br.com.hotelaria.tests.base.BaseTest;
import br.com.hotelaria.utils.Utils;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.containsString;

@Epic("Busca Tests")
@Feature("Employee")
@DisplayName("Busca Employee")
public class GetTests extends BaseTest {

    EmployeeClient employeeClient = new EmployeeClient();

    @Test
    @Story("Deve buscar employee com sucesso")
    public void testMustGetListOfAllEmployees() {

        EmployeeRequest employeeRequest = EmployeeFactory.employeeCompleto();
        EmployeeRequest employeeRequest1 = EmployeeFactory.employeeCompleto();
        EmployeeResponse employeeResponse = employeeClient.cadastrarEmployee(Utils.convertEmployeeToJson(employeeRequest))
                .then().extract().as(EmployeeResponse.class);
        EmployeeResponse employeeResponse1 = employeeClient.cadastrarEmployee(Utils.convertEmployeeToJson(employeeRequest1))
                .then().extract().as(EmployeeResponse.class);

        EmployeeResponse[] listaEmployeeResponse = employeeClient.buscarTodosEmployee()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(EmployeeResponse[].class);

        assertNotNull(listaEmployeeResponse);
        assertTrue(listaEmployeeResponse.length >= 2);

        employeeClient.deletarEmployee(employeeResponse.getId());
        employeeClient.deletarEmployee(employeeResponse1.getId());
    }

    @Test
    @Story("Deve buscar employee com sucesso")
    public void testMustFindEmployeeByCpf() {

        EmployeeRequest novoEmployee = EmployeeFactory.employeeCompleto();
        EmployeeResponse employeeResponse = employeeClient.cadastrarEmployee(Utils.convertEmployeeToJson(novoEmployee))
                .then().extract().as(EmployeeResponse.class);

        EmployeeResponse employeeBuscado = employeeClient.buscarEmployeePorCpf(employeeResponse.getSocialSecurityNumber())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(EmployeeResponse.class);

        assertEquals(novoEmployee.getName(), employeeBuscado.getName());
        assertEquals(novoEmployee.getRole(), employeeBuscado.getRole());
        assertEquals(novoEmployee.getRemuneration(), employeeBuscado.getRemuneration());
        assertEquals(novoEmployee.getWorkschedule(), employeeBuscado.getWorkschedule());
        assertEquals(novoEmployee.getEmail(), employeeBuscado.getEmail());
        assertEquals(novoEmployee.getPhone(), employeeBuscado.getPhone());
        assertEquals(novoEmployee.getSocialSecurityNumber(), employeeBuscado.getSocialSecurityNumber());
        assertTrue(employeeBuscado.getEmployeeAddress().isEmpty());

        employeeClient.deletarEmployee(employeeResponse.getId());
    }

    @Test
    @Story("Deve retornar erro padrão ao buscar employee")
    public void testMustReturnErrorWhenSearchingEmployeeByInvalidCpf() {

        employeeClient.buscarEmployeePorCpf(Utils.faker.random().hex(11))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString(ValuesData.CPF_DONT_EXIST));
    }
}
