package br.com.hotelaria.tests.guest;

import br.com.hotelaria.client.GuestClient;
import br.com.hotelaria.data.changeless.ValuesData;
import br.com.hotelaria.data.factory.GuestFactory;
import br.com.hotelaria.dto.guest.GuestRequest;
import br.com.hotelaria.dto.guest.GuestResponse;
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
@Feature("Guest")
@DisplayName("Delete Guest")
public class DeleteTests extends BaseTest {

    GuestClient guestClient = new GuestClient();

    @Test
    @Story("Deve retornar erro ao deletar guest")
    public void testMustReturnErrorWhenDeletingNonExistentClient() {

        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();

        GuestResponse guestResponse = guestClient.cadastrarGuest(Utils.convertGuestToJson(novoGuestRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().as(GuestResponse.class);

        guestClient.deletarGuest(guestResponse.getId());
        guestClient.deletarGuest(guestResponse.getId())
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString(ValuesData.ID_DONT_EXIST));
    }
}
