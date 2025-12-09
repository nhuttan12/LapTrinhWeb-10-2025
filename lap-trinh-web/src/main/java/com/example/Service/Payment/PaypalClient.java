package com.example.Service.Payment;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;

public class PaypalClient {

    public static final String CLIENT_ID = "Ac-QhnSifBz3j1GEQTeFJJEgFo8y05fQ6R-ZF7lGR8DcUSl3iRRaDZmTOi0LnsbuECjAkPA0bS04orlO";
    public static final String CLIENT_SECRET = "EPrClqoxd3Zqc9NfpWkKUTET4qj7bMrGbr8hXW9A4who8fUs6-bbvSRb0Irj8ZcFB-evNxNh2L7sPmtB";

    public static final PayPalHttpClient client =
            new PayPalHttpClient(
                    new PayPalEnvironment.Sandbox(CLIENT_ID, CLIENT_SECRET)
            );
}
