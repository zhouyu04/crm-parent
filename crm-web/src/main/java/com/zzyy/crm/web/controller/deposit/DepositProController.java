package com.zzyy.crm.web.controller.deposit;


import com.zzyy.crm.model.deposit.ProjectInfo;
import com.zzyy.crm.model.model.RespBean;
import com.zzyy.crm.model.model.RespPageBean;
import com.zzyy.crm.service.deposit.DepositProService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deposit/pro/")
public class DepositProController {

    @Autowired
    DepositProService depositProService;

    @RequestMapping("/list")
    public RespPageBean list(@RequestBody ProjectInfo info){

        return depositProService.list(info);

    }

    @RequestMapping("createCode")
    public RespBean createCode(@RequestParam("name") String name) {
        return RespBean.ok(depositProService.createCode(name));
    }



}
