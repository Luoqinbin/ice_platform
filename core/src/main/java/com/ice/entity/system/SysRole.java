package com.ice.entity.system;

import com.ice.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/16.
 */
@Data
@Table(name="sys_roles")
public class SysRole extends BaseEntity implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="role_name")
    private String role_name;
	
	@Column(name="role_desc")
    private String role_desc;
	
	@Column(name="enable")
    private Integer enable;

	@Column(name="role_auth")
    private String role_auth;

	@Column(name = "create_time")
	private Date create_time;
	@Column(name = "create_id")
	private String create_id;
	@Column(name = "create_name")
	private String create_name;
	@Column(name = "update_time")
	private Date update_time;
	@Column(name = "update_name")
	private String update_name;
	@Column(name = "update_id")
	private String update_id;
	@Column(name = "status")
	private Integer status;//1 可以，2 禁用， -1删除
}
