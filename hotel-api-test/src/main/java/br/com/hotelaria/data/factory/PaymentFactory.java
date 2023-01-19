package br.com.hotelaria.data.factory;

import br.com.hotelaria.dto.payment.PaymentRequest;
import br.com.hotelaria.utils.Utils;

public class PaymentFactory {

    public static PaymentRequest novoPaymentVálido() {
        return criarPayment();
    }

    public static PaymentRequest novoPaymentNumeroCartaoInvalido() {
        PaymentRequest paymentNumeroCartaoInvalido = criarPayment();
        paymentNumeroCartaoInvalido.setCard(Utils.faker.number().digits(15));
        return paymentNumeroCartaoInvalido;
    }
    private static PaymentRequest criarPayment() {
        return PaymentRequest.builder()
                .method("Crédito")
                .card(Utils.faker.finance().creditCard().replaceAll("-", ""))
                .build();
    }
}
