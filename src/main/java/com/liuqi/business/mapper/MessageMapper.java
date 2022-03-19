package com.liuqi.business.mapper;

import com.liuqi.base.BaseMapper;
import com.liuqi.business.model.MessageModel;
import com.liuqi.business.model.MessageModelDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface MessageMapper extends BaseMapper<MessageModel,MessageModelDto>{


    Integer getNotReadCount(Long userId);

    MessageModelDto getTodayByType(@Param("userId") Long userId, @Param("type") Integer type, @Param("startDate")Date startDate,@Param("endDate")Date endDate);
}
