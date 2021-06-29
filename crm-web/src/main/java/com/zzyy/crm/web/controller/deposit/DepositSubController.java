package com.zzyy.crm.web.controller.deposit;

import com.alibaba.fastjson.JSON;

import com.zzyy.crm.model.deposit.SubjectInfo;
import com.zzyy.crm.model.model.RespBean;
import com.zzyy.crm.model.model.RespPageBean;
import com.zzyy.crm.service.deposit.DepositSubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deposit/sub/")
public class DepositSubController {

    @Autowired
    DepositSubService depositSubService;

    @RequestMapping("add")
    public RespBean add(@RequestBody SubjectInfo subjectInfo) {

        subjectInfo = depositSubService.add(subjectInfo);
        return RespBean.ok("添加成功",subjectInfo);
    }

    @RequestMapping("list")
    public RespPageBean list(@RequestBody SubjectInfo subjectInfo) {

        RespPageBean list = depositSubService.list(subjectInfo);
        System.out.println(JSON.toJSONString(list));
        return list;


    }


}
