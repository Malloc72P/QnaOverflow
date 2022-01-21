package scra.qnaboard;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class QnaboardApplicationTests {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.scope}")
    private String scope;

    @Test
    @DisplayName("oauth 설정파일을 불러올 수 있어야 함")
    void loadOauthProperties() {
        assertThat(clientId).isNotNull().isNotEqualTo("");
        assertThat(clientSecret).isNotNull().isNotEqualTo("");
        assertThat(scope).isNotNull().isNotEqualTo("");
    }
}
