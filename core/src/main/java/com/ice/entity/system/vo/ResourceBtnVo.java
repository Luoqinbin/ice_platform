package com.ice.entity.system.vo;

import java.io.Serializable;

import lombok.Data;
/**
 * @desc 用于设置角色的按钮权限
 * @author zhousg
 * @date 2016年8月22日下午5:42:15
 */
@Data
public class ResourceBtnVo implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	//是否已经选中，1  是   0  否
	private int isCheck;
}
