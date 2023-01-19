package br.com.hotelaria.tests.employee;


import br.com.hotelaria.client.EmployeeClient;
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
    public void testDeveAtualizarEmployeeComSucesso() {

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
    public void testDeveRetornarErroAoAtualizarEmployeeComCamposVazios() {

        EmployeeRequest novoEmployee = EmployeeFactory.employeeCompleto();

        EmployeeResponse employeeCadastrado = employeeClient.cadastrarEmployee(Utils.convertEmployeeToJson(novoEmployee))
                .then().extract().as(EmployeeResponse.class);

        employeeClient.atualizarEmployee(Utils.convertEmployeeToJson(EmployeeFactory.employeeComTodosOsCamposVazios()), employeeCadastrado.getId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("O nome não pode estar vazio"))
                .body(containsString("O cargo não pode estar vazio"))
                .body(containsString("O salário não pode ser nulo"))
                .body(containsString("O horário de trabalho não pode estar vazio"))
                .body(containsString("O email não pode estar vazio"))
                .body(containsString("O telefone não pode estar vazio"))
                .body(containsString("O cpf é inválid"));

        employeeClient.deletarEmployee(employeeCadastrado.getId());
    }

    @Test
    @Story("Deve retornar erro padrão ao tentar atualizar employee")
    public void testDeveRetornarErroAoAtualizarSalarioDoEmployeeParaZero() {

        EmployeeRequest novoEmployee = EmployeeFactory.employeeCompleto();

        EmployeeResponse employeeCadastrado = employeeClient.cadastrarEmployee(Utils.convertEmployeeToJson(novoEmployee))
                .then().extract().as(EmployeeResponse.class);
        novoEmployee.setRemuneration(0d);

        employeeClient.atualizarEmployee(Utils.convertEmployeeToJson(novoEmployee), employeeCadastrado.getId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("O salário tem que ser maior que zero"));

        employeeClient.deletarEmployee(employeeCadastrado.getId());
    }
}
