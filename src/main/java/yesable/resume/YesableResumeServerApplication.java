package yesable.resume;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class YesableResumeServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(YesableResumeServerApplication.class, args);
    }

}
