package com.liuqi.business.model;

import lombok.Data;
import java.util.Date;
import java.math.BigDecimal;
import com.liuqi.base.BaseModel;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class AddressModel extends BaseModel{

	/**
	 *serialVersionUID
	 */
	private static final long serialVersionUID = 1L;


	/**
	 *路径
	 */
	
	private String address;
	
	/**
	 *地址
	 */
	
	private String privateKey;
	
	/**
	 *0未使用 1已使用
	 */
	
	private Integer protocol;
	
	private Long userId;

}
