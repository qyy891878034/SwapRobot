package com.liuqi.business.model;

import lombok.Data;
import java.util.Date;
import java.math.BigDecimal;
import com.liuqi.base.BaseModel;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class ClampRecordModel extends BaseModel{

	/**
	 *serialVersionUID
	 */
	private static final long serialVersionUID = 1L;


	/**
	 *链名称
	 */
	
	private String chainName;
	
	/**
	 *夹的地址
	 */
	
	private String address;
	
	/**
	 *夹的币合约地址
	 */
	
	private String contractAddress;
	
	/**
	 *夹的哈希
	 */
	
	private String hash;
	


}
