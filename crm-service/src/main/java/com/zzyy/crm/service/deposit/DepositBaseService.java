package com.zzyy.crm.service.deposit;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zzyy.crm.common.utils.DateTimeUtil;
import com.zzyy.crm.common.utils.ExcelUtils;
import com.zzyy.crm.config.BizCustomException;
import com.zzyy.crm.mapper.DepositBaseMapper;
import com.zzyy.crm.model.deposit.*;
import com.zzyy.crm.model.model.RespBean;
import com.zzyy.crm.model.model.RespPageBean;
import com.zzyy.crm.model.model.excel.AppointmentImportDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DepositBaseService {


    @Autowired
    DepositBaseMapper depositBaseMapper;

    @Autowired
    DepositProService depositProService;

    @Autowired
    DepositSubService depositSubService;

    @Autowired
    RedisTemplate redisTemplate;

    @Transactional
    public void add(Appointment appointment) {

        checkParams(appointment);

        String projectId = appointment.getProjectId();
        if (StringUtils.isBlank(projectId)) {
            projectId = depositProService.addByName(appointment.getProject());
        }

        appointment.setProjectId(projectId);

        depositBaseMapper.addAppointment(appointment);
    }

    private void checkParams(Appointment appointment) {

        if (StringUtils.isBlank(appointment.getProject())) {
            throw new BizCustomException(20001, "摘要不能空为");
        }


    }

    public RespPageBean list(AppQuery info) {

        int page = (info.getPage() - 1) * info.getSize();
        info.setPage(page);

        RespPageBean respPageBean = new RespPageBean();

        if (StringUtils.isNotBlank(info.getType())) {
            Set<String> pop = (Set<String>) redisTemplate.opsForSet().pop(info.getType() + "_id");
            if (!CollectionUtils.isEmpty(pop)) {
                info.setProjectIds(pop);
            }
        }

        List<Appointment> appointmentList = depositBaseMapper.list(info);
        long count = depositBaseMapper.count(info);
        respPageBean.setData(appointmentList);
        respPageBean.setTotal(count);

        return respPageBean;
    }

    @Transactional
    public RespBean importExcel(MultipartFile file) throws IOException {

        List<AppointmentImportDto> appointments = ExcelUtils.readExcelFileToDTO(file, AppointmentImportDto.class);
        System.out.println(appointments);
        if (CollectionUtils.isEmpty(appointments)) {
            throw new BizCustomException(301, "导入数据不能为空");
        }
        //将对象中的数据一行一行处理成与数据库可行的
        //1.获取科目准备转换
        Set<String> subCodes = appointments.stream().map(AppointmentImportDto::getSubjectCode).collect(Collectors.toSet());
        List<SubjectInfo> subjectInfos = depositSubService.listByCodes(subCodes);
        Map<String, String> subMap = convertSubMap(subjectInfos);
        //2.获取摘要准备转换
        Set<String> proNames = appointments.stream().map(AppointmentImportDto::getProject).collect(Collectors.toSet());
        List<ProjectInfo> projectInfoList = depositProService.listByName(proNames);
        Map<String, String> proMap = convertProMap(projectInfoList);
        //2.将excel数据转换成
        List<Appointment> appointmentList = new ArrayList<>();
        for (AppointmentImportDto dto : appointments) {
            Appointment appointment = new Appointment();
            BeanUtils.copyProperties(dto, appointment);
            //处理科目值
            if (subMap.containsKey(dto.getSubjectCode())) {
                appointment.setSubId(subMap.get(dto.getSubjectCode()));
            } else {
                SubjectInfo sub = new SubjectInfo();
                sub.setSubjectName(dto.getSubjectName());
                sub.setSubjectCode(dto.getSubjectCode());
                SubjectInfo add = depositSubService.add(sub);
                appointment.setSubId(add.getId());
                subMap.put(dto.getSubjectCode(), add.getId());
            }
            //处理借贷值
            String directionStr = dto.getDirectionStr();
            if (StringUtils.equals(directionStr, "借")) {
                appointment.setDirection(1);
            } else if (StringUtils.equals(directionStr, "贷")) {
                appointment.setDirection(2);
            } else {
                appointment.setDirection(0);
            }
            //处理摘要
            String project = dto.getProject();
            if (proMap.containsKey(project)) {
                appointment.setProjectId(proMap.get(project));
            } else {
                appointment.setProjectId(depositProService.addByName(project));
            }
            try {
                appointment.setEmpDate(DateTimeUtil.parseShortDate(dto.getEmpDate()));
            } catch (ParseException e) {
                e.printStackTrace();
                throw new BizCustomException(1003, "日期转换异常");
            }
            appointmentList.add(appointment);
        }

        depositBaseMapper.batchInsert(appointmentList);
        return RespBean.ok("导入完成");
    }

    private Map<String, String> convertProMap(List<ProjectInfo> projectInfoList) {

        Map<String, String> map = new HashMap<>();
        if (CollectionUtils.isEmpty(projectInfoList)) {
            return map;
        }
        map = projectInfoList.stream().collect(Collectors.toMap(ProjectInfo::getName, ProjectInfo::getId));
        return map;
    }

    private Map<String, String> convertSubMap(List<SubjectInfo> subjectInfos) {

        Map<String, String> map = new HashMap<>();
        if (CollectionUtils.isEmpty(subjectInfos)) {
            return map;
        }
        map = subjectInfos.stream().collect(Collectors.toMap(SubjectInfo::getSubjectCode, SubjectInfo::getId));
        return map;
    }

    public Appointment detail(String id) {

        Appointment appointment = depositBaseMapper.findById(id);
        System.out.println(appointment);

        return appointment;
    }

    @Transactional
    public void edit(Appointment appointment) {


        String id = appointment.getId();
        if (StringUtils.isBlank(id)) {
            throw new BizCustomException(10001, "修改ID不能为空");
        }

        Appointment exist = depositBaseMapper.findById(appointment.getId());

        //修改项目
        String project = appointment.getProject();
        String existProject = exist.getProject();
        if (!StringUtils.equals(project, existProject)) {
            ProjectInfo projectInfo = new ProjectInfo();
            projectInfo.setId(exist.getProjectId());
            projectInfo.setName(appointment.getProject());
            depositProService.edit(projectInfo);
        }

        //修改保证金
        if (exist == null) {
            throw new BizCustomException(10001, "找不到修改数据");
        }

        Integer existIsCount = exist.getIsCount() != null ? exist.getIsCount() : 0;
        Integer isCount = appointment.getIsCount() != null ? appointment.getIsCount() : 0;
        if (!StringUtils.equals(exist.getReference(), appointment.getReference()) || existIsCount != isCount) {
            depositBaseMapper.edit(appointment);
        }

        //修改关联摘要
        List<String> releateProject = appointment.getReleateProject();
        if (!CollectionUtils.isEmpty(releateProject)) {
            List<AppointmentPro> appointmentPros = new ArrayList<>();
            releateProject.forEach(proId -> {
                AppointmentPro appointmentPro = new AppointmentPro();
                appointmentPro.setAppId(appointment.getId());
                appointmentPro.setProId(proId);
                appointmentPros.add(appointmentPro);
            });
            depositBaseMapper.batchInsertReleated(appointmentPros);
        }
    }

    public JSONObject statistics() {

        JSONObject res = new JSONObject();
        List<AppointmentStatistics> statistics = depositBaseMapper.statistics();
        log.info("获取统计结果：", JSON.toJSONString(statistics));
        if (CollectionUtils.isEmpty(statistics)) {
            return res;
        }
        int zero = 0;
        int positive = 0;//正数
        int negative = 0;//负数
        Set<String> zeroSet = new HashSet<>();
        Set<String> positiveSet = new HashSet<>();
        Set<String> negativeSet = new HashSet<>();

        for (AppointmentStatistics st : statistics) {
            String projectId = st.getProjectId();
            BigDecimal money = st.getMoney() != null ? st.getMoney() : BigDecimal.ZERO;
            if (money.compareTo(BigDecimal.ZERO) == 0) {
                zero++;
                zeroSet.add(projectId);
            } else if (money.compareTo(BigDecimal.ZERO) > 0) {
                positive++;
                positiveSet.add(projectId);
            } else {
                negative++;
                negativeSet.add(projectId);
            }
        }
        res.put("zero", zero);
        res.put("positive", positive);
        res.put("negative", negative);

        //将数据放到redis
        redisTemplate.opsForSet().add("zero_id", zeroSet);
        redisTemplate.opsForSet().add("positive_id", positiveSet);
        redisTemplate.opsForSet().add("negative_id", negativeSet);

        return res;
    }
}
