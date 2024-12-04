package org.success.payment.stripe.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PortalRedirect {
    private String url;
}
