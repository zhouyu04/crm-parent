package com.zzyy.crm.service.deposit;


import com.zzyy.crm.common.utils.DateTimeUtil;
import com.zzyy.crm.common.utils.PinYinUtil;
import com.zzyy.crm.config.BizCustomException;
import com.zzyy.crm.mapper.DepositProMapper;
import com.zzyy.crm.model.deposit.ProjectInfo;
import com.zzyy.crm.model.model.RespPageBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class DepositProService {

    @Autowired
    DepositProMapper depositProMapper;

    public RespPageBean list(ProjectInfo info) {

        info.setPage((info.getPage() - 1) * info.getSize());
        List<ProjectInfo> projectInfoList = depositProMapper.list(info);
        long count = depositProMapper.count(info);

        RespPageBean res = new RespPageBean();
        res.setData(projectInfoList);
        res.setTotal(count);
        return res;
    }


    public String addByName(String project) {

        ProjectInfo byName = depositProMapper.findByName(project);
        if (byName != null){
            return byName.getId();
        }

        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setName(project);
        projectInfo.setCode(createCode(project));

        depositProMapper.add(projectInfo);
        return projectInfo.getId();
    }

    public String createCode(String name) {

        if (StringUtils.isBlank(name)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        String firstSpell = sb.append(PinYinUtil.getFirstSpell(name))
                .append("_")
                .append(DateTimeUtil.format(new Date(), DateTimeUtil.SUP_SHORT_PATTERN)).toString();


        return firstSpell;
    }

    public List<ProjectInfo> listByName(Set<String> proNames) {


        return depositProMapper.listByName(proNames);
    }

    private void checkExist(ProjectInfo projectInfo) {
        ProjectInfo project = depositProMapper.findByName(projectInfo.getName());
        if (project != null) {
            throw new BizCustomException(1002, "项目名称已经存在");
        }

    }

    public void edit(ProjectInfo projectInfo) {
        checkExist(projectInfo);

        String code = createCode(projectInfo.getName());
        projectInfo.setCode(code);

        depositProMapper.edit(projectInfo);

    }
}
