package br.com.hotelaria.tests.guest;

import br.com.hotelaria.client.GuestClient;
import br.com.hotelaria.data.changeless.GuestData;
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

@Epic("Cadastro Tests")
@Feature("Guest")
@DisplayName("Cadastro Guest")
public class PostTests extends BaseTest {

    GuestClient guestClient = new GuestClient();

    @Test
    @Story("Deve cadastrar guest com sucesso")
    public void testMustSaveGuest() {


        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();

        GuestResponse guestResponse = guestClient.cadastrarGuest(Utils.convertGuestToJson(novoGuestRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().as(GuestResponse.class);

        assertEquals(novoGuestRequest.getName(), guestResponse.getName());
        assertEquals(novoGuestRequest.getPhone(), guestResponse.getPhone());
        assertEquals(novoGuestRequest.getEmail(), guestResponse.getEmail());
        assertEquals(novoGuestRequest.getDateOfBirth(), guestResponse.getDateOfBirth());
        assertEquals(novoGuestRequest.getSocialSecurityNumber(), guestResponse.getSocialSecurityNumber());

        guestClient.deletarGuest(guestResponse.getId());
    }

    @Test
    @Story("Deve retornar erro padrão ao tentar cadastrar guest")
    public void testMustReturnErrorWhenSavingClientWithEmptyFields() {

        GuestRequest guestRequestVazio = GuestFactory.guestComTodosCamposVazios();
        guestClient.cadastrarGuest(Utils.convertGuestToJson(guestRequestVazio))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString(GuestData.NAME_NOT_EMPTY))
                .body(containsString(GuestData.PHONE_NOT_EMPTY))
                .body(containsString(GuestData.CPF_INVALID))
                .body(containsString(GuestData.BIRTHDAY_NOT_EMPTY))
                .body(containsString(GuestData.EMAIL_NOT_EMPTY));
    }

    @Test
    @Story("Deve retornar erro padrão ao tentar cadastrar guest")
    public void testMustReturnErrorWhenSavingClientWithBirthdayInTheFuture() {

        GuestRequest guestRequestComDataNascimentoFuturo = GuestFactory.guestCompletoComDataNascimentoNoFuturo();
        guestClient.cadastrarGuest(Utils.convertGuestToJson(guestRequestComDataNascimentoFuturo))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString(GuestData.CURRENT_DATE_AFTER_END_DATE));
    }

    @Test
    @Story("Deve retornar erro padrão ao tentar cadastrar guest")
    public void testMustReturnErrorWhenSavingClientWithAgeUnderEighteen() {

        GuestRequest guestRequestComIdadeMenorQueDezoito = GuestFactory.guestComIdadeMenorQueDezoito();

        guestClient.cadastrarGuest(Utils.convertGuestToJson(guestRequestComIdadeMenorQueDezoito))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString(GuestData.AGE_UNDER_EIGHTEEN));
    }
}
