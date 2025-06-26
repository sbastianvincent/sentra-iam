package com.svincent7.sentraiam.identity.controller;

import com.svincent7.sentraiam.common.controller.BaseController;
import com.svincent7.sentraiam.common.dto.role.RoleResponse;
import com.svincent7.sentraiam.common.dto.role.RoleWithPermissions;
import com.svincent7.sentraiam.common.dto.user.ModifyRoleRequest;
import com.svincent7.sentraiam.common.dto.user.UserRequest;
import com.svincent7.sentraiam.common.dto.user.UserResponse;
import com.svincent7.sentraiam.identity.model.UserEntity;
import com.svincent7.sentraiam.identity.service.user.UserService;
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
@RequestMapping("/api/identity/v1/users")
public class UserController extends BaseController<UserEntity, UserRequest, UserResponse, String> {
    private final UserService userService;

    @Override
    protected UserService getService() {
        return userService;
    }

    @RequestMapping(path = "/{userId}/roles", method = RequestMethod.GET)
    public ResponseEntity<List<RoleResponse>> getRoleByUserId(final @PathVariable String userId) {
        return ResponseEntity.ok(userService.getRoleByUserId(userId));
    }

    @RequestMapping(path = "/{userId}/roles", method = RequestMethod.POST)
    public ResponseEntity<List<RoleResponse>> addRoleToUserId(final @PathVariable String userId,
            @Valid final @RequestBody ModifyRoleRequest modifyRoleRequest) {
        return ResponseEntity.ok(userService.addRoleToUserId(userId, modifyRoleRequest));
    }

    @RequestMapping(path = "/{userId}/roles", method = RequestMethod.DELETE)
    public ResponseEntity<List<RoleResponse>> deleteRoleFromUserId(final @PathVariable String userId,
            @Valid final @RequestBody ModifyRoleRequest modifyRoleRequest) {
        return ResponseEntity.ok(userService.deleteRoleFromUserId(userId, modifyRoleRequest));
    }

    @RequestMapping(path = "/{userId}/roles-permissions", method = RequestMethod.GET)
    public ResponseEntity<List<RoleWithPermissions>> getRoleWithPermissionsByUserId(final @PathVariable String userId) {
        return ResponseEntity.ok(userService.getRoleWithPermissionsByUserId(userId));
    }
}
