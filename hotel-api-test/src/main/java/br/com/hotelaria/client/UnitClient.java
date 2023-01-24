package br.com.hotelaria.client;

import br.com.hotelaria.data.changeless.UnitData;
import br.com.hotelaria.specs.AuthSpecs;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UnitClient {

    public Response cadastroUnit(String unit) {
        return
                given()
                    .log().all()
                    .spec(AuthSpecs.requestSpec())
                    .body(unit)
                .when()
                    .post(UnitData.ENDPOINT_UNIT)
                ;
    }

    public Response deleteUnit(Integer idUnit) {
        return
                given()
                    .log().all()
                    .pathParam(UnitData.UNIT_ID, idUnit)
                .when()
                    .delete(UnitData.ENDPOINT_UNIT + String.format("{%s}", UnitData.UNIT_ID))
                ;
    }

    public Response buscarUnit(String unitName) {
        return
                given()
                    .log().all()
                    .spec(AuthSpecs.requestSpec())
                    .pathParam(UnitData.UNIT_NAME, unitName)
                .when()
                    .get(UnitData.ENDPOINT_UNIT + String.format("{%s}", UnitData.UNIT_NAME))
                ;
    }

    public Response updateUnit(String unit, Integer idUnit) {
        return
                given()
                    .log().all()
                    .spec(AuthSpecs.requestSpec())
                    .body(unit)
                    .pathParam(UnitData.UNIT_ID, idUnit)
                .when()
                    .put(UnitData.ENDPOINT_UPDATE_UNIT + String.format("{%s}", UnitData.UNIT_ID))
                ;
    }

    public Response checkInUnit(String unitName) {
        return
                given()
                    .log().all()
                    .spec(AuthSpecs.requestSpec())
                    .pathParam(UnitData.UNIT_NAME, unitName)
                .when()
                    .patch(UnitData.ENDPOINT_CHECKIN_UNIT + String.format("{%s}", UnitData.UNIT_NAME))
                ;
    }

    public Response checkOutUnit(String unitName) {
        return
                given()
                    .log().all()
                    .spec(AuthSpecs.requestSpec())
                    .pathParam(UnitData.UNIT_NAME, unitName)
                .when()
                    .patch(UnitData.ENDPOINT_CHECKOUT_UNIT + String.format("{%s}", UnitData.UNIT_NAME))
                ;
    }
}
