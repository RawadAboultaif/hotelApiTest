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
import br.com.hotelaria.dto.payment.PaymentResponse;
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
@Feature("Payment")
@DisplayName("Atualizar Payment")
public class PutTests extends BaseTest {

    GuestClient guestClient = new GuestClient();
    PaymentClient paymentClient = new PaymentClient();

    @Test
    @Story("Deve atualizar payment com sucesso")
    public void testMustUpdatePayment() {

        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();
        PaymentRequest paymentRequestAntigo = PaymentFactory.novoPaymentV├ílido();
        PaymentRequest paymentRequestNovo = PaymentFactory.novoPaymentV├ílido();

        GuestResponse guestResponse = guestClient.cadastrarGuest(Utils.convertGuestToJson(novoGuestRequest))
                .then().extract().as(GuestResponse.class);

        PaymentResponse paymentCadastrado = paymentClient.cadastroPayment(Utils.convertPaymentToJson(paymentRequestAntigo), guestResponse.getId())
                .then().extract().as(PaymentResponse.class);

        PaymentResponse paymentAtualizado = paymentClient.atualizarPayment(Utils.convertPaymentToJson(paymentRequestNovo), paymentCadastrado.getId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(PaymentResponse.class);

        assertEquals(paymentRequestNovo.getMethod(), paymentAtualizado.getMethod());
        assertEquals(paymentRequestNovo.getCard(), paymentAtualizado.getCard());

        guestClient.deletarGuest(guestResponse.getId());
    }

    @Test
    @Story("Deve retornar erro padr├úo ao tentar atualizar payment")
    public void testMustReturnErrorWhenUpdatingPaymentWithNonExistentId () {

        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();
        PaymentRequest paymentRequestAntigo = PaymentFactory.novoPaymentV├ílido();
        PaymentRequest paymentRequestNovo = PaymentFactory.novoPaymentV├ílido();

        GuestResponse guestResponse = guestClient.cadastrarGuest(Utils.convertGuestToJson(novoGuestRequest))
                .then().extract().as(GuestResponse.class);

        PaymentResponse paymentCadastrado = paymentClient.cadastroPayment(Utils.convertPaymentToJson(paymentRequestAntigo), guestResponse.getId())
                .then().extract().as(PaymentResponse.class);
        paymentClient.deletePayment(paymentCadastrado.getId());

        paymentClient.atualizarPayment(Utils.convertPaymentToJson(paymentRequestNovo), paymentCadastrado.getId())
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString(ValuesData.ID_DONT_EXIST));

        guestClient.deletarGuest(guestResponse.getId());
    }

    @Test
    @Story("Deve retornar erro padr├úo ao tentar atualizar payment")
    public void testMustReturnErroWhenUpdatingPaymentWithInvalidCreditCardNumber() {

        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();
        PaymentRequest paymentRequestAntigo = PaymentFactory.novoPaymentV├ílido();
        PaymentRequest paymentRequestNovo = PaymentFactory.novoPaymentNumeroCartaoInvalido();

        GuestResponse guestResponse = guestClient.cadastrarGuest(Utils.convertGuestToJson(novoGuestRequest))
                .then().extract().as(GuestResponse.class);

        PaymentResponse paymentCadastrado = paymentClient.cadastroPayment(Utils.convertPaymentToJson(paymentRequestAntigo), guestResponse.getId())
                .then().extract().as(PaymentResponse.class);

        paymentClient.atualizarPayment(Utils.convertPaymentToJson(paymentRequestNovo), paymentCadastrado.getId())
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString(PaymentData.CARD_INVALID));

        guestClient.deletarGuest(guestResponse.getId());
    }
}
