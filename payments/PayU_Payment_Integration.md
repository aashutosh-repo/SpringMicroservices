# 💳 PayU Payment Integration (Spring Boot + Angular)

This document provides a step-by-step guide for integrating **PayU Payments** using the **Hosted Checkout Flow** in a Spring Boot backend and Angular frontend.

---

## 🚀 Overview

Our integration uses **PayU’s hosted payment page**, ensuring secure handling of user card, UPI, and net banking details.  
The process involves generating a **SHA-512 hash** for every payment request, redirecting the user to PayU, and validating the callback response.

---

## ⚙️ Workflow Summary

### 🔄 Flow Diagram
```
Client (Angular) → Backend (Spring Boot) → PayU Gateway → Backend (Callback) → Client (Status)
```

### 🧭 Flow Steps
1. User initiates payment in Angular UI.  
2. Backend creates a **payment form** with hash and returns HTML.  
3. User is redirected automatically to PayU’s payment page.  
4. PayU processes the payment and redirects to:  
   - `surl` → on success  
   - `furl` → on failure  
5. Backend verifies response hash and updates payment status.  
6. Client receives the final transaction status.

---

## 🧩 Required Parameters

| Field | Description | Example |
|-------|--------------|----------|
| **key** | Merchant key from PayU | `gtKFFx` |
| **txnid** | Unique transaction ID | `ORD12345` |
| **amount** | Payment amount (two decimal places) | `1200.00` |
| **productinfo** | Description of product/service | `Flight Ticket` |
| **firstname** | Customer name | `Aashutosh` |
| **email** | Customer email | `ashu@example.com` |
| **phone** | Customer mobile | `9876543210` |
| **surl** | Success callback URL | `https://yourapp.com/payments/payu/success` |
| **furl** | Failure callback URL | `https://yourapp.com/payments/payu/failure` |
| **hash** | SHA-512 security hash | Generated on server |

---

## 🔐 Hash Generation

### ✅ Sequence
```
key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5||||||SALT
```

### 🧠 Example
```
sha512(merchantKey|ORD12345|100.00|MyProduct|Aashutosh|test@ashu.com|||||||||||merchantSalt)
```

### 🧩 Java Implementation
```java
private String hashSha512(String input) throws Exception {
    MessageDigest md = MessageDigest.getInstance("SHA-512");
    byte[] messageDigest = md.digest(input.getBytes(StandardCharsets.UTF_8));
    StringBuilder sb = new StringBuilder();
    for (byte b : messageDigest) {
        sb.append(String.format("%02x", b));
    }
    return sb.toString();
}
```

---

## 🧱 Backend: Spring Boot Implementation

### 🏗️ Payment Form Builder
```java
public String buildHostedCheckoutForm(PayURequest request) throws Exception {
    String txnid = request.getOrderId();
    String productinfo = "MyProduct";
    String surl = "http://localhost:8085/payments/payu/success";
    String furl = "http://localhost:8085/payments/payu/failure";

    String hashString = merchantKey + "|" + txnid + "|" + request.getAmount() + "|" + productinfo + "|" +
            request.getFirstname() + "|" + request.getEmail() + "|||||||||||" + merchantSalt;

    String hash = hashSha512(hashString);

    StringBuilder html = new StringBuilder();
    html.append("<html><body onload='document.forms["payu_form"].submit()'>");
    html.append("<form id='payu_form' method='post' action='")
        .append(baseUrl).append("/_payment'>");

    Map<String, String> params = Map.of(
        "key", merchantKey,
        "txnid", txnid,
        "amount", request.getAmount(),
        "productinfo", productinfo,
        "firstname", request.getFirstname(),
        "email", request.getEmail(),
        "phone", request.getPhone(),
        "surl", surl,
        "furl", furl,
        "hash", hash
    );

    for (var entry : params.entrySet()) {
        html.append("<input type='hidden' name='")
            .append(entry.getKey()).append("' value='")
            .append(entry.getValue()).append("'/>");
    }

    html.append("</form><p>Redirecting to PayU...</p></body></html>");
    return html.toString();
}
```

---

## 🖥️ Frontend: Angular Service Example

```typescript
goToPayU(orderId: string, amount: string, firstname: string, email: string, phone: string) {
  const body = {
    orderId,
    amount,
    firstname,
    email,
    phone
  };

  return this.http.post('http://localhost:8085/payments/payu/hostedRedirect', body, {
    responseType: 'text'
  });
}
```

In your component:
```typescript
this.paymentService.goToPayU(orderId, amount, name, email, phone)
  .subscribe(html => {
    const newTab = window.open('', '_self');
    newTab.document.write(html);
    newTab.document.close();
  });
```

---

## 🔁 Callback Handling

### ✅ PayU will send POST data to:
- `surl` → On success
- `furl` → On failure

### 🧾 Response Fields:
| Field | Description |
|-------|--------------|
| `status` | Payment status (success/failure) |
| `txnid` | Transaction ID |
| `amount` | Payment amount |
| `hash` | Response hash |
| `email` | Customer email |
| `productinfo` | Product info |
| `mihpayid` | PayU transaction ID |

### 🛡️ Validate Response Hash
```java
String reverseHashSequence = merchantSalt + "|" + status + "|||||||||||" +
    email + "|" + firstname + "|" + productinfo + "|" +
    amount + "|" + txnid + "|" + merchantKey;

String expectedHash = hashSha512(reverseHashSequence);

if (!expectedHash.equalsIgnoreCase(receivedHash)) {
    throw new SecurityException("Invalid Hash: Possible tampering detected");
}
```

---

## 🌐 Environment Configuration

| Environment | URL | Description |
|--------------|------|-------------|
| Sandbox | `https://test.payu.in` | Testing/Development |
| Production | `https://secure.payu.in` | Live Transactions |

---

## 🧪 Sandbox Testing Credentials

| Field | Value |
|--------|--------|
| Merchant Key | `gtKFFx` |
| Salt | `eCwWELxi` |
| PayU URL | `https://test.payu.in/_payment` |

You can use any of PayU’s test cards for transaction simulation.

---

## ⚠️ Common Issues

| Issue | Cause | Resolution |
|--------|--------|-------------|
| `Hash mismatch` | Incorrect sequence or missing fields | Ensure correct format and salt |
| `Invalid Key` | Wrong credentials | Use valid Merchant Key/Salt |
| `Payment page not loading` | Wrong PayU endpoint | Use `/test` or `/secure` URLs |
| `Tampered response` | Skipped hash verification | Always verify callback hash |

---

## 🔒 Security Best Practices
- Never generate hash on the frontend.
- Store merchant key/salt in environment variables.
- Always use HTTPS for callbacks (`surl`, `furl`).
- Log and verify all callback requests.
- Validate the hash before updating transaction status.

---

## 📁 API Endpoints Summary

| Endpoint | Method | Description |
|-----------|--------|-------------|
| `/payments/payu/hostedRedirect` | `POST` | Initiate PayU transaction |
| `/payments/payu/success` | `POST` | PayU success callback |
| `/payments/payu/failure` | `POST` | PayU failure callback |

---

## ✅ Conclusion

This integration ensures:
- Secure payment redirection  
- Hash-based data integrity  
- Flexible support for UPI, Cards, and NetBanking  

You can now safely collect payments through PayU with minimal PCI compliance effort.

---

**Author:** `Aashutosh Kumar`  
**Tech Stack:** Spring Boot · Angular · PayU Hosted Checkout  
**Version:** `v1.0.0`
