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

@Epic("Atualizar Tests")
@Feature("Unit")
@DisplayName("Atualizar Unit")
public class PutTests extends BaseTest {

    UnitClient unitClient = new UnitClient();

    @Test
    @Story("Deve atualizar unit com sucesso")
    public void testMustUpdateUnit() {

        UnitRequest unitRequest = UnitFactory.novoUnit();
        UnitRequest unitRequestUpdated = UnitFactory.novoUnit();

        UnitResponse unitResponse = unitClient.cadastroUnit(Utils.convertUnitToJson(unitRequest))
                .then().extract().as(UnitResponse.class);

        UnitResponse unitUpdated = unitClient.updateUnit(Utils.convertUnitToJson(unitRequestUpdated), unitResponse.getId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(UnitResponse.class);

        assertEquals(unitRequestUpdated.getName(), unitUpdated.getName());
        assertEquals(unitRequestUpdated.getPrice(), unitUpdated.getPrice());
        assertEquals(unitRequestUpdated.getLimitGuest(), unitUpdated.getLimitGuest());

        unitClient.deleteUnit(unitResponse.getId());
    }

    @Test
    @Story("Deve retornar erro padrão ao tentar atualizar unit")
    public void testMustReturnErrorWhenUpdatingUnitNameToOneAlreadyRegistered() {

        UnitRequest unitRequest = UnitFactory.novoUnit();
        UnitRequest unitRequest2 = UnitFactory.novoUnit();

        unitClient.cadastroUnit(Utils.convertUnitToJson(unitRequest))
                .then().extract().as(UnitResponse.class);

        UnitResponse unitResponse2 = unitClient.cadastroUnit(Utils.convertUnitToJson(unitRequest2))
                .then().extract().as(UnitResponse.class);

        unitClient.updateUnit(Utils.convertUnitToJson(unitRequest), unitResponse2.getId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString(UnitData.ROOM_NUMBER_ALREADY_EXIST));
    }

    @Test
    @Story("Deve retornar erro padrão ao tentar atualizar unit")
    public void testMustReturnErrorWhenUpdatingUnitLimitAndPriceBelowZero() {

        UnitRequest unitRequest = UnitFactory.novoUnit();

        UnitResponse unitResponse = unitClient.cadastroUnit(Utils.convertUnitToJson(unitRequest))
                .then().extract().as(UnitResponse.class);
        unitRequest.setPrice((double) Utils.faker.number().negative());
        unitRequest.setLimitGuest(Utils.faker.number().negative());

        unitClient.updateUnit(Utils.convertUnitToJson(unitRequest), unitResponse.getId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString(UnitData.UNIT_LIMIT_CANNOT_BE_UNDER_ZERO));
    }
}
