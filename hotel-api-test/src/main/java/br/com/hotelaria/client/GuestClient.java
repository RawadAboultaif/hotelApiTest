package br.com.hotelaria.client;

import br.com.hotelaria.data.changeless.GuestData;
import br.com.hotelaria.specs.AuthSpecs;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class GuestClient {

    public Response cadastrar(String guest) {

        return
                given()
                    .log().all()
                    .spec(AuthSpecs.requestSpec())
                    .body(guest)
                .when()
                    .post(GuestData.ENDPOINT_GUEST)
                ;
    }

    public Response deletar(Integer idGuest) {
        return
                given()
                    .log().all()
                    .spec(AuthSpecs.requestSpec())
                    .pathParam(GuestData.ID_GUEST, idGuest)
                .when()
                    .delete(GuestData.ENDPOINT_GUEST + String.format("{%s}", GuestData.ID_GUEST))
                ;
    }

    public Response buscarTodosGuest() {
        return
                given()
                    .log().all()
                    .spec(AuthSpecs.requestSpec())
                .when()
                    .get(GuestData.ENDPOINT_LIST_ALL_GUEST)
                ;
    }

    public Response buscarGuestPorCpf(String cpf) {
        return
                given()
                    .log().all()
                    .spec(AuthSpecs.requestSpec())
                    .pathParam(GuestData.SOCIAL_SECURITY_NUMBER, cpf)
                .when()
                    .get(GuestData.ENDPOINT_BUSCA_POR_CPF + String.format("{%s}", GuestData.SOCIAL_SECURITY_NUMBER))
                ;
    }
}
