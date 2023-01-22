package br.com.hotelaria.tests.guest;

import br.com.hotelaria.client.GuestClient;
import br.com.hotelaria.data.changeless.ValuesData;
import br.com.hotelaria.data.factory.GuestFactory;
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

@Epic("Busca Tests")
@Feature("Guest")
@DisplayName("Busca Guest")
public class GetTests extends BaseTest {

    GuestClient guestClient = new GuestClient();

    @Test
    @Story("Deve buscar guest com sucesso")
    public void testMustGetListOfAllClients() {


        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();
        GuestRequest novoGuest2Request = GuestFactory.guestCompleto();

        GuestResponse guestResponse = guestClient.cadastrarGuest(Utils.convertGuestToJson(novoGuestRequest))
                .then().extract().as(GuestResponse.class);
        GuestResponse guestResponse2 = guestClient.cadastrarGuest(Utils.convertGuestToJson(novoGuest2Request))
                .then().extract().as(GuestResponse.class);;

        GuestResponse[] listaGuestRequest = guestClient.buscarTodosGuest()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(GuestResponse[].class);

        assertNotNull(listaGuestRequest);
        assertTrue(listaGuestRequest.length >= 2);

        guestClient.deletarGuest(guestResponse.getId());
        guestClient.deletarGuest(guestResponse2.getId());

    }

    @Test
    @Story("Deve buscar guest com sucesso")
    public void testMustFindClientByCpf() {


        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();

        GuestResponse guestCadastrado = guestClient.cadastrarGuest(Utils.convertGuestToJson(novoGuestRequest))
                .then()
                .extract().as(GuestResponse.class);

        GuestResponse guestBuscado = guestClient.buscarGuestPorCpf(guestCadastrado.getSocialSecurityNumber())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(GuestResponse.class);

        assertEquals(novoGuestRequest.getName(), guestBuscado.getName());
        assertEquals(novoGuestRequest.getPhone(), guestBuscado.getPhone());
        assertEquals(novoGuestRequest.getEmail(), guestBuscado.getEmail());
        assertEquals(novoGuestRequest.getDateOfBirth(), guestBuscado.getDateOfBirth());
        assertEquals(novoGuestRequest.getSocialSecurityNumber(), guestBuscado.getSocialSecurityNumber());
        assertTrue(guestBuscado.getGuestAddress().isEmpty());
        assertTrue(guestBuscado.getGuestPayment().isEmpty());

        guestClient.deletarGuest(guestCadastrado.getId());
    }

    @Test
    @Story("Deve retornar erro padrão ao buscar guest")
    public void testMustReturnErrorWhenSearchingGuestByInvalidCpf() {

        guestClient.buscarGuestPorCpf(Utils.faker.number().digits(20))
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString(ValuesData.CPF_DONT_EXIST));
    }
}
