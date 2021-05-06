package com.example.photobook.mapperToEntity;

import com.example.photobook.dto.UserDataDto;
import com.example.photobook.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDataDtoMapper implements Mapper<UserDataDto, User> {

    private final ModelMapper modelMapper;

    @Override
    public User toEntity(UserDataDto userDataDto) {
        User user = new User();
        modelMapper.map(userDataDto, user);
        return user;
    }
}
