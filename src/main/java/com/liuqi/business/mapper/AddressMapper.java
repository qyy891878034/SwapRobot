package com.liuqi.business.mapper;

import com.liuqi.base.BaseMapper;
import com.liuqi.business.model.AddressModel;
import com.liuqi.business.model.AddressModelDto;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressMapper extends BaseMapper<AddressModel,AddressModelDto>{

    @Select("select private_key from t_address where address = #{address}")
    String getPrivateKey(String address);

}
