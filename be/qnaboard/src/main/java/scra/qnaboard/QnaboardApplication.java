package scra.qnaboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class QnaboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(QnaboardApplication.class, args);
    }
}
