package org.success.ipLooker;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationResponse {
    String ip_adress;
    String country_code;
    String state;
    String city;
    String time_zone;
    String postal_code;
    Double latitude;
    Double longitude;
}
