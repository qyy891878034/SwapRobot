package com.liuqi.business.mapper;

import com.liuqi.base.BaseMapper;
import com.liuqi.business.model.PassphraseModel;
import com.liuqi.business.model.PassphraseModelDto;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface PassphraseMapper extends BaseMapper<PassphraseModel,PassphraseModelDto>{

    PassphraseModelDto getByUserId(Long userId);

}
