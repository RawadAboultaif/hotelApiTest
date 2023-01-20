package br.com.hotelaria.tests.address;

import br.com.hotelaria.client.AddressClient;
import br.com.hotelaria.client.EmployeeClient;
import br.com.hotelaria.client.GuestClient;
import br.com.hotelaria.data.factory.AddressFactory;
import br.com.hotelaria.data.factory.EmployeeFactory;
import br.com.hotelaria.data.factory.GuestFactory;
import br.com.hotelaria.dto.address.AddressRequest;
import br.com.hotelaria.dto.address.AddressResponse;
import br.com.hotelaria.dto.employee.EmployeeRequest;
import br.com.hotelaria.dto.employee.EmployeeResponse;
import br.com.hotelaria.dto.guest.GuestRequest;
import br.com.hotelaria.dto.guest.GuestResponse;
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
@Feature("Address")
@DisplayName("Atualizar Address")
public class PutTests extends BaseTest {

    GuestClient guestClient = new GuestClient();
    EmployeeClient employeeClient = new EmployeeClient();
    AddressClient addressClient = new AddressClient();

    @Test
    @Story("Deve atualizar address com sucesso")
    public void testDeveAtualizarAddressComSucesso() {

        EmployeeRequest novoEmployee = EmployeeFactory.employeeCompleto();
        AddressRequest novoAddressRequest = AddressFactory.addressCompleto();
        AddressRequest atualizadoAddressRequest = AddressFactory.addressCompleto();

        EmployeeResponse employeeResponse = employeeClient.cadastrarEmployee(Utils.convertEmployeeToJson(novoEmployee))
                .then().extract().as(EmployeeResponse.class);

        AddressResponse addressCadastrado = addressClient.cadastrarAddressIdEmployee(Utils.convertAddressToJson(novoAddressRequest), employeeResponse.getId())
                .then().extract().as(AddressResponse.class);

        AddressResponse addressAtualizado = addressClient.atualizarAddress(Utils.convertAddressToJson(atualizadoAddressRequest), addressCadastrado.getId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(AddressResponse.class);

        assertEquals(atualizadoAddressRequest.getStreetName(), addressAtualizado.getStreetName());
        assertEquals(atualizadoAddressRequest.getNumber(), addressAtualizado.getNumber());
        assertEquals(atualizadoAddressRequest.getComplement(), addressAtualizado.getComplement());
        assertEquals(atualizadoAddressRequest.getCity(), addressAtualizado.getCity());
        assertEquals(atualizadoAddressRequest.getState(), addressAtualizado.getState());
        assertEquals(atualizadoAddressRequest.getZipcode(), addressAtualizado.getZipcode());
        assertEquals(atualizadoAddressRequest.getCountry(), addressAtualizado.getCountry());

        employeeClient.deletarEmployee(employeeResponse.getId());
    }

    @Test
    @Story("Deve retornar erro padrão ao tentar atualizar address")
    public void testDeveRetornarErroAoAtualizarAddressComCamposVazios() {

        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();
        AddressRequest novoAddressRequest = AddressFactory.addressCompleto();
        AddressRequest atualizadoAddressRequest = AddressFactory.addressComCamposObrigatoriosVazios();

        GuestResponse guestResponse = guestClient.cadastrarGuest(Utils.convertGuestToJson(novoGuestRequest))
                .then().extract().as(GuestResponse.class);

        AddressResponse addressCadastrado = addressClient.cadastrarAddressIdGuest(Utils.convertAddressToJson(novoAddressRequest), guestResponse.getId())
                .then().extract().as(AddressResponse.class);

        addressClient.atualizarAddress(Utils.convertAddressToJson(atualizadoAddressRequest), addressCadastrado.getId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("O nome da rua nao pode estar vazia"))
                .body(containsString("O número do local não pode estar vazio"))
                .body(containsString("O nome da cidade não pode estar vazia"))
                .body(containsString("O número do estado não pode estar vazio"))
                .body(containsString("O código postal não pode estar vazio"))
                .body(containsString("O nome do país não pode estar vazio"));

        guestClient.deletarGuest(guestResponse.getId());
    }
}
