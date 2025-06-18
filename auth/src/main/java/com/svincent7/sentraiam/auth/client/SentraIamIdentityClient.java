package com.svincent7.sentraiam.auth.client;

import com.svincent7.sentraiam.common.client.IdentityClient;
import com.svincent7.sentraiam.common.dto.auth.LoginRequest;
import com.svincent7.sentraiam.common.dto.credential.VerifyCredentialResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "sentra-iam-identity")
public interface SentraIamIdentityClient extends IdentityClient {

    @RequestMapping(value = "/api/identity/v1/credentials/verify", method = RequestMethod.POST)
    ResponseEntity<VerifyCredentialResponse> verifyCredentials(@RequestHeader("Authorization") String authorization,
                                                               @RequestBody LoginRequest loginRequest);
}
