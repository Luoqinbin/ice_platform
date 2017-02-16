package com.ice.entity.system.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * @desc 用于设置角色的菜单树结构
 * @author zhousg
 * @date 2016年8月22日下午5:42:36
 */
@Data
public class ResourceTreeVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String pId;
	
	private String name;
	
	private boolean checked;
	
	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}
}
