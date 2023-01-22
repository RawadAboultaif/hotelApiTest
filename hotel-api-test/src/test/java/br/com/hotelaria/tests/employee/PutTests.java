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

@Epic("Atualizar Tests")
@Feature("Employee")
@DisplayName("Atualizar Employee")
public class PutTests extends BaseTest {

    EmployeeClient employeeClient = new EmployeeClient();

    @Test
    @Story("Deve atualizar employee com sucesso")
    public void testMustUpdateEmployee() {

        EmployeeRequest novoEmployee = EmployeeFactory.employeeCompleto();

        EmployeeResponse employeeCadastrado = employeeClient.cadastrarEmployee(Utils.convertEmployeeToJson(novoEmployee))
                .then().extract().as(EmployeeResponse.class);

        EmployeeRequest employeeComDadosNovos = EmployeeFactory.employeeCompleto();
        employeeComDadosNovos.setSocialSecurityNumber(novoEmployee.getSocialSecurityNumber());

        EmployeeResponse employeeAtualizado = employeeClient.atualizarEmployee(Utils.convertEmployeeToJson(employeeComDadosNovos), employeeCadastrado.getId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(EmployeeResponse.class);

        assertEquals(employeeComDadosNovos.getName(), employeeAtualizado.getName());
        assertEquals(employeeComDadosNovos.getPhone(), employeeAtualizado.getPhone());
        assertEquals(employeeComDadosNovos.getRole(), employeeAtualizado.getRole());
        assertEquals(employeeComDadosNovos.getEmail(), employeeAtualizado.getEmail());
        assertEquals(employeeComDadosNovos.getWorkschedule(), employeeAtualizado.getWorkschedule());
        assertEquals(employeeComDadosNovos.getRemuneration(), employeeAtualizado.getRemuneration());
        assertEquals(employeeComDadosNovos.getSocialSecurityNumber(), employeeAtualizado.getSocialSecurityNumber());

        employeeClient.deletarEmployee(employeeAtualizado.getId());
    }

    @Test
    @Story("Deve retornar erro padrão ao tentar atualizar employee")
    public void testMustReturnErrorWhenUpdatingEmployeeWithEmptyFields() {

        EmployeeRequest novoEmployee = EmployeeFactory.employeeCompleto();

        EmployeeResponse employeeCadastrado = employeeClient.cadastrarEmployee(Utils.convertEmployeeToJson(novoEmployee))
                .then().extract().as(EmployeeResponse.class);

        employeeClient.atualizarEmployee(Utils.convertEmployeeToJson(EmployeeFactory.employeeComTodosOsCamposVazios()), employeeCadastrado.getId())
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

        employeeClient.deletarEmployee(employeeCadastrado.getId());
    }

    @Test
    @Story("Deve retornar erro padrão ao tentar atualizar employee")
    public void testMustReturnErrorWhenUpdatingRemunerationToZero() {

        EmployeeRequest novoEmployee = EmployeeFactory.employeeCompleto();

        EmployeeResponse employeeCadastrado = employeeClient.cadastrarEmployee(Utils.convertEmployeeToJson(novoEmployee))
                .then().extract().as(EmployeeResponse.class);
        novoEmployee.setRemuneration(0d);

        employeeClient.atualizarEmployee(Utils.convertEmployeeToJson(novoEmployee), employeeCadastrado.getId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString(EmployeeData.REMUNERATION_ABOVE_ZERO));

        employeeClient.deletarEmployee(employeeCadastrado.getId());
    }
}
