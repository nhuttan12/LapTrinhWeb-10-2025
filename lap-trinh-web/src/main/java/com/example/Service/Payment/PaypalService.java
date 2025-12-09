package com.example.Service.Payment;

import com.paypal.orders.*;

import java.util.List;

public class PaypalService {

    public PaypalCreateResult createOrder(int orderId, double amount, String returnUrl, String cancelUrl) throws Exception {

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");

        ApplicationContext appContext = new ApplicationContext()
                .brandName("Lap Trinh Web Shop")
                .landingPage("BILLING")
                .returnUrl(returnUrl)
                .cancelUrl(cancelUrl);

        AmountWithBreakdown amountObj = new AmountWithBreakdown()
                .currencyCode("USD")
                .value(String.format("%.2f", amount));

        PurchaseUnitRequest unit = new PurchaseUnitRequest()
                .customId(String.valueOf(orderId))   // vẫn gửi customId nhưng PAYPAL sẽ KHÔNG TRẢ LẠI
                .amountWithBreakdown(amountObj);

        orderRequest.purchaseUnits(List.of(unit));
        orderRequest.applicationContext(appContext);

        OrdersCreateRequest request = new OrdersCreateRequest();
        request.header("prefer", "return=representation");
        request.requestBody(orderRequest);

        var response = PaypalClient.client.execute(request);

        String paypalOrderId = response.result().id();
        String approvalUrl = response.result().links().stream()
                .filter(l -> l.rel().equals("approve"))
                .findFirst()
                .get()
                .href();

        return new PaypalCreateResult(paypalOrderId, approvalUrl);
    }

}
