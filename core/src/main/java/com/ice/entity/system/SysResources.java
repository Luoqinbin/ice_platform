package com.ice.entity.system;

import com.ice.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

/**
 * 资源
 */
public class SysResources extends BaseEntity implements Serializable {
    private String resource_type;
    private String resource_name;
    private String resource_desc;
    private String resource_path;
    private String resource_parent;
    private int enable;
    private String resource_icon;
    private int order_no;
    private int resource_level;
    private String btn_style;
    private Integer btn_index;
    @Transient
    private List<SysResources> childs;

    public String getResource_type() {
        return resource_type;
    }

    public void setResource_type(String resource_type) {
        this.resource_type = resource_type;
    }

    public String getResource_name() {
        return resource_name;
    }

    public void setResource_name(String resource_name) {
        this.resource_name = resource_name;
    }

    public String getResource_desc() {
        return resource_desc;
    }

    public void setResource_desc(String resource_desc) {
        this.resource_desc = resource_desc;
    }

    public String getResource_path() {
        return resource_path;
    }

    public void setResource_path(String resource_path) {
        this.resource_path = resource_path;
    }

    public String getResource_parent() {
        return resource_parent;
    }

    public void setResource_parent(String resource_parent) {
        this.resource_parent = resource_parent;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public String getResource_icon() {
        return resource_icon;
    }

    public void setResource_icon(String resource_icon) {
        this.resource_icon = resource_icon;
    }

    public int getOrder_no() {
        return order_no;
    }

    public void setOrder_no(int order_no) {
        this.order_no = order_no;
    }

    public int getResource_level() {
        return resource_level;
    }

    public void setResource_level(int resource_level) {
        this.resource_level = resource_level;
    }

    public String getBtn_style() {
        return btn_style;
    }

    public void setBtn_style(String btn_style) {
        this.btn_style = btn_style;
    }

    public Integer getBtn_index() {
        return btn_index;
    }

    public void setBtn_index(Integer btn_index) {
        this.btn_index = btn_index;
    }

    public List<SysResources> getChilds() {
        return childs;
    }

    public void setChilds(List<SysResources> childs) {
        this.childs = childs;
    }
}
