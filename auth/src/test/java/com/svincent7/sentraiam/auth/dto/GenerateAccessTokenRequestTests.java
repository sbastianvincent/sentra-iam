package com.svincent7.sentraiam.auth.dto;

import com.svincent7.sentraiam.common.dto.permission.PermissionResponse;
import com.svincent7.sentraiam.common.dto.role.RoleWithPermissions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GenerateAccessTokenRequestTests {

    @Test
    void testGetPermissions() {
        List<PermissionResponse> permissions = new ArrayList<>();
        permissions.add(Mockito.mock(PermissionResponse.class));
        permissions.add(Mockito.mock(PermissionResponse.class));

        RoleWithPermissions role = new RoleWithPermissions();
        role.setPermissions(permissions);

        List<RoleWithPermissions> roles = new ArrayList<>();
        roles.add(role);

        GenerateAccessTokenRequest request = new GenerateAccessTokenRequest();
        request.setRoles(roles);

        Set<String> result = request.getPermissions();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.size(), 1);
        Assertions.assertEquals(roles.get(0).getPermissions().size(), 2);
    }
}
