package com.store.bill.service.mapper;

import com.store.bill.service.dto.response.User;
import com.store.bill.service.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    User userEntityToUser(UserEntity userEntity);
}
