package scra.qnaboard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class QnaboardApplication {

    private final TestRepository testRepository;

    public static void main(String[] args) {
        SpringApplication.run(QnaboardApplication.class, args);
    }

    @PostConstruct
    public void init() {
        testRepository.save(new TestEntity("asdf"));
    }
}
