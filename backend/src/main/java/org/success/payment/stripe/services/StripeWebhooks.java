package org.success.payment.stripe.services;

import com.stripe.model.Charge;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import org.springframework.stereotype.Service;
import org.success.payment.stripe.StripeContext;

@Service
public class StripeWebhooks extends StripeContext {

    public void processWebhook(Event event){
        if(event.getType().equals("charge.succeeded")){
            StripeObject object = this.getAbstractStripeObject(event);
            Charge charge = (Charge) object;
            System.out.println("charge");
        }
    }

    private StripeObject getAbstractStripeObject(Event event){
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        return dataObjectDeserializer.getObject().get();
    }
}
