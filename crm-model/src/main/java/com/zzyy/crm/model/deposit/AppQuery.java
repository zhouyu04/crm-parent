package com.zzyy.crm.model.deposit;


import com.zzyy.crm.model.model.PageInfo;
import lombok.Data;

@Data
public class AppQuery extends PageInfo {


    private String skey;

    private String subId;

}
