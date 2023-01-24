package br.com.hotelaria.client;

import br.com.hotelaria.data.changeless.GuestData;
import br.com.hotelaria.specs.AuthSpecs;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class GuestClient {

    public Response cadastrarGuest(String guest) {
        return
                given()
                    .log().all()
                    .spec(AuthSpecs.requestSpec())
                    .body(guest)
                .when()
                    .post(GuestData.ENDPOINT_GUEST)
                ;
    }

    public Response deletarGuest(Integer idGuest) {
        return
                given()
                    .log().all()
                    .pathParam(GuestData.GUEST_ID, idGuest)
                .when()
                    .delete(GuestData.ENDPOINT_GUEST + String.format("{%s}", GuestData.GUEST_ID))
                ;
    }

    public Response buscarTodosGuest() {
        return
                given()
                    .log().all()
                .when()
                    .get(GuestData.ENDPOINT_LIST_ALL_GUEST)
                ;
    }

    public Response buscarGuestPorCpf(String cpf) {
        return
                given()
                    .log().all()
                    .spec(AuthSpecs.requestSpec())
                    .pathParam(GuestData.SOCIAL_SECURITY_NUMBER_GUEST, cpf)
                .when()
                    .get(GuestData.ENDPOINT_BUSCA_POR_CPF_GUEST + String.format("{%s}", GuestData.SOCIAL_SECURITY_NUMBER_GUEST))
                ;
    }

    public Response atualizarGuest(String guest, Integer id) {
        return
                given()
                    .log().all()
                    .spec(AuthSpecs.requestSpec())
                    .pathParam(GuestData.GUEST_ID, id)
                    .body(guest)
                .when()
                    .put(GuestData.ENDPOINT_ATUALIZAR_GUEST + String.format("{%s}", GuestData.GUEST_ID))
                ;
    }
}
