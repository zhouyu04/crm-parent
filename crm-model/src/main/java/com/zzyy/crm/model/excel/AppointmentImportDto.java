package com.zzyy.crm.model.model.excel;


import com.zzyy.crm.common.interfaces.FieldMeta;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AppointmentImportDto {

    @FieldMeta(fileNote = "科目代码")
    private String subjectCode;

    @FieldMeta(fileNote = "科目名称")
    private String subjectName;

    @FieldMeta(fileNote = "业务日期")
    private String empDate;//业务日期

    @FieldMeta(fileNote = "凭证字号")
    private String proof;//凭证

    @FieldMeta(fileNote = "参考信息")
    private String reference;//参考信息

    @FieldMeta(fileNote = "摘要")
    private String project;//摘要-既项目

    @FieldMeta(fileNote = "业务编号")
    private String businessNo;//业务编号

    @FieldMeta(fileNote = "结算方式")
    private String settlementType;//结算方式

    @FieldMeta(fileNote = "结算号")
    private String settlement;//结算号

    @FieldMeta(fileNote = "对方科目")
    private String targetSub;//对方科目

    @FieldMeta(fileNote = "系统模块")
    private String sysModule;//系统模块

    @FieldMeta(fileNote = "业务描述")
    private String describe;//描述

    @FieldMeta(fileNote = "金额")
    private BigDecimal money;//借方金额

    @FieldMeta(fileNote = "方向")
    private String directionStr;//方向 1-借 2-贷

    @FieldMeta(fileNote = "余额")
    private BigDecimal balance;//余额

}
