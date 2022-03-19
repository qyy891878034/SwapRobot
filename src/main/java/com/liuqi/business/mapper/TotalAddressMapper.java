package com.liuqi.business.mapper;

import com.liuqi.base.BaseMapper;
import com.liuqi.business.model.TotalAddressModel;
import com.liuqi.business.model.TotalAddressModelDto;
import org.springframework.stereotype.Repository;

@Repository
public interface TotalAddressMapper extends BaseMapper<TotalAddressModel,TotalAddressModelDto>{

    TotalAddressModelDto getByUserIdAndProtocol(Long userId, Integer protocol);

}
