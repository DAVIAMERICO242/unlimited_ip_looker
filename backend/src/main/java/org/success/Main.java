package org.success;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
@SpringBootApplication
@EnableAsync
public class Main {
    public static void main(String[] args) throws IOException, GeoIp2Exception {
        SpringApplication.run(Main.class, args);
    }

//    @Bean
//    public OpenEntityManagerInViewFilter openEntityManagerInViewFilter(){
//        return new OpenEntityManagerInViewFilter();
//    }
}
