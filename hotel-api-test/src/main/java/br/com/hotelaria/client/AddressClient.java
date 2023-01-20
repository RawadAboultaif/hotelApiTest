package br.com.hotelaria.client;

import br.com.hotelaria.data.changeless.AddressData;
import br.com.hotelaria.data.changeless.EmployeeData;
import br.com.hotelaria.data.changeless.GuestData;
import br.com.hotelaria.specs.AuthSpecs;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AddressClient {

    public Response cadastrarAddressIdGuest(String address, Integer idGuest) {

        return
                given()
                    .log().all()
                    .spec(AuthSpecs.requestSpec())
                    .pathParam(GuestData.ID_GUEST, idGuest)
                    .body(address)
                .when()
                    .post(AddressData.ENDPOINT_ADDRESS_IDGUEST + String.format("{%s}", GuestData.ID_GUEST))
                ;
    }

    public Response cadastrarAddressIdEmployee(String address, Integer idEmployee) {

        return
                given()
                    .log().all()
                    .spec(AuthSpecs.requestSpec())
                    .pathParam(EmployeeData.ID_EMPLOYEE, idEmployee)
                    .body(address)
                .when()
                    .post(AddressData.ENDPOINT_ADDRESS_IDEMPLOYEE + String.format("{%s}", EmployeeData.ID_EMPLOYEE))
                ;
    }

    public Response deletarAddress(Integer idAddress) {
        return
                given()
                    .log().all()
                    .spec(AuthSpecs.requestSpec())
                    .pathParam(AddressData.ID_ADDRESS, idAddress)
                .when()
                    .delete(AddressData.ENDPOINT_ADDRESS + String.format("{%s}", AddressData.ID_ADDRESS))
                ;
    }

    public Response atualizarAddress(String address, Integer idAddress) {
        return
                given()
                    .log().all()
                    .spec(AuthSpecs.requestSpec())
                    .body(address)
                    .pathParam(AddressData.ID_ADDRESS, idAddress)
                .when()
                    .put(AddressData.ENDPOINT_ADDRESS_UPDATE + String.format("{%s}", AddressData.ID_ADDRESS))
                ;
    }

    public Response buscarAddress(Integer idAddress) {
        return
                given()
                    .log().all()
                    .spec(AuthSpecs.requestSpec())
                    .pathParam(AddressData.ID_ADDRESS, idAddress)
                .when()
                    .get(AddressData.ENDPOINT_ADDRESS + String.format("{%s}", AddressData.ID_ADDRESS))
                ;
    }
}
