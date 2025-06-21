package com.svincent7.sentraiam.identity.controller;

import com.svincent7.sentraiam.common.dto.permission.PermissionResponse;
import com.svincent7.sentraiam.identity.service.permission.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/identity/v1/permissions")
public class PermissionController {
    private final PermissionService permissionService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<PermissionResponse>> getUserVisiblePermissions() {
        return ResponseEntity.ok(permissionService.getPermissionsUserVisible());
    }

    @RequestMapping(path = "/get-all", method = RequestMethod.GET)
    public ResponseEntity<List<PermissionResponse>> getAll() {
        return ResponseEntity.ok(permissionService.getPermissions());
    }
}
