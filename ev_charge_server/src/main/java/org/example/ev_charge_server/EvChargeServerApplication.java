package org.example.ev_charge_server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("org.example.ev_charge_server.mapper")
@SpringBootApplication
public class EvChargeServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EvChargeServerApplication.class, args);
    }

}
