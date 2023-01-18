package br.com.hotelaria.tests.guest;

import br.com.hotelaria.client.GuestClient;
import br.com.hotelaria.data.factory.GuestFactory;
import br.com.hotelaria.dto.guest.GuestRequest;
import br.com.hotelaria.dto.guest.GuestResponse;
import br.com.hotelaria.tests.base.BaseTest;
import br.com.hotelaria.utils.Utils;
import io.qameta.allure.Story;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GetTests extends BaseTest {

    GuestClient guestClient = new GuestClient();

    @Test
    @Story("Deve buscar guest com sucesso")
    public void testDeveBuscarListaDeGuestComSucesso() {


        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();
        GuestRequest novoGuest2Request = GuestFactory.guestCompleto();

        GuestResponse guestResponse = guestClient.cadastrar(Utils.convertGuestToJson(novoGuestRequest))
                .then().extract().as(GuestResponse.class);
        GuestResponse guestResponse2 = guestClient.cadastrar(Utils.convertGuestToJson(novoGuest2Request))
                .then().extract().as(GuestResponse.class);;

        GuestResponse[] listaGuestRequest = guestClient.buscarTodosGuest()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(GuestResponse[].class);

        assertNotNull(listaGuestRequest);
        assertTrue(listaGuestRequest.length >= 2);

        guestClient.deletar(guestResponse.getId());
        guestClient.deletar(guestResponse2.getId());

    }

    @Test
    @Story("Deve buscar guest com sucesso")
    public void testDeveBuscarGuestPorCpfComSucesso() {


        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();

        GuestResponse guestResponse = guestClient.cadastrar(Utils.convertGuestToJson(novoGuestRequest))
                .then()
                .extract().as(GuestResponse.class);

        GuestResponse guestBuscado = guestClient.buscarGuestPorCpf(guestResponse.getSocialSecurityNumber())
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
    }
}
