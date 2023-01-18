package br.com.hotelaria.tests.guest;

import br.com.hotelaria.client.GuestClient;
import br.com.hotelaria.data.factory.GuestFactory;
import br.com.hotelaria.dto.guest.GuestResponse;
import br.com.hotelaria.dto.guest.GuestRequest;
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
@Feature("Guest")
@DisplayName("Cadastro Guest")
public class PostTests extends BaseTest {

    GuestClient guestClient = new GuestClient();

    @Test
    @Story("Deve cadastrar guest com sucesso")
    public void testeDeveCadastrarGuestValidoComSucesso() {


        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();

        GuestResponse guestResponse = guestClient.cadastrar(Utils.convertGuestToJson(novoGuestRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().as(GuestResponse.class);

        assertEquals(novoGuestRequest.getName(), guestResponse.getName());
        assertEquals(novoGuestRequest.getPhone(), guestResponse.getPhone());
        assertEquals(novoGuestRequest.getEmail(), guestResponse.getEmail());
        assertEquals(novoGuestRequest.getDateOfBirth(), guestResponse.getDateOfBirth());
        assertEquals(novoGuestRequest.getSocialSecurityNumber(), guestResponse.getSocialSecurityNumber());

        guestClient.deletar(guestResponse.getId());
    }

    @Test
    @Story("Deve erro padrão ao tentar cadastrar guest")
    public void testDeveRetornarErroPadraoAoCadastrarGuestComCamposVazios() {

        GuestRequest guestRequestVazio = GuestFactory.guestComTodosCamposVazios();
        guestClient.cadastrar(Utils.convertGuestToJson(guestRequestVazio))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("O nome não pode ser estar vazio"))
                .body(containsString("O telefone não pode estar vazio"))
                .body(containsString("O cpf não pode ser estar vazio"))
                .body(containsString("A data de nascimento não pode ser nulo"))
                .body(containsString("O email não pode estar vazio"));
    }

    @Test
    @Story("Deve erro padrão ao tentar cadastrar guest")
    public void testDeveRetornarErroPadraoAoCadastrarGuestComDataNascimentoNoFuturo() {

        GuestRequest guestRequestComDataNascimentoFuturo = GuestFactory.guestCompletoComDataNascimentoNoFuturo();
        guestClient.cadastrar(Utils.convertGuestToJson(guestRequestComDataNascimentoFuturo))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("A data tem quer anterior a data atual"));
    }
}
