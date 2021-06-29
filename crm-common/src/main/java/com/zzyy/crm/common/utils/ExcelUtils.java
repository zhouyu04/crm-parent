package com.zzyy.crm.common.utils;


import com.zzyy.crm.common.interfaces.FieldMeta;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtils {

    public static final String XLS = ".xls";
    public static final String XLSX = ".xlsx";

    public static <T> List<T> readExcelFileToDTO(MultipartFile file, Class<T> clazz) throws IOException {
        return readExcelFileToDTO(file, clazz, 0);
    }


    public static <T> List<T> readExcelFileToDTO(MultipartFile file, Class<T> clazz, Integer sheetId) throws IOException {
        //将文件转成workbook类型
        Workbook workbook = buildWorkbook(file);
        //第一个表
        return readSheetToDTO(workbook.getSheetAt(sheetId), clazz);
    }


    public static <T> List<T> readSheetToDTO(Sheet sheet, Class<T> clazz) throws IOException {
        List<T> result = new ArrayList<>();
        List<Map<String, String>> sheetValue = changeSheetToMapList(sheet);
        for (Map<String, String> valueMap : sheetValue) {
            T dto = buildDTOByClass(clazz, valueMap);
            if (dto != null) {
                result.add(dto);
            }
        }
        return result;
    }

    //类型转换
    private static Workbook buildWorkbook(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        if (filename.endsWith(XLS)) {
            return new HSSFWorkbook(file.getInputStream());
        } else if (filename.endsWith(XLSX)) {
            return new XSSFWorkbook(file.getInputStream());
        } else {
            throw new IOException("unknown file format: " + filename);
        }
    }

    private static List<Map<String, String>> changeSheetToMapList(Sheet sheet) {
        List<Map<String, String>> result = new ArrayList<>();
        int rowNumber = sheet.getPhysicalNumberOfRows();
        String[] titles = getSheetRowValues(sheet.getRow(0)); // 第一行作为表头
        for (int i = 1; i < rowNumber; i++) {
            String[] values = getSheetRowValues(sheet.getRow(i));
            Map<String, String> valueMap = new HashMap<>();
            for (int j = 0; j < titles.length; j++) {
                valueMap.put(titles[j], values[j]);
            }
            result.add(valueMap);
        }
        return result;
    }

    private static <T> T buildDTOByClass(Class<T> clazz, Map<String, String> valueMap) {
        try {
            T dto = clazz.newInstance();
            for (Field field : clazz.getDeclaredFields()) {
                FieldMeta desc = field.getAnnotation(FieldMeta.class);
                if (desc == null) {
                    continue;
                }
                String value = valueMap.get(desc.fileNote());
                if (StringUtils.isNotBlank(value)) {
                    String name = field.getType().getName();
                    Method method = clazz.getMethod(getSetMethodName(field.getName()), field.getType());
                    if (name.equalsIgnoreCase("java.lang.Integer")) {
                        Integer age = Integer.parseInt(value.substring(0, value.indexOf(".")));
                        method.invoke(dto, age);
                    } else if (name.equalsIgnoreCase("java.math.BigDecimal")) {
                        BigDecimal bigDecimal = new BigDecimal(value);
                        method.invoke(dto, bigDecimal);
                    } else {
                        method.invoke(dto, value);
                    }
                }
            }
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getSetMethodName(String name) {
        String firstChar = name.substring(0, 1);
        return "set" + firstChar.toUpperCase() + name.substring(1);
    }

    private static String[] getSheetRowValues(Row row) {
        if (row == null) {
            return new String[]{};
        } else {
            int cellNumber = row.getLastCellNum();
            List<String> cellValueList = new ArrayList<>();
            for (int i = 0; i < cellNumber; i++) {
                cellValueList.add(getValueOnCell(row.getCell(i)));
            }
            return cellValueList.toArray(new String[0]);
        }
    }

    private static String getValueOnCell(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
//            case NUMERIC: return String.format("%.2f", cell.getNumericCellValue());
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return DateTimeUtil.formatDate(cell.getDateCellValue());
                }
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return cell.getBooleanCellValue() ? "true" : "false";
            case FORMULA:
                try {
                    return cell.getStringCellValue();
                } catch (Exception e) {
//                    return String.format("%.2f", cell.getNumericCellValue());
                    return String.valueOf(cell.getNumericCellValue());
                }
            default:
                return "";
        }
    }
}
