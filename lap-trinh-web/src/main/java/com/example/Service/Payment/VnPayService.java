package com.example.Service.Payment;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
public class VnPayService {
    // Sandbox config
    public static final String vnp_TmnCode = "Y8L4VYQG";
    public static final String vnp_HashSecret = "EIE60UT6MU9BVHO36ITDNPEXL7RGBCDC";
    public static final String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";

    public String generatePaymentUrl(int orderId, long amount, String returnUrl, String ipAddress) throws Exception {

        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", vnp_TmnCode);
        params.put("vnp_Amount", String.valueOf(amount * 100)); // VNPAY yêu cầu *100
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", String.valueOf(orderId));
        params.put("vnp_OrderInfo", "Pay order " + orderId);
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale", "vn");
        params.put("vnp_IpAddr", ipAddress);
        params.put("vnp_ReturnUrl", returnUrl);

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        params.put("vnp_CreateDate", String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", cal));
        System.out.println("vnp_CreateDate = " + params.get("vnp_CreateDate"));
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        System.out.println("Current VN Time: " + sdf.format(now));


        // Sort parameters alphabetically
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);

        // Build query + hash data
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String key : keys) {
            String value = params.get(key);
            if (value != null && !value.isEmpty()) {
                hashData.append(key).append("=").append(URLEncoder.encode(value, StandardCharsets.UTF_8));
                hashData.append("&");

                query.append(URLEncoder.encode(key, StandardCharsets.UTF_8))
                        .append("=")
                        .append(URLEncoder.encode(value, StandardCharsets.UTF_8));
                query.append("&");
            }
        }

        // Remove last "&"
        hashData.setLength(hashData.length() - 1);
        query.setLength(query.length() - 1);

        String secureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
        query.append("&vnp_SecureHash=").append(secureHash);

        return vnp_PayUrl + "?" + query;
    }

    private String hmacSHA512(String key, String data) throws Exception {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA512");
        javax.crypto.spec.SecretKeySpec secretKeySpec =
                new javax.crypto.spec.SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        mac.init(secretKeySpec);
        byte[] digest = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder(2 * digest.length);
        for (byte b : digest) sb.append(String.format("%02x", b & 0xff));
        return sb.toString();
    }
}
