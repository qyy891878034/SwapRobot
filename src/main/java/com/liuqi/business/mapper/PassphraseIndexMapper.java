package com.liuqi.business.mapper;

import com.liuqi.base.BaseMapper;
import com.liuqi.business.model.PassphraseIndexModel;
import com.liuqi.business.model.PassphraseIndexModelDto;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface PassphraseIndexMapper extends BaseMapper<PassphraseIndexModel,PassphraseIndexModelDto>{

    PassphraseIndexModelDto getByPidAndP(Long passphraseId, Integer protocol);

    @Update("update t_passphrase_index set `index` = #{index} where id = #{id}")
    void updateIndex(Long id, Integer index);
}
