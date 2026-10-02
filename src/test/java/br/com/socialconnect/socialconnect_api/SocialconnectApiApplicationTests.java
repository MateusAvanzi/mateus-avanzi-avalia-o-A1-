package br.com.socialconnect.socialconnect_api;

import br.com.socialconnect.api.ApiApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ApiApplication.class)
@ActiveProfiles("test")
class SocialconnectApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
