package br.com.hotelaria.tests.address;

import br.com.hotelaria.client.AddressClient;
import br.com.hotelaria.client.GuestClient;
import br.com.hotelaria.data.changeless.ValuesData;
import br.com.hotelaria.data.factory.AddressFactory;
import br.com.hotelaria.data.factory.GuestFactory;
import br.com.hotelaria.dto.address.AddressRequest;
import br.com.hotelaria.dto.address.AddressResponse;
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
@Feature("Address")
@DisplayName("Delete Address")
public class DeleteTests extends BaseTest {

    GuestClient guestClient = new GuestClient();
    AddressClient addressClient = new AddressClient();

    @Test
    @Story("Deve retornar erro ao deletar address")
    public void testMustReturnErrorWhenDeletingAddressWithIdNonExistent() {

        GuestRequest novoGuestRequest = GuestFactory.guestCompleto();
        AddressRequest novoAddressRequest = AddressFactory.addressCompleto();

        GuestResponse guestResponse = guestClient.cadastrarGuest(Utils.convertGuestToJson(novoGuestRequest))
                .then().extract().as(GuestResponse.class);

        AddressResponse addressResponse = addressClient.cadastrarAddressIdGuest(Utils.convertAddressToJson(novoAddressRequest), guestResponse.getId())
                .then().extract().as(AddressResponse.class);

        addressClient.deletarAddress(addressResponse.getId());
        addressClient.deletarAddress(addressResponse.getId())
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString(ValuesData.ID_DONT_EXIST));

        guestClient.deletarGuest(guestResponse.getId());
    }
}
