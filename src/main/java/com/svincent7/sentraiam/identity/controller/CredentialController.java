package com.svincent7.sentraiam.identity.controller;

import com.svincent7.sentraiam.common.controller.BaseController;
import com.svincent7.sentraiam.identity.dto.auth.LoginRequest;
import com.svincent7.sentraiam.identity.dto.credential.CredentialRequest;
import com.svincent7.sentraiam.identity.dto.credential.CredentialResponse;
import com.svincent7.sentraiam.identity.dto.credential.VerifyCredentialResponse;
import com.svincent7.sentraiam.identity.model.CredentialEntity;
import com.svincent7.sentraiam.identity.service.credential.CredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/identity/v1/credentials")
public class CredentialController extends BaseController<CredentialEntity, CredentialRequest, CredentialResponse,
        String> {
    private final CredentialService credentialService;

    @Override
    protected CredentialService getService() {
        return credentialService;
    }

    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    public ResponseEntity<VerifyCredentialResponse> verifyCredentials(final @RequestBody LoginRequest loginRequest) {
        VerifyCredentialResponse response = credentialService.verifyCredentials(loginRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    protected void verifyGetAll() {
        returnURINotFound();
    }
}
