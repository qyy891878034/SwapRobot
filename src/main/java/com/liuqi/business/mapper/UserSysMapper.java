package com.liuqi.business.mapper;

import com.liuqi.base.BaseMapper;
import com.liuqi.business.model.UserSysModel;
import com.liuqi.business.model.UserSysModelDto;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSysMapper extends BaseMapper<UserSysModel, UserSysModelDto> {

    UserSysModelDto findByName(String name);
}
