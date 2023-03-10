package br.com.hotelaria.tests.guest;

import br.com.hotelaria.client.GuestClient;
import br.com.hotelaria.data.changeless.GuestData;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("Atualizar Tests")
@Feature("Guest")
@DisplayName("Atualizar Guest")
public class PutTests extends BaseTest {

    GuestClient guestClient = new GuestClient();

    @Test
    @Story("Deve atualizar guest com sucesso")
    public void testMustUpdateClient() {

        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();
        GuestResponse guestCadastrado = guestClient.cadastrarGuest(Utils.convertGuestToJson(novoGuestRequest))
                .then().extract().as(GuestResponse.class);
        GuestRequest guestAtualizado = GuestFactory.guestAtualizarNomeEmailPhone(novoGuestRequest);

        GuestResponse guestResponse = guestClient.atualizarGuest(Utils.convertGuestToJson(guestAtualizado), guestCadastrado.getId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(GuestResponse.class);

        assertEquals(guestAtualizado.getName(), guestResponse.getName());
        assertEquals(guestAtualizado.getPhone(), guestResponse.getPhone());
        assertEquals(guestAtualizado.getEmail(), guestResponse.getEmail());
        assertEquals(guestAtualizado.getDateOfBirth(), guestResponse.getDateOfBirth());
        assertEquals(guestAtualizado.getSocialSecurityNumber(), guestResponse.getSocialSecurityNumber());

        guestClient.deletarGuest(guestCadastrado.getId());
    }

    @Test
    @Story("Deve retornar erro padr??o ao tentar atualizar guest")
    public void testMustReturnErroWhenUpdatingClientWithEmptyFields() {

        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();
        GuestResponse guestCadastrado = guestClient.cadastrarGuest(Utils.convertGuestToJson(novoGuestRequest))
                .then().extract().as(GuestResponse.class);

        guestClient.atualizarGuest(Utils.convertGuestToJson(GuestFactory.guestComTodosCamposVazios()), guestCadastrado.getId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString(GuestData.NAME_NOT_EMPTY))
                .body(containsString(GuestData.PHONE_NOT_EMPTY))
                .body(containsString(GuestData.CPF_INVALID))
                .body(containsString(GuestData.BIRTHDAY_NOT_EMPTY))
                .body(containsString(GuestData.EMAIL_NOT_EMPTY));

        guestClient.deletarGuest(guestCadastrado.getId());
    }

    @Test
    @Story("Deve retornar erro padr??o ao tentar atualizar guest")
    public void testMustReturnErrorWhenUpdatingClientCpfToOneAlreadyRegistered() {

        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();
        GuestResponse guestCadastrado = guestClient.cadastrarGuest(Utils.convertGuestToJson(novoGuestRequest))
                .then().extract().as(GuestResponse.class);
        GuestRequest novoGuestRequest2 = GuestFactory.guestCompleto();
        GuestResponse guestCadastrado2 = guestClient.cadastrarGuest(Utils.convertGuestToJson(novoGuestRequest2))
                .then().extract().as(GuestResponse.class);
        novoGuestRequest2.setSocialSecurityNumber(guestCadastrado.getSocialSecurityNumber());

        guestClient.atualizarGuest(Utils.convertGuestToJson(novoGuestRequest2), guestCadastrado2.getId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString(ValuesData.CPF_ALREADY_REGISTERED));

        guestClient.deletarGuest(guestCadastrado.getId());
        guestClient.deletarGuest(guestCadastrado2.getId());
    }
}
