package project.school.socialmedia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.kafka.annotation.EnableKafka;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
@ComponentScan(basePackages = "project.school.socialmedia")
@EnableKafka
public class CommunicatorApplication {
  public static void main(String[] args) {
    SpringApplication.run(CommunicatorApplication.class, args);
  }
}




