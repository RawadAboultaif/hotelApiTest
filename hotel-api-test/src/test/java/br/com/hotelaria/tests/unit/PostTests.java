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
import static org.junit.jupiter.api.Assertions.*;

@Epic("Cadastro Tests")
@Feature("Unit")
@DisplayName("Cadastro Unit")
public class PostTests extends BaseTest {

    UnitClient unitClient = new UnitClient();

    @Test
    @Story("Deve cadastrar unit com sucesso")
    public void testMustSaveUnit() {

        UnitRequest unitRequest = UnitFactory.novoUnit();

        UnitResponse unitResponse = unitClient.cadastroUnit(Utils.convertUnitToJson(unitRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().as(UnitResponse.class);

        assertEquals(unitRequest.getName(), unitResponse.getName());
        assertEquals(unitRequest.getPrice(), unitResponse.getPrice());
        assertEquals(unitRequest.getLimitGuest(), unitResponse.getLimitGuest());
        assertTrue(unitResponse.getStatus().equals(UnitData.UNIT_EMPTY));

        unitClient.deleteUnit(unitResponse.getId());
    }

    @Test
    @Story("Deve retornar erro padrão ao tentar cadastrar unit")
    public void testMustReturnErrorWhenSavingUnitWithNegativePrice() {

        UnitRequest unitRequest = UnitFactory.novoUnit();
        unitRequest.setPrice(-500d);

        unitClient.cadastroUnit(Utils.convertUnitToJson(unitRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString(UnitData.UNIT_LIMIT_CANNOT_BE_UNDER_ZERO));
    }

    @Test
    @Story("Deve retornar erro padrão ao tentar cadastrar unit")
    public void testMustReturnErrorWhenSavingUnitWithLimitGuestEqualToZero() {

        UnitRequest unitRequest = UnitFactory.novoUnit();
        unitRequest.setLimitGuest(0);

        unitClient.cadastroUnit(Utils.convertUnitToJson(unitRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString(UnitData.UNIT_LIMIT_CANNOT_BE_UNDER_ZERO));
    }

    @Test
    @Story("Deve retornar erro padrão ao tentar cadastrar unit")
    public void testMustReturnErrorWhenSavingUnitWithUnitNameAlreadyRegistered() {

        UnitRequest unitRequest = UnitFactory.novoUnit();
        UnitRequest unitRequest2 = UnitFactory.novoUnit();

        UnitResponse unitResponse = unitClient.cadastroUnit(Utils.convertUnitToJson(unitRequest))
                .then().extract().as(UnitResponse.class);

        unitClient.cadastroUnit(Utils.convertUnitToJson(unitRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString(UnitData.ROOM_NUMBER_ALREADY_EXIST));

        unitClient.deleteUnit(unitResponse.getId());
    }
}
