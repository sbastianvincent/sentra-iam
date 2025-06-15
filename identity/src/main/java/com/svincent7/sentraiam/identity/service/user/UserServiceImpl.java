package com.svincent7.sentraiam.identity.service.user;

import com.svincent7.sentraiam.common.dto.user.UserRequest;
import com.svincent7.sentraiam.common.dto.user.UserResponse;
import com.svincent7.sentraiam.common.service.BaseMapper;
import com.svincent7.sentraiam.identity.model.UserEntity;
import com.svincent7.sentraiam.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    protected JpaRepository<UserEntity, String> getRepository() {
        return userRepository;
    }

    @Override
    protected BaseMapper<UserEntity, UserRequest, UserResponse> getMapper() {
        return userMapper;
    }
}
