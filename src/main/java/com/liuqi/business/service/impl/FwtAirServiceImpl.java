package com.liuqi.business.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.liuqi.base.BaseMapper;
import com.liuqi.base.BaseServiceImpl;
import com.liuqi.business.model.FwtAirModel;
import com.liuqi.business.model.FwtAirModelDto;


import com.liuqi.business.service.FwtAirService;
import com.liuqi.business.mapper.FwtAirMapper;

@Service
@Transactional(readOnly = true)
public class FwtAirServiceImpl extends BaseServiceImpl<FwtAirModel,FwtAirModelDto> implements FwtAirService{

	@Autowired
	private FwtAirMapper fwtAirMapper;
	

	@Override
	public BaseMapper<FwtAirModel,FwtAirModelDto> getBaseMapper() {
		return this.fwtAirMapper;
	}

}
