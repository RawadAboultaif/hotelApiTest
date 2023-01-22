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

@Epic("Delete Tests")
@Feature("Unit")
@DisplayName("Delete Unit")
public class DeleteTests extends BaseTest {

    UnitClient unitClient = new UnitClient();

    @Test
    @Story("Deve retornar erro ao deletar unit")
    public void testMustReturnErrorWhenDeletingUnitNonExistent() {

        UnitRequest unitRequest = UnitFactory.novoUnit();

        UnitResponse unitResponse = unitClient.cadastroUnit(Utils.convertUnitToJson(unitRequest))
                .then().extract().as(UnitResponse.class);

        unitClient.deleteUnit(unitResponse.getId());
        unitClient.deleteUnit(unitResponse.getId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString(UnitData.ROOM_ID_DONT_EXIST));
    }
}
