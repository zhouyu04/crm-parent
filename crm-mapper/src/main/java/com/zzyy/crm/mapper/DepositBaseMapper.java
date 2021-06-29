package com.zzyy.crm.mapper;


import com.zzyy.crm.model.deposit.AppQuery;
import com.zzyy.crm.model.deposit.Appointment;
import com.zzyy.crm.model.deposit.AppointmentPro;
import com.zzyy.crm.model.model.PageInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DepositBaseMapper {
    List<Appointment> list(AppQuery info);

    void addAppointment(Appointment appointment);

    void batchInsert(List<Appointment> list);

    Appointment findById(String id);

    void edit(Appointment appointment);

    void batchInsertReleated(List<AppointmentPro> list);

    long count(PageInfo info);
}
