package project.school.socialmedia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
@ComponentScan(basePackages = "project.school.socialmedia")
public class SocialProfileApplication {
  public static void main(String[] args) {
    SpringApplication.run(SocialProfileApplication.class, args);
  }
}