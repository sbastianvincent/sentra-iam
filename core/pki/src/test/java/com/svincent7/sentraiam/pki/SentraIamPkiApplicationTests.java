package com.svincent7.sentraiam.pki;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SentraIamPkiApplicationTests {

	@Autowired
	private SentraIamPkiApplication application;

	@Test
	public void contextLoads() {
		// Ensure that the application context loads successfully
		Assertions.assertNotNull(application);
	}

	@Test
	public void testApplicationStart() {
		application.main(new String[] {});
	}

}
