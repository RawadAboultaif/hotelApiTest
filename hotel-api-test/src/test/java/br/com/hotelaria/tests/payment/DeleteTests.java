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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsString;


@Epic("Delete Tests")
@Feature("Payment")
@DisplayName("Delete Payment")
public class DeleteTests extends BaseTest {

    GuestClient guestClient = new GuestClient();
    PaymentClient paymentClient = new PaymentClient();

    @Test
    @Story("Deve retornar erro ao deletar payment")
    public void testMustReturnErrorWhenDeletingNonExistentPayment() {


        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();
        PaymentRequest novoPaymentRequest = PaymentFactory.novoPaymentVálido();

        GuestResponse guestResponse = guestClient.cadastrarGuest(Utils.convertGuestToJson(novoGuestRequest))
                .then().extract().as(GuestResponse.class);

        PaymentResponse paymentResponse = paymentClient.cadastroPayment(Utils.convertPaymentToJson(novoPaymentRequest), guestResponse.getId())
                .then().extract().as(PaymentResponse.class);

        paymentClient.deletePayment(paymentResponse.getId());
        paymentClient.deletePayment(paymentResponse.getId())
                .then()
                .body(containsString(ValuesData.ID_DONT_EXIST));

        guestClient.deletarGuest(guestResponse.getId());
    }
}
