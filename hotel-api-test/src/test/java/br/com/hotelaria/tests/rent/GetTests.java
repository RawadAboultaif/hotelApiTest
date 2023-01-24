package br.com.hotelaria.tests.rent;

import br.com.hotelaria.client.GuestClient;
import br.com.hotelaria.client.RentClient;
import br.com.hotelaria.client.UnitClient;
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

@Epic("Busca Tests")
@Feature("Rent")
@DisplayName("Busca Rent")
public class GetTests extends BaseTest {

    RentClient rentClient = new RentClient();
    UnitClient unitClient = new UnitClient();
    GuestClient guestClient = new GuestClient();

    @Test
    @Story("Deve buscar rent com sucesso")
    public void testMustFindRentById() {

        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();
        UnitRequest unitRequest = UnitFactory.novoUnit();
        RentRequest rentRequest = RentFactory.novoRent();

        GuestResponse guestResponse = guestClient.cadastrarGuest(Utils.convertGuestToJson(novoGuestRequest))
                .then().extract().as(GuestResponse.class);

        UnitResponse unitResponse = unitClient.cadastroUnit(Utils.convertUnitToJson(unitRequest))
                .then().extract().as(UnitResponse.class);

        RentResponse rentCadastrado = rentClient.cadastroRent(Utils.convertRentToJson(rentRequest), guestResponse.getId(), unitResponse.getName())
                .then().extract().as(RentResponse.class);

        RentResponse rentResponse = rentClient.buscarRent(rentCadastrado.getId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(RentResponse.class);

        assertEquals(rentResponse.getId(), rentCadastrado.getId());
        assertEquals(rentResponse.getCheckIn(), rentCadastrado.getCheckIn());
        assertEquals(rentResponse.getCheckOut(), rentCadastrado.getCheckOut());
        assertEquals(rentResponse.getTotalPrice(), rentCadastrado.getTotalPrice());
        assertEquals(rentResponse.getGuest(), guestResponse);
        assertEquals(rentResponse.getUnit().getLimitGuest(), unitResponse.getLimitGuest());
        assertEquals(rentResponse.getUnit().getPrice(), unitResponse.getPrice());
        assertEquals(rentResponse.getUnit().getName(), unitResponse.getName());
        assertEquals(rentResponse.getUnit().getStatus(), UnitData.UNIT_F);

        guestClient.deletarGuest(guestResponse.getId());
        unitClient.deleteUnit(unitResponse.getId());
    }

    @Test
    @Story("Deve retornar erro padrão ao buscar rent")
    public void testMustReturnErrorWhenSearchinRentWithNonExistentId() {

        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();
        UnitRequest unitRequest = UnitFactory.novoUnit();
        RentRequest rentRequest = RentFactory.novoRent();

        GuestResponse guestResponse = guestClient.cadastrarGuest(Utils.convertGuestToJson(novoGuestRequest))
                .then().extract().as(GuestResponse.class);

        UnitResponse unitResponse = unitClient.cadastroUnit(Utils.convertUnitToJson(unitRequest))
                .then().extract().as(UnitResponse.class);

        RentResponse rentCadastrado = rentClient.cadastroRent(Utils.convertRentToJson(rentRequest), guestResponse.getId(), unitResponse.getName())
                .then().extract().as(RentResponse.class);

        rentClient.deletarRent(rentCadastrado.getId());
        rentClient.buscarRent(rentCadastrado.getId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString(ValuesData.ID_DONT_EXIST));


        guestClient.deletarGuest(guestResponse.getId());
        unitClient.deleteUnit(unitResponse.getId());
    }
}
