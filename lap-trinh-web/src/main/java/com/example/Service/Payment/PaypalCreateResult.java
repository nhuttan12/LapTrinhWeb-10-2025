package com.example.Service.Payment;

public class PaypalCreateResult {
    private final String paypalOrderId;
    private final String approvalUrl;

    public PaypalCreateResult(String paypalOrderId, String approvalUrl) {
        this.paypalOrderId = paypalOrderId;
        this.approvalUrl = approvalUrl;
    }

    public String getPaypalOrderId() {
        return paypalOrderId;
    }

    public String getApprovalUrl() {
        return approvalUrl;
    }
}
