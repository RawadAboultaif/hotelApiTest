package br.com.hotelaria.data.factory;

import br.com.hotelaria.dto.unit.UnitRequest;
import br.com.hotelaria.utils.Utils;

public class UnitFactory {

    public static UnitRequest novoUnit() {
        return criarUnit();
    }

    private static UnitRequest criarUnit() {
        return UnitRequest.builder()
                .name(Utils.faker.number().digits(11))
                .price(Double.valueOf(Utils.faker.number().numberBetween(500, 1000)))
                .limitGuest(Utils.faker.number().numberBetween(2, 4))
                .build();
    }
}
