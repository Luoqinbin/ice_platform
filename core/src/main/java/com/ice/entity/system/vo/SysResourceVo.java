package com.ice.entity.system.vo;

import com.ice.order.BaseOrder;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 资源
 */
@Data
public class SysResourceVo extends  BaseOrder  implements Serializable {
    String id;
    @NotBlank
    private String resource_type;
    @NotBlank
    private String resource_name;
    @NotBlank
    private String resource_desc;
    @NotBlank
    private String resource_path;
    @NotBlank
    private String resource_parent;
    @NotNull
    private int enable;
    @NotBlank
    private String resource_icon;
    private int order_no;
    private int resource_level;
    private String btn_style;
    private Integer btn_index;

}
