package org.success.ipLooker;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationResponse {
    String ip_address;
    String country;
    String state;
    String city;
    String time_zone;
    String postal_code;
    Double latitude;
    Double longitude;
}
