package com.zzyy.crm.model.deposit;


import com.zzyy.crm.model.model.PageInfo;
import lombok.Data;

@Data
public class ProjectInfo extends PageInfo {

    private String id;

    private String name;

    private String code;
}
