package com.zzyy.crm.service.deposit;


import com.zzyy.crm.config.BizCustomException;
import com.zzyy.crm.mapper.DepositSubMapper;
import com.zzyy.crm.model.deposit.SubjectInfo;
import com.zzyy.crm.model.model.RespPageBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class DepositSubService {

    @Autowired
    DepositSubMapper depositSubMapper;

    public SubjectInfo add(SubjectInfo subjectInfo) {

        checkParams(subjectInfo);

        int id = depositSubMapper.add(subjectInfo);

        return subjectInfo;
//        return RespBean.ok("添加成功",subjectInfo);
    }

    private void checkParams(SubjectInfo subjectInfo) {

        if (subjectInfo == null) {
            throw new BizCustomException(10001, "请求参数不合法");
        }
        if (StringUtils.isBlank(subjectInfo.getSubjectCode())) {
            throw new BizCustomException(10002, "科目代码不能为空");
        }
        if (StringUtils.isBlank(subjectInfo.getSubjectName())) {
            throw new BizCustomException(10003, "科目名称不能为空");
        }
    }

    public RespPageBean list(SubjectInfo subjectInfo) {

        int page = (subjectInfo.getPage() - 1) * subjectInfo.getSize();
        subjectInfo.setPage(page);

        List<SubjectInfo> infoList = depositSubMapper.list(subjectInfo);
        long total = depositSubMapper.listCount(subjectInfo);

        RespPageBean respPageBean = new RespPageBean();
        respPageBean.setData(infoList);
        respPageBean.setTotal(total);

        return respPageBean;
    }

    public List<SubjectInfo> listByCodes(Set<String> subCodes) {

        return depositSubMapper.listByCodes(subCodes);
    }
}
