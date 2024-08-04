package com.example.service.user;

import com.example.dto.user.UserRegistrationRequestDto;
import com.example.dto.user.UserResponseDto;
import com.example.exception.exceptions.RegistrationException;

public interface UserService {

    UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException;
}
