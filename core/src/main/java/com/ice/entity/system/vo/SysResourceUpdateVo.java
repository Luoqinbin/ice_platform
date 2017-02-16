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
public class SysResourceUpdateVo extends  BaseOrder  implements Serializable {
    @NotNull
    String idUpdate;
    @NotBlank
    private String resource_type_update;
    @NotBlank
    private String resource_name_update;
    @NotBlank
    private String resource_desc_update;
    @NotBlank
    private String resource_path_update;
    @NotBlank
    private String resource_parent_update;
    @NotNull
    private int enable_update;
    @NotBlank
    private String resource_icon_update;
    private int order_no_update;
    private int resource_level_update;
    private String btn_style_update;
    private Integer btn_index_update;

}
