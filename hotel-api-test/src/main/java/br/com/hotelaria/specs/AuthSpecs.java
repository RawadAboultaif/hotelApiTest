package br.com.hotelaria.specs;


import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class AuthSpecs {


    private AuthSpecs() {}

    public static RequestSpecification requestSpec() {
        return new RequestSpecBuilder().
                setContentType(ContentType.JSON).
                build();
    }
}
