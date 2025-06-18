package com.svincent7.sentraiam.identity.initializer;

import com.svincent7.sentraiam.common.exception.ResourceNotFoundException;
import com.svincent7.sentraiam.common.initializer.Initializer;
import com.svincent7.sentraiam.common.permission.Permission;
import com.svincent7.sentraiam.identity.service.permission.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MasterPermissionInitializer implements Initializer {
    private final PermissionService permissionService;

    @Override
    public void initialize() {
        for (String key : Permission.PERMISSIONS.keySet()) {
            try {
                permissionService.getPermissionByName(key);
            } catch (ResourceNotFoundException exception) {
                permissionService.addPermission(key, Permission.PERMISSIONS.get(key).isUserVisible());
            }
        }
    }

    @Override
    public int getOrder() {
        return MasterInitializerConfig.INIT_PERMISSION_ORDER;
    }
}
