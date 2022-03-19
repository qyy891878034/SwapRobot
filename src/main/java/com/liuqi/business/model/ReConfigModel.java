package com.liuqi.business.model;

import com.liuqi.base.BaseModel;
import lombok.Data;

@Data
public class ReConfigModel extends BaseModel {

	/**
	 *serialVersionUID
	 */
	private static final long serialVersionUID = 1L;


	/**
	 *url地址
	 */
	
	private String url;
	
	/**
	 *商户编码
	 */
	
	private String storeNo;
	
	/**
	 *key
	 */
	
	private String key;
	


}
