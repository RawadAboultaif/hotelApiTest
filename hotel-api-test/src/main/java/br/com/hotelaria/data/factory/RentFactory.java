package br.com.hotelaria.data.factory;

import br.com.hotelaria.dto.rent.RentRequest;
import br.com.hotelaria.utils.Utils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class RentFactory {

    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    static DateTimeFormatter dateFormatNow = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static RentRequest novoRent(){
        return criarRent();
    }

    public static RentRequest rentEmptyCamposVazios() {
        RentRequest rentRequest = criarRent();
        rentRequest.setCheckIn(StringUtils.EMPTY);
        rentRequest.setCheckOut(StringUtils.EMPTY);
        return rentRequest;
    }
    public static RentRequest rentWithCheckInAfterCheckOut() {
        RentRequest rentRequest = criarRent();
        rentRequest.setCheckIn(dateFormat.format(Utils.faker.date().future(1000, TimeUnit.DAYS)));
        rentRequest.setCheckOut(LocalDate.now().format(dateFormatNow));
        return rentRequest;
    }

    private static RentRequest criarRent() {
        return RentRequest.builder()
                .checkIn(LocalDate.now().format(dateFormatNow))
                .checkOut(dateFormat.format(Utils.faker.date().future(1000, TimeUnit.DAYS)))
                .build();
    }
}
