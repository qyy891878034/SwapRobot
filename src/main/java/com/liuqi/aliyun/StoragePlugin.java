/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package com.liuqi.aliyun;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * Plugin - 存储
 *
 * @author SHOP++ Team
 * @version 3.0
 */
public abstract class StoragePlugin {

	/**
	 * 获取ID
	 *
	 * @return ID
	 */
	public final String getId() {
		return getClass().getAnnotation(Component.class).value();
	}

	/**
	 * 获取名称
	 *
	 * @return 名称
	 */
	public abstract String getName();

	/**
	 * 获取版本
	 *
	 * @return 版本
	 */
	public abstract String getVersion();

	/**
	 * 获取作者
	 *
	 * @return 作者
	 */
	public abstract String getAuthor();

	/**
	 * 获取网址
	 *
	 * @return 网址
	 */
	public abstract String getSiteUrl();


	/**
	 * 文件上传
	 *
	 * @param path        上传路径
	 * @param file        上传文件
	 * @param contentType 文件类型
	 */
	public abstract void upload(String path, File file, String contentType);


	/**
	 * 上传图片
	 * @param bytes
	 * @return
	 */
	public abstract String upload(byte[] bytes);

	/**
	 * 获取访问URL
	 *
	 * @param path 上传路径
	 * @return 访问URL
	 */
	public abstract String getUrl(String path);

	/**
	 * 文件浏览
	 *
	 * @param path 浏览路径
	 * @return 文件信息
	 */
	public abstract List<FileInfo> browser(String path);

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		StoragePlugin other = (StoragePlugin) obj;
		return new EqualsBuilder().append(getId(), other.getId()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getId()).toHashCode();
	}


}