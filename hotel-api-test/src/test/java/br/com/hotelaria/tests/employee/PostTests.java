package br.com.hotelaria.tests.employee;

import br.com.hotelaria.client.EmployeeClient;
import br.com.hotelaria.data.factory.EmployeeFactory;
import br.com.hotelaria.dto.employee.EmployeeRequest;
import br.com.hotelaria.dto.employee.EmployeeResponse;
import br.com.hotelaria.tests.base.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Epic("Cadastrar Tests")
@Feature("Employee")
@DisplayName("Cadastrar Employee")
public class PostTests extends BaseTest {

    EmployeeClient employeeClient = new EmployeeClient();

    @Test
    @Story("Deve cadastrar employee com sucesso")
    public void testDeveCadastrarEmployeeComSucesso() {

        EmployeeRequest novoEmployee = EmployeeFactory.employeeCompleto();

        EmployeeResponse employeeResponse = employeeClient.cadastrarEmployee(Utils.)
    }
}
