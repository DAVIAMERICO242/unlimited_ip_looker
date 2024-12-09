package org.success.payment.paypal.DTOs;

import lombok.Data;
import org.success.payment.paypal.enums.WebhookVerificationStatus;

@Data
public class WebhookSignatureResponse{
    private WebhookVerificationStatus verification_status;
}
