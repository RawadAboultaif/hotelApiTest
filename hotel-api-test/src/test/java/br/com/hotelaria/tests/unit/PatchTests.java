package br.com.hotelaria.tests.unit;

import br.com.hotelaria.client.UnitClient;
import br.com.hotelaria.data.changeless.UnitData;
import br.com.hotelaria.data.factory.UnitFactory;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Patch Tests")
@Feature("Unit")
@DisplayName("Patch Unit")
public class PatchTests extends BaseTest {

    UnitClient unitClient = new UnitClient();

    @Test
    @Story("Deve atualizar unit com sucesso")
    public void testMustUpdateUnitCheckIn() {

        UnitRequest unitRequest = UnitFactory.novoUnit();

        UnitResponse unitResponse = unitClient.cadastroUnit(Utils.convertUnitToJson(unitRequest))
                .then().extract().as(UnitResponse.class);

        UnitResponse unitCheckIn = unitClient.checkInUnit(unitResponse.getName())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(UnitResponse.class);

        assertEquals(unitResponse.getId(), unitCheckIn.getId());
        assertEquals(unitResponse.getName(), unitCheckIn.getName());
        assertEquals(unitResponse.getPrice(), unitCheckIn.getPrice());
        assertEquals(unitResponse.getLimitGuest(), unitCheckIn.getLimitGuest());
        assertTrue(unitCheckIn.getStatus().equals(UnitData.UNIT_FULL));

        unitClient.deleteUnit(unitResponse.getId());
    }

    @Test
    @Story("Deve retornar erro padrão ao tentar atualizar unit")
    public void testMustReturnErrorWhenCheckinInNonExistentUnitId() {

        UnitRequest unitRequest = UnitFactory.novoUnit();

        UnitResponse unitResponse = unitClient.cadastroUnit(Utils.convertUnitToJson(unitRequest))
                .then().extract().as(UnitResponse.class);

        unitClient.deleteUnit(unitResponse.getId());

        unitClient.checkInUnit(unitResponse.getName())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString(UnitData.ROOM_NAME_DONT_EXIST));
    }

    @Test
    @Story("Deve atualizar unit com sucesso")
    public void testMustUpdateUnitCheckOut() {

        UnitRequest unitRequest = UnitFactory.novoUnit();

        UnitResponse unitResponse = unitClient.cadastroUnit(Utils.convertUnitToJson(unitRequest))
                .then().extract().as(UnitResponse.class);

        unitClient.checkInUnit(unitResponse.getName())
                .then().extract().as(UnitResponse.class);

        UnitResponse unitCheckOut = unitClient.checkOutUnit(unitResponse.getName())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(UnitResponse.class);

        assertEquals(unitResponse.getId(), unitCheckOut.getId());
        assertEquals(unitResponse.getName(), unitCheckOut.getName());
        assertEquals(unitResponse.getPrice(), unitCheckOut.getPrice());
        assertEquals(unitResponse.getLimitGuest(), unitCheckOut.getLimitGuest());
        assertTrue(unitCheckOut.getStatus().equals(UnitData.UNIT_EMPTY));

        unitClient.deleteUnit(unitResponse.getId());
    }

    @Test
    @Story("Deve retornar erro padrão ao tentar atualizar unit")
    public void testMustReturnErrorWhenCheckinOutNonExistentUnitId() {

        UnitRequest unitRequest = UnitFactory.novoUnit();

        UnitResponse unitResponse = unitClient.cadastroUnit(Utils.convertUnitToJson(unitRequest))
                .then().extract().as(UnitResponse.class);

        unitClient.deleteUnit(unitResponse.getId());

        unitClient.checkOutUnit(unitResponse.getName())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString(UnitData.ROOM_NAME_DONT_EXIST));
    }
}
