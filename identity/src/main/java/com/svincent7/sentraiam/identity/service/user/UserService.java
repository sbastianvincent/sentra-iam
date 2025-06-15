package com.svincent7.sentraiam.identity.service.user;

import com.svincent7.sentraiam.common.dto.user.UserRequest;
import com.svincent7.sentraiam.common.dto.user.UserResponse;
import com.svincent7.sentraiam.common.service.BaseService;
import com.svincent7.sentraiam.identity.model.UserEntity;

public abstract class UserService extends BaseService<UserEntity, UserRequest, UserResponse, String> {
}
