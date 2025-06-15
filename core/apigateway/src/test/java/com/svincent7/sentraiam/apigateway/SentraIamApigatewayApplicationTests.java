package com.svincent7.sentraiam.apigateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SentraIamApigatewayApplicationTests {

	@Autowired
	private SentraIamApigatewayApplication apigatewayApplication;

	@Test
	public void contextLoads() {
		// Ensure that the application context loads successfully
		Assertions.assertNotNull(apigatewayApplication);
	}

	@Test
	public void testApplicationStart() {
		apigatewayApplication.main(new String[] {});
	}

}
