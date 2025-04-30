package tn.esprit.mscompte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
public class MsCompteApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsCompteApplication.class, args);
    }

    }

