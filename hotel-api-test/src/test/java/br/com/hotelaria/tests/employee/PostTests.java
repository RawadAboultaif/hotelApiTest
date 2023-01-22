package br.com.hotelaria.tests.employee;

import br.com.hotelaria.client.EmployeeClient;
import br.com.hotelaria.data.changeless.EmployeeData;
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

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;

@Epic("Cadastro Tests")
@Feature("Employee")
@DisplayName("Cadastro Employee")
public class PostTests extends BaseTest {

    EmployeeClient employeeClient = new EmployeeClient();

    @Test
    @Story("Deve cadastrar employee com sucesso")
    public void testMustSaveEmployee() {

        EmployeeRequest novoEmployee = EmployeeFactory.employeeCompleto();

        EmployeeResponse employeeResponse = employeeClient.cadastrarEmployee(Utils.convertEmployeeToJson(novoEmployee))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().as(EmployeeResponse.class);

        assertEquals(novoEmployee.getName(), employeeResponse.getName());
        assertEquals(novoEmployee.getRole(), employeeResponse.getRole());
        assertEquals(novoEmployee.getRemuneration(), employeeResponse.getRemuneration());
        assertEquals(novoEmployee.getWorkschedule(), employeeResponse.getWorkschedule());
        assertEquals(novoEmployee.getEmail(), employeeResponse.getEmail());
        assertEquals(novoEmployee.getPhone(), employeeResponse.getPhone());
        assertEquals(novoEmployee.getSocialSecurityNumber(), employeeResponse.getSocialSecurityNumber());

        employeeClient.deletarEmployee(employeeResponse.getId());
    }

    @Test
    @Story("Deve retornar erro padrão ao tentar cadastrar employee")
    public void testMustreturnErrorWhenSavingEmployeeWithEmptyFields() {

        EmployeeRequest novoEmployeeComCamposVazios = EmployeeFactory.employeeComTodosOsCamposVazios();

        employeeClient.cadastrarEmployee(Utils.convertEmployeeToJson(novoEmployeeComCamposVazios))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString(EmployeeData.NOME_NOT_EMPTY))
                .body(containsString(EmployeeData.ROLE_NOT_EMPTY))
                .body(containsString(EmployeeData.REMUNERATION_NOT_NULL))
                .body(containsString(EmployeeData.SCHEDULE_NOT_EMPTY))
                .body(containsString(EmployeeData.EMAIL_NOT_EMPTY))
                .body(containsString(EmployeeData.PHONE_NOT_EMPTY))
                .body(containsString(EmployeeData.CPF_INVALID));
    }

    @Test
    @Story("Deve retornar erro padrão ao tentar cadastrar employee")
    public void testMustReturnErrorWhenSavingEmployeeWithNegativeRemuneration() {

        EmployeeRequest novoEmployee = EmployeeFactory.employeeCompleto();
        novoEmployee.setRemuneration(-800.00);

        employeeClient.cadastrarEmployee(Utils.convertEmployeeToJson(novoEmployee))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString(EmployeeData.REMUNERATION_ABOVE_ZERO));
    }
}
