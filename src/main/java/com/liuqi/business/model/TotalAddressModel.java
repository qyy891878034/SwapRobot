package com.liuqi.business.model;

import lombok.Data;
import java.util.Date;
import java.math.BigDecimal;
import com.liuqi.base.BaseModel;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class TotalAddressModel extends BaseModel{

	/**
	 *serialVersionUID
	 */
	private static final long serialVersionUID = 1L;


	/**
	 *用户id
	 */
	
	private Long userId;
	
	/**
	 *币种id
	 */
	
	private Long currencyId;
	
	/**
	 *协议
	 */
	
	private Integer protocol;
	
	/**
	 *空1
	 */
	
	private String airdrop;
	
	/**
	 *空2
	 */
	
	private String extract;
	
	/**
	 *空3
	 */
	
	private String collect;
	


}
