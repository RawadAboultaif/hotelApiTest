package br.com.hotelaria.tests.payment;

import br.com.hotelaria.client.GuestClient;
import br.com.hotelaria.client.PaymentClient;
import br.com.hotelaria.data.changeless.PaymentData;
import br.com.hotelaria.data.changeless.ValuesData;
import br.com.hotelaria.data.factory.GuestFactory;
import br.com.hotelaria.data.factory.PaymentFactory;
import br.com.hotelaria.dto.guest.GuestRequest;
import br.com.hotelaria.dto.guest.GuestResponse;
import br.com.hotelaria.dto.payment.PaymentRequest;
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
@Feature("Payment")
@DisplayName("Cadastro Payment")
public class PostTests extends BaseTest {

    GuestClient guestClient = new GuestClient();
    PaymentClient paymentClient = new PaymentClient();

    @Test
    @Story("Deve cadastrar payment com sucesso")
    public void testMustSavePaymentAndLinkWithClient() {

        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();
        PaymentRequest novoPaymentRequest = PaymentFactory.novoPaymentVálido();

        GuestResponse guestResponse = guestClient.cadastrarGuest(Utils.convertGuestToJson(novoGuestRequest))
                .then().extract().as(GuestResponse.class);

        paymentClient.cadastroPayment(Utils.convertPaymentToJson(novoPaymentRequest), guestResponse.getId());

        GuestResponse guestFullResponse = guestClient.buscarGuestPorCpf(guestResponse.getSocialSecurityNumber())
                .then().log().all().extract().as(GuestResponse.class);

        assertEquals(novoPaymentRequest.getMethod(), guestFullResponse.getGuestPayment().get(0).getMethod());
        assertEquals(novoPaymentRequest.getCard(), guestFullResponse.getGuestPayment().get(0).getCard());

        guestClient.deletarGuest(guestFullResponse.getId());
    }

    @Test
    @Story("Deve retornar erro padrão ao tentar cadastrar payment")
    public void testMustReturnErrorWhenSavingPaymentToNonExistentClient() {

        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();
        GuestResponse guestResponse = guestClient.cadastrarGuest(Utils.convertGuestToJson(novoGuestRequest))
                .then().extract().as(GuestResponse.class);
        guestClient.deletarGuest(guestResponse.getId());

        PaymentRequest novoPaymentRequest = PaymentFactory.novoPaymentVálido();
        paymentClient.cadastroPayment(Utils.convertPaymentToJson(novoPaymentRequest), guestResponse.getId())
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString(ValuesData.ID_DONT_EXIST));

        guestClient.deletarGuest(guestResponse.getId());
    }

    @Test
    @Story("Deve retornar erro padrão ao tentar cadastrar payment")
    public void testMustReturnErroWhenSavingPaymentoWithInvalidCreditCardNumber() {

        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();
        GuestResponse guestResponse = guestClient.cadastrarGuest(Utils.convertGuestToJson(novoGuestRequest))
                .then().extract().as(GuestResponse.class);

        PaymentRequest paymentRequestNumeroCartaoInvalido = PaymentFactory.novoPaymentNumeroCartaoInvalido();
        paymentClient.cadastroPayment(Utils.convertPaymentToJson(paymentRequestNumeroCartaoInvalido), guestResponse.getId())
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString(PaymentData.CARD_INVALID));

        guestClient.deletarGuest(guestResponse.getId());
    }
}
