package br.com.hotelaria.tests.payment;

import br.com.hotelaria.client.GuestClient;
import br.com.hotelaria.client.PaymentClient;
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

@Epic("Busca Tests")
@Feature("Payment")
@DisplayName("Busca Payment")
public class GetTests extends BaseTest {

    GuestClient guestClient = new GuestClient();
    PaymentClient paymentClient = new PaymentClient();

    @Test
    @Story("Deve buscar payment com sucesso")
    public void testMustFindPaymentById() {

        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();
        PaymentRequest novoPaymentRequest = PaymentFactory.novoPaymentVálido();

        GuestResponse guestResponse = guestClient.cadastrarGuest(Utils.convertGuestToJson(novoGuestRequest))
                .then().extract().as(GuestResponse.class);

        PaymentResponse paymentCadastrado = paymentClient.cadastroPayment(Utils.convertPaymentToJson(novoPaymentRequest), guestResponse.getId())
                .then().extract().as(PaymentResponse.class);

        PaymentResponse paymentBuscado = paymentClient.buscaPaymentPorId(paymentCadastrado.getId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(PaymentResponse.class);

        assertEquals(novoPaymentRequest.getMethod(), paymentBuscado.getMethod());
        assertEquals(novoPaymentRequest.getCard(), paymentBuscado.getCard());

        guestClient.deletarGuest(guestResponse.getId());
    }

    @Test
    @Story("Deve retornar erro padrão ao buscar payment")
    public void testMustReturnErrorWhenSearchingPaymentByNonExistentId() {

        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();
        PaymentRequest novoPaymentRequest = PaymentFactory.novoPaymentVálido();

        GuestResponse guestResponse = guestClient.cadastrarGuest(Utils.convertGuestToJson(novoGuestRequest))
                .then().extract().as(GuestResponse.class);

        PaymentResponse paymentCadastrado = paymentClient.cadastroPayment(Utils.convertPaymentToJson(novoPaymentRequest), guestResponse.getId())
                .then().extract().as(PaymentResponse.class);
        paymentClient.deletePayment(paymentCadastrado.getId());

        paymentClient.buscaPaymentPorId(paymentCadastrado.getId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString(ValuesData.ID_DONT_EXIST));
    }
}
