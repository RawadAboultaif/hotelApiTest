package br.com.hotelaria.data.factory;

import br.com.hotelaria.dto.guest.GuestRequest;
import br.com.hotelaria.utils.Utils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class GuestFactory {

    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static GuestRequest guestCompleto() {
        return criarGuest();
    }

    public static GuestRequest guestAtualizarNomeEmailPhone(GuestRequest guest) {
        guest.setName(Utils.faker.name().fullName());
        guest.setEmail(Utils.faker.internet().emailAddress());
        guest.setPhone(Utils.faker.phoneNumber().cellPhone());
        return guest;
    }

    public static GuestRequest guestComIdadeMenorQueDezoito() {
        GuestRequest guestIdadeMenorQueDezoito = criarGuest();
        guestIdadeMenorQueDezoito.setDateOfBirth(dateFormat.format(Utils.faker.date().birthday(0, 17)));
        return guestIdadeMenorQueDezoito;
    }

    public static GuestRequest guestCompletoComDataNascimentoNoFuturo() {
        GuestRequest guestRequestDataNascimentoFuturo = criarGuest();
        guestRequestDataNascimentoFuturo.setDateOfBirth(dateFormat.format(Utils.faker.date().future(1000, TimeUnit.DAYS)));
        return guestRequestDataNascimentoFuturo;
    }

    public static GuestRequest guestComTodosCamposVazios() {
        GuestRequest guestRequestVazio = criarGuest();
        guestRequestVazio.setName(StringUtils.EMPTY);
        guestRequestVazio.setSocialSecurityNumber(StringUtils.EMPTY);
        guestRequestVazio.setDateOfBirth(StringUtils.EMPTY);
        guestRequestVazio.setEmail(StringUtils.EMPTY);
        guestRequestVazio.setPhone(StringUtils.EMPTY);
        return guestRequestVazio;
    }

    private static GuestRequest criarGuest() {
        GuestRequest novoGuestRequest = GuestRequest.builder()
                .name(Utils.faker.name().fullName())
                .socialSecurityNumber(Utils.faker.cpf().valid(false))
                .dateOfBirth(dateFormat.format(Utils.faker.date().birthday(18, 100)))
                .email(Utils.faker.internet().emailAddress())
                .phone(Utils.faker.phoneNumber().cellPhone())
                .build();
        return novoGuestRequest;
    }
}
