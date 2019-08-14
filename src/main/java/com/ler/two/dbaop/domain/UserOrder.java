package com.ler.two.dbaop.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户订单表
 * </p>
 *
 * @author lww
 * @since 2019-08-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="UserOrder对象", description="用户订单表")
public class UserOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "订单名称")
    @TableField("order_name")
    private String orderName;

    @ApiModelProperty(value = "订单金额，单位分")
    @TableField("order_amount")
    private Long orderAmount;

    @ApiModelProperty(value = "创建人")
    @TableField("creator")
    private Long creator;

    @ApiModelProperty(value = "创建时间")
    @TableField("gmt_create")
    private Date gmtCreate;

    @ApiModelProperty(value = "修改人")
    @TableField("modifier")
    private Long modifier;

    @ApiModelProperty(value = "修改时间")
    @TableField("gmt_modify")
    private Date gmtModify;

    @ApiModelProperty(value = "逻辑删除")
    @TableField("is_deleted")
    private Integer isDeleted;


}
