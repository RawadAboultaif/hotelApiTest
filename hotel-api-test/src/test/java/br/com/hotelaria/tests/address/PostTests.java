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

@Epic("Cadastro Tests")
@Feature("Address")
@DisplayName("Cadastro Address")
public class PostTests extends BaseTest {

    GuestClient guestClient = new GuestClient();

    EmployeeClient employeeClient = new EmployeeClient();
    AddressClient addressClient = new AddressClient();

    @Test
    @Story("Deve cadastrar address com sucesso")
    public void testDeveCadastrarAddressEVincularAoClienteComSucesso() {

        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();
        AddressRequest novoAddressRequest = AddressFactory.addressCompleto();

        GuestResponse guestResponse = guestClient.cadastrarGuest(Utils.convertGuestToJson(novoGuestRequest))
                .then().extract().as(GuestResponse.class);

        AddressResponse addressResponse = addressClient.cadastrarAddressIdGuest(Utils.convertAddressToJson(novoAddressRequest), guestResponse.getId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().as(AddressResponse.class);

        GuestResponse guestFullResponse = guestClient.buscarGuestPorCpf(guestResponse.getSocialSecurityNumber())
                .then().extract().as(GuestResponse.class);

        assertEquals(guestFullResponse.getGuestAddress().get(0).getStreetName(), addressResponse.getStreetName());
        assertEquals(guestFullResponse.getGuestAddress().get(0).getNumber(), addressResponse.getNumber());
        assertEquals(guestFullResponse.getGuestAddress().get(0).getComplement(), addressResponse.getComplement());
        assertEquals(guestFullResponse.getGuestAddress().get(0).getCity(), addressResponse.getCity());
        assertEquals(guestFullResponse.getGuestAddress().get(0).getState(), addressResponse.getState());
        assertEquals(guestFullResponse.getGuestAddress().get(0).getZipcode(), addressResponse.getZipcode());
        assertEquals(guestFullResponse.getGuestAddress().get(0).getCountry(), addressResponse.getCountry());

        guestClient.deletarGuest(guestResponse.getId());
    }

    @Test
    @Story("Deve cadastrar address com sucesso")
    public void testDeveCadastrarAddressEVincularAoEmployeeComSucesso() {


        EmployeeRequest novoEmployee = EmployeeFactory.employeeCompleto();
        AddressRequest novoAddressRequest = AddressFactory.addressCompleto();

        EmployeeResponse employeeResponse = employeeClient.cadastrarEmployee(Utils.convertEmployeeToJson(novoEmployee))
                .then().extract().as(EmployeeResponse.class);

        AddressResponse addressResponse = addressClient.cadastrarAddressIdEmployee(Utils.convertAddressToJson(novoAddressRequest), employeeResponse.getId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().as(AddressResponse.class);

        EmployeeResponse employeeFullResponse = employeeClient.buscarEmployeePorCpf(employeeResponse.getSocialSecurityNumber())
                .then().extract().as(EmployeeResponse.class);

        assertEquals(employeeFullResponse.getEmployeeAddress().get(0).getStreetName(), addressResponse.getStreetName());
        assertEquals(employeeFullResponse.getEmployeeAddress().get(0).getNumber(), addressResponse.getNumber());
        assertEquals(employeeFullResponse.getEmployeeAddress().get(0).getComplement(), addressResponse.getComplement());
        assertEquals(employeeFullResponse.getEmployeeAddress().get(0).getCity(), addressResponse.getCity());
        assertEquals(employeeFullResponse.getEmployeeAddress().get(0).getState(), addressResponse.getState());
        assertEquals(employeeFullResponse.getEmployeeAddress().get(0).getZipcode(), addressResponse.getZipcode());
        assertEquals(employeeFullResponse.getEmployeeAddress().get(0).getCountry(), addressResponse.getCountry());


        employeeClient.deletarEmployee(employeeResponse.getId());
    }

    @Test
    @Story("Deve cadastrar address com sucesso")
    public void testDeveCadastrarAddressComCampoComplementVazio() {

        EmployeeRequest novoEmployee = EmployeeFactory.employeeCompleto();
        AddressRequest novoAddressRequest = AddressFactory.addressCampoComplementVazio();

        EmployeeResponse employeeResponse = employeeClient.cadastrarEmployee(Utils.convertEmployeeToJson(novoEmployee))
                .then().extract().as(EmployeeResponse.class);
        AddressResponse addressResponse = addressClient.cadastrarAddressIdEmployee(Utils.convertAddressToJson(novoAddressRequest), employeeResponse.getId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().as(AddressResponse.class);

        EmployeeResponse employeeFullResponse = employeeClient.buscarEmployeePorCpf(employeeResponse.getSocialSecurityNumber())
                .then().extract().as(EmployeeResponse.class);

        assertEquals(employeeFullResponse.getEmployeeAddress().get(0).getStreetName(), addressResponse.getStreetName());
        assertEquals(employeeFullResponse.getEmployeeAddress().get(0).getNumber(), addressResponse.getNumber());
        assertEquals(employeeFullResponse.getEmployeeAddress().get(0).getComplement(), addressResponse.getComplement());
        assertEquals(employeeFullResponse.getEmployeeAddress().get(0).getCity(), addressResponse.getCity());
        assertEquals(employeeFullResponse.getEmployeeAddress().get(0).getState(), addressResponse.getState());
        assertEquals(employeeFullResponse.getEmployeeAddress().get(0).getZipcode(), addressResponse.getZipcode());
        assertEquals(employeeFullResponse.getEmployeeAddress().get(0).getCountry(), addressResponse.getCountry());

        employeeClient.deletarEmployee(employeeResponse.getId());

    }

    @Test
    @Story("Deve retornar erro padrão ao tentar cadastrar address")
    public void testDeveRetornarErroAoVincularAddressAoIdGuestInexistente() {

        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();
        AddressRequest novoAddressRequest = AddressFactory.addressCompleto();

        GuestResponse guestResponse = guestClient.cadastrarGuest(Utils.convertGuestToJson(novoGuestRequest))
                .then().extract().as(GuestResponse.class);
        guestClient.deletarGuest(guestResponse.getId());

        addressClient.cadastrarAddressIdGuest(Utils.convertAddressToJson(novoAddressRequest), guestResponse.getId())
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString("O id nao existe"));

        guestClient.deletarGuest(guestResponse.getId());
    }

    @Test
    @Story("Deve retornar erro padrão ao tentar cadastrar address")
    public void testDeveRetornarErroAoVincularAddressAoIdEmployeeInexistente() {

        EmployeeRequest novoEmployeeRequest = EmployeeFactory.employeeCompleto();
        AddressRequest novoAddressRequest = AddressFactory.addressCompleto();

        EmployeeResponse employeeResponse = employeeClient.cadastrarEmployee(Utils.convertEmployeeToJson(novoEmployeeRequest))
                .then().extract().as(EmployeeResponse.class);
        employeeClient.deletarEmployee(employeeResponse.getId());

        addressClient.cadastrarAddressIdGuest(Utils.convertAddressToJson(novoAddressRequest), employeeResponse.getId())
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString("O id nao existe"));

        employeeClient.deletarEmployee(employeeResponse.getId());
    }
}
