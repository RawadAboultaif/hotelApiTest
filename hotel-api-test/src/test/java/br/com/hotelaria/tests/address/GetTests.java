package br.com.hotelaria.tests.address;

import br.com.hotelaria.client.AddressClient;
import br.com.hotelaria.client.GuestClient;
import br.com.hotelaria.data.changeless.ValuesData;
import br.com.hotelaria.data.factory.AddressFactory;
import br.com.hotelaria.data.factory.GuestFactory;
import br.com.hotelaria.dto.address.AddressRequest;
import br.com.hotelaria.dto.address.AddressResponse;
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
import static org.junit.jupiter.api.Assertions.assertEquals;


@Epic("Busca Tests")
@Feature("Address")
@DisplayName("Busca Address")
public class GetTests extends BaseTest {

    GuestClient guestClient = new GuestClient();
    AddressClient addressClient = new AddressClient();

    @Test
    @Story("Deve buscar address com sucesso")
    public void testMustFindAddressById() {

        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();
        AddressRequest novoAddressRequest = AddressFactory.addressCompleto();

        GuestResponse guestResponse = guestClient.cadastrarGuest(Utils.convertGuestToJson(novoGuestRequest))
                .then().extract().as(GuestResponse.class);

        AddressResponse addressCadastrado = addressClient.cadastrarAddressIdGuest(Utils.convertAddressToJson(novoAddressRequest), guestResponse.getId())
                .then().extract().as(AddressResponse.class);

        AddressResponse addressBuscado = addressClient.buscarAddress(addressCadastrado.getId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(AddressResponse.class);

        assertEquals(addressCadastrado.getStreetName(), addressBuscado.getStreetName());
        assertEquals(addressCadastrado.getNumber(), addressBuscado.getNumber());
        assertEquals(addressCadastrado.getComplement(), addressBuscado.getComplement());
        assertEquals(addressCadastrado.getCity(), addressBuscado.getCity());
        assertEquals(addressCadastrado.getState(), addressBuscado.getState());
        assertEquals(addressCadastrado.getZipcode(), addressBuscado.getZipcode());
        assertEquals(addressCadastrado.getCountry(), addressBuscado.getCountry());

        guestClient.deletarGuest(guestResponse.getId());
    }

    @Test
    @Story("Deve retornar erro padrão ao buscar address")
    public void testMustReturnErrorWhenSearchingAddressByNonExistentId() {

        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();
        AddressRequest novoAddressRequest = AddressFactory.addressCompleto();

        GuestResponse guestResponse = guestClient.cadastrarGuest(Utils.convertGuestToJson(novoGuestRequest))
                .then().extract().as(GuestResponse.class);

        AddressResponse addressCadastrado = addressClient.cadastrarAddressIdGuest(Utils.convertAddressToJson(novoAddressRequest), guestResponse.getId())
                .then().extract().as(AddressResponse.class);
        addressClient.deletarAddress(addressCadastrado.getId());

        addressClient.buscarAddress(addressCadastrado.getId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString(ValuesData.ID_DONT_EXIST));

        guestClient.deletarGuest(guestResponse.getId());
    }
}
