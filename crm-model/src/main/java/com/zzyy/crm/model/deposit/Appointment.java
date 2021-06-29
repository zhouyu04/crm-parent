package com.zzyy.crm.model.deposit;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class Appointment {

    private String id;

    private String subId;//科目id

    private String subjectName;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date empDate;//业务日期

    private String proof;//凭证

    private String reference;//参考信息

    private String projectId;

    private String project;//摘要-既项目

    private String projectCode;//项目code

    private List<String> releateProject;//关联项目名称

    private String businessNo;//业务编号

    private String settlementType;//结算方式

    private String settlement;//结算号

    private String targetSub;//对方科目

    private String sysModule;//系统模块

    private String describe;//描述

    private BigDecimal money;//借方金额

    private int direction;//方向 1-借 2-贷

    private BigDecimal balance;//余额

    List<ProjectInfo> pros;
}
