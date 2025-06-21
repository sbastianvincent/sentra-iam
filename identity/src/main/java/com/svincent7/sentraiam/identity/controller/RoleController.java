package com.svincent7.sentraiam.identity.controller;

import com.svincent7.sentraiam.common.controller.BaseController;
import com.svincent7.sentraiam.common.dto.permission.PermissionResponse;
import com.svincent7.sentraiam.common.dto.role.ModifyPermissionRequest;
import com.svincent7.sentraiam.common.dto.role.RoleRequest;
import com.svincent7.sentraiam.common.dto.role.RoleResponse;
import com.svincent7.sentraiam.identity.model.RoleEntity;
import com.svincent7.sentraiam.identity.service.role.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/identity/v1/roles")
public class RoleController extends BaseController<RoleEntity, RoleRequest, RoleResponse, String> {
    private final RoleService roleService;

    @Override
    protected RoleService getService() {
        return roleService;
    }

    @RequestMapping(path = "/{roleId}/permissions", method = RequestMethod.GET)
    public ResponseEntity<List<PermissionResponse>> getPermissionsByRoleId(final @PathVariable String roleId) {
        return ResponseEntity.ok(roleService.getPermissionsByRoleId(roleId));
    }

    @RequestMapping(path = "/{roleId}/permissions", method = RequestMethod.POST)
    public ResponseEntity<List<PermissionResponse>> addPermissionToRoleId(final @PathVariable String roleId,
              @Valid final @RequestBody ModifyPermissionRequest modifyPermissionRequest) {
        return ResponseEntity.ok(roleService.addPermissionToRoleId(roleId, modifyPermissionRequest));
    }

    @RequestMapping(path = "/{roleId}/permissions", method = RequestMethod.DELETE)
    public ResponseEntity<List<PermissionResponse>> deletePermissionFromRoleId(final @PathVariable String roleId,
              @Valid final @RequestBody ModifyPermissionRequest modifyPermissionRequest) {
        return ResponseEntity.ok(roleService.deletePermissionFromRoleId(roleId, modifyPermissionRequest));
    }
}
