package br.com.hotelaria.client;

import br.com.hotelaria.data.changeless.GuestData;
import br.com.hotelaria.data.changeless.RentData;
import br.com.hotelaria.data.changeless.UnitData;
import br.com.hotelaria.specs.AuthSpecs;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class RentClient {

    public Response cadastroRent(String rent, Integer idGuest, String unitName) {
        return
                given()
                    .log().all()
                    .spec(AuthSpecs.requestSpec())
                    .body(rent)
                    .pathParam(GuestData.GUEST_ID, idGuest)
                    .pathParam(UnitData.UNIT_NAME, unitName)
                .when()
                    .post(RentData.ENDPOINT_RENT + String.format("{%s}/{%s}", GuestData.GUEST_ID, UnitData.UNIT_NAME))
                ;
    }

    public Response deletarRent(Integer idRent) {
        return
                given()
                    .log().all()
                    .pathParam(RentData.ID_RENT, idRent)
                .when()
                    .delete(RentData.ENDPOINT_RENT + String.format("{%s}", RentData.ID_RENT))
                ;
    }

    public Response buscarRent(Integer idRent) {
        return
                given()
                    .log().all()
                    .pathParam(RentData.ID_RENT, idRent)
                .when()
                    .get(RentData.ENDPOINT_RENT + String.format("{%s}", RentData.ID_RENT))
                ;
    }
}
