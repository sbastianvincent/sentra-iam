package com.svincent7.sentraiam.identity.controller;

import com.svincent7.sentraiam.common.controller.BaseController;
import com.svincent7.sentraiam.common.dto.user.UserRequest;
import com.svincent7.sentraiam.common.dto.user.UserResponse;
import com.svincent7.sentraiam.identity.model.UserEntity;
import com.svincent7.sentraiam.identity.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/identity/v1/users")
public class UserController extends BaseController<UserEntity, UserRequest, UserResponse, String> {
    private final UserService userService;

    @Override
    protected UserService getService() {
        return userService;
    }
}
