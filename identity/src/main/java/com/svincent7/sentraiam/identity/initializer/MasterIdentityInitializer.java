package com.svincent7.sentraiam.identity.initializer;

import com.svincent7.sentraiam.common.dto.credential.CredentialRequest;
import com.svincent7.sentraiam.common.dto.credential.CredentialType;
import com.svincent7.sentraiam.common.dto.role.ModifyPermissionRequest;
import com.svincent7.sentraiam.common.dto.role.RoleRequest;
import com.svincent7.sentraiam.common.dto.role.RoleResponse;
import com.svincent7.sentraiam.common.dto.tenant.TenantStatus;
import com.svincent7.sentraiam.common.dto.user.UserRequest;
import com.svincent7.sentraiam.common.dto.user.UserResponse;
import com.svincent7.sentraiam.common.exception.ResourceNotFoundException;
import com.svincent7.sentraiam.common.initializer.Initializer;
import com.svincent7.sentraiam.common.permission.Permission;
import com.svincent7.sentraiam.identity.config.IdentityConfig;
import com.svincent7.sentraiam.identity.model.PermissionEntity;
import com.svincent7.sentraiam.identity.model.TenantEntity;
import com.svincent7.sentraiam.identity.service.credential.CredentialService;
import com.svincent7.sentraiam.identity.service.permission.PermissionService;
import com.svincent7.sentraiam.identity.service.role.RoleService;
import com.svincent7.sentraiam.identity.service.tenant.TenantService;
import com.svincent7.sentraiam.identity.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MasterIdentityInitializer implements Initializer {
    private final IdentityConfig identityConfig;
    private final TenantService tenantService;
    private final UserService userService;
    private final CredentialService credentialService;
    private final RoleService roleService;
    private final PermissionService permissionService;

    @Override
    public void initialize() {
        try {
             tenantService.getTenantByName(identityConfig.getMasterTenantName());
        } catch (ResourceNotFoundException exception) {
            TenantEntity tenant = new TenantEntity();
            tenant.setId(identityConfig.getMasterTenantId());
            tenant.setTenantName(identityConfig.getMasterTenantName());
            tenant.setTenantStatus(TenantStatus.ACTIVE);
            tenantService.saveTenant(tenant);
        }

        UserResponse userResponse;
        try {
            userResponse = userService.getByUsernameAndTenantId(identityConfig.getMasterUserUsername(),
                    identityConfig.getMasterTenantId());
        } catch (ResourceNotFoundException exception) {
            UserRequest userRequest = new UserRequest();
            userRequest.setUsername(identityConfig.getMasterUserUsername());
            userRequest.setTenantId(identityConfig.getMasterTenantId());
            userRequest.setEnabled(true);
            userResponse = userService.create(userRequest);
        }

        try {
            credentialService.findByUserIdAndIdentifierAndType(
                    userResponse.getId(), identityConfig.getMasterUserUsername(), CredentialType.PASSWORD);
        } catch (ResourceNotFoundException exception) {
            CredentialRequest credentialRequest = new CredentialRequest();
            credentialRequest.setIdentifier(identityConfig.getMasterUserUsername());
            credentialRequest.setType(CredentialType.PASSWORD);
            credentialRequest.setSecret(identityConfig.getMasterUserPassword());
            credentialRequest.setUserId(userResponse.getId());
            credentialService.create(credentialRequest);
        }

        RoleResponse roleResponse;
        try {
            roleResponse = roleService.getByRoleNameAndTenantId(identityConfig.getMasterRoleName(),
                    identityConfig.getMasterTenantId());
        } catch (ResourceNotFoundException exception) {
            RoleRequest roleRequest = new RoleRequest();
            roleRequest.setRoleName(identityConfig.getMasterRoleName());
            roleRequest.setTenantId(identityConfig.getMasterTenantId());
            roleResponse = roleService.create(roleRequest);
        }

        for (String key : Permission.PERMISSIONS.keySet()) {
            try {
                PermissionEntity permission = permissionService.getPermissionByName(key);
                ModifyPermissionRequest modifyPermissionRequest = new ModifyPermissionRequest();
                modifyPermissionRequest.setPermissionId(permission.getId());
                roleService.addPermissionToRoleId(roleResponse.getId(), modifyPermissionRequest);
            } catch (ResourceNotFoundException exception) {
                log.warn("Missing permission: " + key + " in master initializer.");
            }
        }
    }

    @Override
    public int getOrder() {
        return MasterInitializerConfig.INIT_IDENTITY_ORDER;
    }
}
