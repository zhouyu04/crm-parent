package com.zzyy.crm.model.model;

import lombok.Data;

@Data
public class PageInfo {

    private int page = 1;

    private int size = 10;

    private String key;
}
