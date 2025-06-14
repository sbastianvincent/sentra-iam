package com.svincent7.sentraiam.apigateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SentraIamApigatewayApplicationTests {

	@Autowired
	private SentraIamApigatewayApplication discoveryApplication;

	@Test
	public void contextLoads() {
		// Ensure that the application context loads successfully
		Assertions.assertNotNull(discoveryApplication);
	}

	@Test
	public void testApplicationStart() {
		discoveryApplication.main(new String[] {});
	}

}
