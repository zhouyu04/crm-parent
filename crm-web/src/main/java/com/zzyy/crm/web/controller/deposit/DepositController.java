package com.zzyy.crm.web.controller.deposit;



import com.alibaba.fastjson.JSONObject;
import com.zzyy.crm.model.deposit.AppQuery;
import com.zzyy.crm.model.deposit.Appointment;
import com.zzyy.crm.model.model.RespBean;
import com.zzyy.crm.model.model.RespPageBean;
import com.zzyy.crm.service.deposit.DepositBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/deposit/base/")
public class DepositController {

    @Autowired
    DepositBaseService depositBaseService;

    @RequestMapping("add")
    public RespBean add(@RequestBody Appointment appointment){

        depositBaseService.add(appointment);
        return RespBean.ok("保存成功");
    }

    @RequestMapping("edit")
    public RespBean edit(@RequestBody Appointment appointment){

        depositBaseService.edit(appointment);
        return RespBean.ok("修改成功");
    }


    @RequestMapping("list")
    public RespPageBean list(@RequestBody AppQuery info) {

        return depositBaseService.list(info);
    }

    @RequestMapping("detail")
    public RespBean detail(@RequestParam("id") String id){

        Appointment appointment = depositBaseService.detail(id);
        return RespBean.ok("success",appointment);
    }

    @RequestMapping("import")
    public RespBean importExcel(MultipartFile file) throws Exception{

        return depositBaseService.importExcel(file);
    }

    @RequestMapping("statistics")
    public RespBean statistics(){

        JSONObject result = depositBaseService.statistics();
        return RespBean.ok("success",result);
    }


}
