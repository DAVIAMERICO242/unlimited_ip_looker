package org.success.payment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckoutRedirect {
    private String url;
}
