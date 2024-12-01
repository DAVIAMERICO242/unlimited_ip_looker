package org.success.ipLooker;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationResponse {
    String ip_adress;
    String continent_code;
    String country_code;
    String region_code;
    String city;
    String time_zone;
    String postal_code;
    Double latitude;
    Double longitude;
}
