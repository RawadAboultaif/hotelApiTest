package br.com.hotelaria.tests.rent;

import br.com.hotelaria.client.GuestClient;
import br.com.hotelaria.client.RentClient;
import br.com.hotelaria.client.UnitClient;
import br.com.hotelaria.data.changeless.RentData;
import br.com.hotelaria.data.changeless.UnitData;
import br.com.hotelaria.data.changeless.ValuesData;
import br.com.hotelaria.data.factory.GuestFactory;
import br.com.hotelaria.data.factory.RentFactory;
import br.com.hotelaria.data.factory.UnitFactory;
import br.com.hotelaria.dto.guest.GuestRequest;
import br.com.hotelaria.dto.guest.GuestResponse;
import br.com.hotelaria.dto.rent.RentRequest;
import br.com.hotelaria.dto.rent.RentResponse;
import br.com.hotelaria.dto.unit.UnitRequest;
import br.com.hotelaria.dto.unit.UnitResponse;
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

@Epic("Cadastro Tests")
@Feature("Rent")
@DisplayName("Cadastro Rent")
public class PostTests extends BaseTest {

    RentClient rentClient = new RentClient();
    UnitClient unitClient = new UnitClient();
    GuestClient guestClient = new GuestClient();

    @Test
    @Story("Deve cadastrar rent com sucesso")
    public void testMustSaveRentAndLinkToGuestAndUnit() {

        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();
        UnitRequest unitRequest = UnitFactory.novoUnit();
        RentRequest rentRequest = RentFactory.novoRent();

        GuestResponse guestResponse = guestClient.cadastrarGuest(Utils.convertGuestToJson(novoGuestRequest))
                .then().extract().as(GuestResponse.class);

        UnitResponse unitResponse = unitClient.cadastroUnit(Utils.convertUnitToJson(unitRequest))
                .then().extract().as(UnitResponse.class);

        RentResponse rentResponse = rentClient.cadastroRent(Utils.convertRentToJson(rentRequest), guestResponse.getId(), unitResponse.getName())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().as(RentResponse.class);

        assertEquals(rentRequest.getCheckIn(), rentResponse.getCheckIn());
        assertEquals(rentRequest.getCheckOut(), rentResponse.getCheckOut());

        guestClient.deletarGuest(guestResponse.getId());
        unitClient.deleteUnit(unitResponse.getId());
    }

    @Test
    @Story("Deve retornar erro padrão ao tentar cadastrar unit")
    public void testMustReturnErrorWhenSavingRentWithNonExistentGuest() {

        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();
        UnitRequest unitRequest = UnitFactory.novoUnit();
        RentRequest rentRequest = RentFactory.novoRent();

        GuestResponse guestResponse = guestClient.cadastrarGuest(Utils.convertGuestToJson(novoGuestRequest))
                .then().extract().as(GuestResponse.class);

        UnitResponse unitResponse = unitClient.cadastroUnit(Utils.convertUnitToJson(unitRequest))
                .then().extract().as(UnitResponse.class);

        guestClient.deletarGuest(guestResponse.getId());

        rentClient.cadastroRent(Utils.convertRentToJson(rentRequest), guestResponse.getId(), unitResponse.getName())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString(ValuesData.ID_DONT_EXIST));

        unitClient.deleteUnit(unitResponse.getId());
    }

    @Test
    @Story("Deve retornar erro padrão ao tentar cadastrar unit")
    public void testMustReturnErrorWhenSavingRentWithNonExistentUnit() {

        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();
        UnitRequest unitRequest = UnitFactory.novoUnit();
        RentRequest rentRequest = RentFactory.novoRent();

        GuestResponse guestResponse = guestClient.cadastrarGuest(Utils.convertGuestToJson(novoGuestRequest))
                .then().extract().as(GuestResponse.class);

        UnitResponse unitResponse = unitClient.cadastroUnit(Utils.convertUnitToJson(unitRequest))
                .then().extract().as(UnitResponse.class);

        unitClient.deleteUnit(unitResponse.getId());

        rentClient.cadastroRent(Utils.convertRentToJson(rentRequest), guestResponse.getId(), unitResponse.getName())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString(UnitData.ROOM_NAME_DONT_EXIST));

        guestClient.deletarGuest(guestResponse.getId());
    }

    @Test
    @Story("Deve retornar erro padrão ao tentar cadastrar unit")
    public void testMustReturnErrorWhenSavingRentWithCheckInDateAfterCheckOut() {

        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();
        UnitRequest unitRequest = UnitFactory.novoUnit();
        RentRequest rentRequest = RentFactory.rentWithCheckInAfterCheckOut();

        GuestResponse guestResponse = guestClient.cadastrarGuest(Utils.convertGuestToJson(novoGuestRequest))
                .then().extract().as(GuestResponse.class);

        UnitResponse unitResponse = unitClient.cadastroUnit(Utils.convertUnitToJson(unitRequest))
                .then().extract().as(UnitResponse.class);

        rentClient.cadastroRent(Utils.convertRentToJson(rentRequest), guestResponse.getId(), unitResponse.getName())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString(RentData.CHECKIN_GREATER_THAN_CHECKOUT));

        guestClient.deletarGuest(guestResponse.getId());
        unitClient.deleteUnit(unitResponse.getId());
    }

    @Test
    @Story("Deve retornar erro padrão ao tentar cadastrar unit")
    public void testMustReturnErrorWhenSavingRentWithEmptyFields() {

        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();
        UnitRequest unitRequest = UnitFactory.novoUnit();
        RentRequest rentRequest = RentFactory.rentEmptyCamposVazios();

        GuestResponse guestResponse = guestClient.cadastrarGuest(Utils.convertGuestToJson(novoGuestRequest))
                .then().extract().as(GuestResponse.class);

        UnitResponse unitResponse = unitClient.cadastroUnit(Utils.convertUnitToJson(unitRequest))
                .then().extract().as(UnitResponse.class);

        rentClient.cadastroRent(Utils.convertRentToJson(rentRequest), guestResponse.getId(), unitResponse.getName())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString(RentData.CHECKIN_CANNOT_BE_NULL))
                .body(containsString(RentData.CHECKOUT_CANNOT_BE_NUll));

        guestClient.deletarGuest(guestResponse.getId());
        unitClient.deleteUnit(unitResponse.getId());
    }
}
