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

@Epic("Busca Tests")
@Feature("Unit")
@DisplayName("Busca Unit")
public class GetTests extends BaseTest {

    UnitClient unitClient = new UnitClient();

    @Test
    @Story("Deve buscar unit com sucesso")
    public void testMustFindUnitByName() {

        UnitRequest unitRequest = UnitFactory.novoUnit();

        UnitResponse unitResponse = unitClient.cadastroUnit(Utils.convertUnitToJson(unitRequest))
                .then().extract().as(UnitResponse.class);

        UnitResponse unitBuscado = unitClient.buscarUnit(unitResponse.getName())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(UnitResponse.class);

        assertEquals(unitResponse.getId(), unitBuscado.getId());
        assertEquals(unitResponse.getName(), unitBuscado.getName());
        assertEquals(unitResponse.getLimitGuest(), unitBuscado.getLimitGuest());
        assertEquals(unitResponse.getPrice(), unitBuscado.getPrice());

        unitClient.deleteUnit(unitBuscado.getId());
    }

    @Test
    @Story("Deve retornar erro padrão ao buscar unit")
    public void testMustReturnErrorWhenSearchingUnitNameNonExistent() {

        UnitRequest unitRequest = UnitFactory.novoUnit();

        UnitResponse unitResponse = unitClient.cadastroUnit(Utils.convertUnitToJson(unitRequest))
                .then().extract().as(UnitResponse.class);

        unitClient.deleteUnit(unitResponse.getId());

        unitClient.buscarUnit(unitResponse.getName())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString(UnitData.ROOM_NAME_DONT_EXIST));
    }
}
