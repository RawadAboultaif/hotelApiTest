package br.com.hotelaria.client;

import br.com.hotelaria.data.changeless.GuestData;
import br.com.hotelaria.data.changeless.PaymentData;
import br.com.hotelaria.specs.AuthSpecs;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class PaymentClient {

    public Response cadastroPayment(String payment, Integer idGuest) {
        return
                given()
                        .log().all()
                        .spec(AuthSpecs.requestSpec())
                        .pathParam(GuestData.ID_GUEST, idGuest)
                        .body(payment)
                .when()
                        .post(PaymentData.ENDPOINT_VINCULAR_GUEST_PAYMENT + String.format("{%s}", GuestData.ID_GUEST))
                ;
    }

    public Response deletePayment(Integer idPayment) {
        return
                given()
                    .log().all()
                    .spec(AuthSpecs.requestSpec())
                    .pathParam(PaymentData.ID_PAYMENT, idPayment)
                .when()
                    .delete(PaymentData.ENDPOINT_PAYMENT + String.format("{%s}", PaymentData.ID_PAYMENT))
                ;
    }

    public Response atualizarPayment(String payment, Integer idPayment) {
        return
                given()
                    .log().all()
                    .spec(AuthSpecs.requestSpec())
                    .pathParam(PaymentData.ID_PAYMENT, idPayment)
                    .body(payment)
                .when()
                    .put(PaymentData.ENDPOINT_UPDATE_PAYMENT + String.format("{%s}", PaymentData.ID_PAYMENT))
                ;
    }

    public Response buscaPaymentPorId(Integer idPayment) {
        return
                given()
                    .log().all()
                    .spec(AuthSpecs.requestSpec())
                    .pathParam(PaymentData.ID_PAYMENT, idPayment)
                .when()
                    .get(PaymentData.ENDPOINT_PAYMENT + String.format("{%s}", PaymentData.ID_PAYMENT))
                ;
    }
}
