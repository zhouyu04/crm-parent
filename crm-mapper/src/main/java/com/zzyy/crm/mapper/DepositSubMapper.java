package com.zzyy.crm.mapper;

import com.zzyy.crm.model.deposit.SubjectInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface DepositSubMapper {

    int add(SubjectInfo subjectInfo);

    List<SubjectInfo> list(SubjectInfo subjectInfo);

    long listCount(SubjectInfo subjectInfo);

    List<SubjectInfo> listByCodes(@Param("subCodes") Set<String> subCodes);
}
