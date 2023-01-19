package br.com.hotelaria.client;

import br.com.hotelaria.data.changeless.EmployeeData;
import br.com.hotelaria.data.changeless.GuestData;
import br.com.hotelaria.specs.AuthSpecs;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class EmployeeClient {

    public Response cadastrarEmployee(String employee) {

        return
                given()
                    .log().all()
                    .spec(AuthSpecs.requestSpec())
                    .body(employee)
                .when()
                    .post(EmployeeData.ENDPOINT_EMPLOYEE)
                ;
    }

    public Response deletarEmployee(Integer idEmployee) {
        return
                given()
                    .log().all()
                    .spec(AuthSpecs.requestSpec())
                    .pathParam(EmployeeData.ID_EMPLOYEE, idEmployee)
                .when()
                    .delete(EmployeeData.ENDPOINT_EMPLOYEE + String.format("{%s}", EmployeeData.ID_EMPLOYEE))
                ;
    }

    public Response buscarTodosEmployee() {
        return
                given()
                    .log().all()
                    .spec(AuthSpecs.requestSpec())
                .when()
                    .get(EmployeeData.ENDPOINT_LIST_ALL_EMPLOYEE)
                ;
    }

    public Response buscarGuestPorCpf(String cpf) {
        return
                given()
                    .log().all()
                    .spec(AuthSpecs.requestSpec())
                    .pathParam(EmployeeData.SOCIAL_SECURITY_NUMBER_EMPLOYEE, cpf)
                .when()
                    .get(EmployeeData.ENDPOINT_BUSCA_POR_CPF_EMPLOYEE + String.format("{%s}", EmployeeData.SOCIAL_SECURITY_NUMBER_EMPLOYEE))
                ;
    }

    public Response atualizarEmployee(String guest, Integer id) {
        return
                given()
                    .log().all()
                    .spec(AuthSpecs.requestSpec())
                    .pathParam(EmployeeData.ID_EMPLOYEE, id)
                    .body(guest)
                .when()
                    .put(EmployeeData.ENDPOINT_ATUALIZAR_EMPLOYEE + String.format("{%s}", EmployeeData.ID_EMPLOYEE))
                ;
    }
}
