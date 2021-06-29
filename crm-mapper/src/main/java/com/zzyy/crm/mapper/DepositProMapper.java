package com.zzyy.crm.mapper;

import com.zzyy.crm.model.deposit.ProjectInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface DepositProMapper {

    long add(ProjectInfo projectInfo);

    List<ProjectInfo> list(ProjectInfo info);

    long count(ProjectInfo info);

    List<ProjectInfo> listByName(@Param("list") Set<String> list);

    ProjectInfo findById(String id);

    ProjectInfo findByName(String name);

    void edit(ProjectInfo projectInfo);
}
