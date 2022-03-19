package com.liuqi.business.model;

import lombok.Data;
import java.util.Date;
import java.math.BigDecimal;
import com.liuqi.base.BaseModel;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class PassphraseIndexModel extends BaseModel{

	/**
	 *serialVersionUID
	 */
	private static final long serialVersionUID = 1L;


	/**
	 *登录用户
	 */
	
	private Long passphraseId;
	
	/**
	 *登录ip
	 */
	
	private Integer index;
	
	/**
	 *登录城市
	 */
	
	private Integer protocol;
	


}
