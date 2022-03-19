package com.liuqi.business.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.liuqi.base.BaseMapper;
import com.liuqi.base.BaseServiceImpl;
import com.liuqi.business.model.ClampRecordModel;
import com.liuqi.business.model.ClampRecordModelDto;


import com.liuqi.business.service.ClampRecordService;
import com.liuqi.business.mapper.ClampRecordMapper;

@Service
@Transactional(readOnly = true)
public class ClampRecordServiceImpl extends BaseServiceImpl<ClampRecordModel,ClampRecordModelDto> implements ClampRecordService{

	@Autowired
	private ClampRecordMapper clampRecordMapper;
	

	@Override
	public BaseMapper<ClampRecordModel,ClampRecordModelDto> getBaseMapper() {
		return this.clampRecordMapper;
	}

}
