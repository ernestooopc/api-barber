package com.barber.v1.Service;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;


import jakarta.annotation.PostConstruct;

@Service
public class StripeService {

    @Value("${stripe.secret.key}")
    private String stripeKey;


    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeKey;
    }

    public PaymentIntent crearIntentoPago(long monto, String moneda) throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", monto); // en centavos. 1000 = S/.10.00
        params.put("currency", moneda); // "pen" para soles peruanos
        params.put("automatic_payment_methods", Map.of("enabled", true));
        return PaymentIntent.create(params);
    }

    public String obtenerReceiptUrl(String paymentIntentId) throws StripeException {
    PaymentIntent intent = PaymentIntent.retrieve(paymentIntentId);
    String chargeId = intent.getLatestCharge();

    if (chargeId == null) {
        throw new IllegalStateException("El PaymentIntent aún no tiene un cargo asociado.");
    }

    Charge charge = Charge.retrieve(chargeId);
    return charge.getReceiptUrl();
}

}