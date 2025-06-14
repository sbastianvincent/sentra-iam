package com.svincent7.sentraiam.discovery;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
class SentraIamDiscoveryApplicationTests {

	@Autowired
	private SentraIamDiscoveryApplication discoveryApplication;

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
