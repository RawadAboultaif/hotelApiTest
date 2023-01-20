package br.com.hotelaria.data.factory;

import br.com.hotelaria.dto.address.AddressRequest;
import br.com.hotelaria.utils.Utils;
import org.apache.commons.lang3.StringUtils;

public class AddressFactory {

    public static AddressRequest addressCompleto() {
        return criarAddress();
    }

    public static AddressRequest addressCampoComplementVazio() {
        AddressRequest addressRequestComplementVazio = criarAddress();
        addressRequestComplementVazio.setComplement(StringUtils.EMPTY);
        return addressRequestComplementVazio;
    }

    public static AddressRequest addressComCamposObrigatoriosVazios() {
        AddressRequest addressCamposObrigatoriosVazios = criarAddress();
        addressCamposObrigatoriosVazios.setStreetName(StringUtils.EMPTY);
        addressCamposObrigatoriosVazios.setNumber(StringUtils.EMPTY);
        addressCamposObrigatoriosVazios.setCity(StringUtils.EMPTY);
        addressCamposObrigatoriosVazios.setState(StringUtils.EMPTY);
        addressCamposObrigatoriosVazios.setZipcode(StringUtils.EMPTY);
        addressCamposObrigatoriosVazios.setCountry(StringUtils.EMPTY);
        return addressCamposObrigatoriosVazios;
    }

    private static AddressRequest criarAddress() {
        return AddressRequest.builder()
                .streetName(Utils.faker.address().streetName())
                .number(String.valueOf(Utils.faker.number().numberBetween(1, 9999)))
                .complement("ap104")
                .city(Utils.faker.address().city())
                .state(Utils.faker.address().state())
                .zipcode(Utils.faker.address().zipCode())
                .country(Utils.faker.address().country())
                .build();
    }
}
