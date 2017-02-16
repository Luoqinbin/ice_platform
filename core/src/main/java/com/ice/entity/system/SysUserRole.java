package com.ice.entity.system;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/19.
 */
@Data
@Table(name="sys_users_roles")
public class SysUserRole implements Serializable {
    @Id
    private String id;
    @Column(name = "role_id")
    private String role_id;
    @Column(name = "user_id")
    private String user_id;


}
