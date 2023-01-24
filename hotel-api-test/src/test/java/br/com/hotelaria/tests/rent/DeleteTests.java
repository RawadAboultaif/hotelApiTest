package br.com.hotelaria.tests.rent;

import br.com.hotelaria.client.GuestClient;
import br.com.hotelaria.client.RentClient;
import br.com.hotelaria.client.UnitClient;
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

@Epic("Delete Tests")
@Feature("Rent")
@DisplayName("Delete Rent")
public class DeleteTests extends BaseTest {

    RentClient rentClient = new RentClient();
    UnitClient unitClient = new UnitClient();
    GuestClient guestClient = new GuestClient();

    @Test
    @Story("Deve retornar erro ao deletar rent")
    public void testMustReturnErrorWhenDeletingRentNonExistent() {

        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();
        UnitRequest unitRequest = UnitFactory.novoUnit();
        RentRequest rentRequest = RentFactory.novoRent();

        GuestResponse guestResponse = guestClient.cadastrarGuest(Utils.convertGuestToJson(novoGuestRequest))
                .then().extract().as(GuestResponse.class);

        UnitResponse unitResponse = unitClient.cadastroUnit(Utils.convertUnitToJson(unitRequest))
                .then().extract().as(UnitResponse.class);

        RentResponse rentResponse = rentClient.cadastroRent(Utils.convertRentToJson(rentRequest), guestResponse.getId(), unitResponse.getName())
                .then().extract().as(RentResponse.class);

        rentClient.deletarRent(rentResponse.getId());
        rentClient.deletarRent(rentResponse.getId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString(ValuesData.ID_DONT_EXIST));

        guestClient.deletarGuest(guestResponse.getId());
        unitClient.deleteUnit(unitResponse.getId());
    }
}
