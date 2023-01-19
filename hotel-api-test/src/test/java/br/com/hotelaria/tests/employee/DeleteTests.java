package br.com.hotelaria.tests.employee;

import br.com.hotelaria.client.EmployeeClient;
import br.com.hotelaria.data.factory.EmployeeFactory;
import br.com.hotelaria.dto.employee.EmployeeRequest;
import br.com.hotelaria.dto.employee.EmployeeResponse;
import br.com.hotelaria.tests.base.BaseTest;
import br.com.hotelaria.utils.Utils;
import io.qameta.allure.Story;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsString;
public class DeleteTests extends BaseTest {

    EmployeeClient employeeClient = new EmployeeClient();

    @Test
    @Story("Deve retornar erro ao deletar employee")
    public void testDeveRetornarErroPadrãoAoDeletarEmployeeInexistente() {

        EmployeeRequest employeeRequest = EmployeeFactory.employeeCompleto();

        EmployeeResponse employeeResponse = employeeClient.cadastrarEmployee(Utils.convertEmployeeToJson(employeeRequest))
                .then()
                .extract().as(EmployeeResponse.class);

        employeeClient.deletarEmployee(employeeResponse.getId());
        employeeClient.deletarEmployee(employeeResponse.getId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString("O id nao existe"));
    }
}
