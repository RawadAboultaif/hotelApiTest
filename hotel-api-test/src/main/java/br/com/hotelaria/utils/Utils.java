package br.com.hotelaria.utils;


import br.com.hotelaria.dto.address.AddressRequest;
import br.com.hotelaria.dto.employee.EmployeeRequest;
import br.com.hotelaria.dto.guest.GuestRequest;
import br.com.hotelaria.dto.payment.PaymentRequest;
import br.com.hotelaria.dto.unit.UnitRequest;
import com.google.gson.Gson;
import net.datafaker.Faker;

import org.junit.Assert;

import java.util.Locale;

public class Utils {

    public static Faker faker = new Faker(new Locale("pt-BR"));

    public enum Env {
        DEV, PRD
    }

    public static String getBaseUrl() {
        String baseUrl = null;
        Env env = envStringToEnum();

        switch (env) {
            case DEV -> {
                baseUrl = "http://localhost:8080/api/v1/";
            }
            case PRD -> {
                baseUrl = "Não esta em produção.";
            }
            default -> {
                Assert.fail("Ambiente não configurado");
            }
        }
        return baseUrl;
    }

    public static Env envStringToEnum() {
        try {
            return Env.valueOf(getEnv());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Ambiente não existente: " + getEnv());
        }
    }

    public static String getEnv() {
        return Manipulation.getProp().getProperty("prop.env");
    }
    public static String convertGuestToJson(GuestRequest guestRequest) { return new Gson().toJson(guestRequest);}
    public static String convertEmployeeToJson(EmployeeRequest employeeRequest) { return new Gson().toJson(employeeRequest);}
    public static String convertPaymentToJson(PaymentRequest paymentRequest) { return new Gson().toJson(paymentRequest);}
    public static String convertAddressToJson(AddressRequest addressRequest) { return new Gson().toJson(addressRequest);}
    public static String convertUnitToJson(UnitRequest unitRequest) { return new Gson().toJson(unitRequest);}
}
