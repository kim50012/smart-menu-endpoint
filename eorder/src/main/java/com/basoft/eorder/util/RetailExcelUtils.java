package com.basoft.eorder.util;

import com.basoft.eorder.domain.excel.ExcelColumn;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.poi.ss.usermodel.DateUtil.isADateFormat;
import static org.apache.poi.ss.usermodel.DateUtil.isValidExcelDate;

/**
 * @author Abbot
 * @description
 * @date 2018/9/12 15:27
 **/
public class RetailExcelUtils {

    private final static Logger log = LoggerFactory.getLogger(ExcelUtils.class);

    private final static String EXCEL2003 = "xls";
    private final static String EXCEL2007 = "xlsx";

    public final static String STORESETTLES_TYPE = "1";
    public final static String FILENAME_STORESETTLES_KOR = "정산내역";
    public final static String FILENAME_STORESETTLES_CHN = "结算内容";

    public final static String AGT_SETTLES_TYPE = "2";
    public final static String FILENAME_AGT_SETTLES_KOR = "대리상 정산내역";
    public final static String FILENAME_AGT_SETTLES_CHN = "代理商结算内容";

    public final static String RETAIL_ORDER_TYPE = "3";
    public final static String FILENAME_RETAIL_ORDER_KOR = "店铺订单";
    public final static String FILENAME_RETAIL_ORDER_CHN = "店铺订单";


    public final static short fontSize = 230;

    public static <T> List<T> readExcel(String path, Class<T> cls,MultipartFile file){

        String fileName = file.getOriginalFilename();
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            log.error("上传文件格式不正确");
        }
        List<T> dataList = new ArrayList<>();
        Workbook workbook = null;
        try {
            InputStream is = file.getInputStream();
            if (fileName.endsWith(EXCEL2007)) {
//                FileInputStream is = new FileInputStream(new File(path));
                workbook = new XSSFWorkbook(is);
            }
            if (fileName.endsWith(EXCEL2003)) {
//                FileInputStream is = new FileInputStream(new File(path));
                workbook = new HSSFWorkbook(is);
            }
            if (workbook != null) {
                //类映射  注解 value-->bean columns
                Map<String, List<Field>> classMap = new HashMap<>();
                List<Field> fields = Stream.of(cls.getDeclaredFields()).collect(Collectors.toList());
                fields.forEach(
                        field -> {
                            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                            if (annotation != null) {
                                String value = annotation.valueKor();
                                if (StringUtils.isBlank(value)) {
                                    return;//return起到的作用和continue是相同的 语法
                                }
                                if (!classMap.containsKey(value)) {
                                    classMap.put(value, new ArrayList<>());
                                }
                                field.setAccessible(true);
                                classMap.get(value).add(field);
                            }
                        }
                );
                //索引-->columns
                Map<Integer, List<Field>> reflectionMap = new HashMap<>(16);
                //默认读取第一个sheet
                Sheet sheet = workbook.getSheetAt(0);

                boolean firstRow = true;
                for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    //首行  提取注解
                    if (firstRow) {
                        for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                            Cell cell = row.getCell(j);
                            String cellValue = getCellValue(cell);
                            if (classMap.containsKey(cellValue)) {
                                reflectionMap.put(j, classMap.get(cellValue));
                            }
                        }
                        firstRow = false;
                    } else {
                        //忽略空白行
                        if (row == null) {
                            continue;
                        }
                        try {
                            T t = cls.newInstance();
                            //判断是否为空白行
                            boolean allBlank = true;
                            for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                                if (reflectionMap.containsKey(j)) {
                                    Cell cell = row.getCell(j);
                                    String cellValue = getCellValue(cell);
                                    if (StringUtils.isNotBlank(cellValue)) {
                                        allBlank = false;
                                    }
                                    List<Field> fieldList = reflectionMap.get(j);
                                    fieldList.forEach(
                                            x -> {
                                                try {
                                                    handleField(t, cellValue, x);
                                                } catch (Exception e) {
                                                    log.error(String.format("reflect field:%s value:%s exception!", x.getName(), cellValue), e);
                                                }
                                            }
                                    );
                                }
                            }
                            if (!allBlank) {
                                dataList.add(t);
                            } else {
                                log.warn(String.format("row:%s is blank ignore!", i));
                            }
                        } catch (Exception e) {
                            log.error(String.format("parse row:%s exception!", i), e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(String.format("parse excel exception!"), e);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    log.error(String.format("parse excel exception!"), e);
                }
            }
        }
        return dataList;
    }

    private static <T> void handleField(T t, String value, Field field) throws Exception {
        Class<?> type = field.getType();
        if (type == null || type == void.class || StringUtils.isBlank(value)) {
            return;
        }
        if (type == Object.class) {
            field.set(t, value);
            //数字类型
        } else if (type.getSuperclass() == null || type.getSuperclass() == Number.class) {
            if (type == int.class || type == Integer.class) {
                field.set(t, NumberUtils.toInt(value));
            } else if (type == long.class || type == Long.class) {
                field.set(t, NumberUtils.toLong(value));
            } else if (type == byte.class || type == Byte.class) {
                field.set(t, NumberUtils.toByte(value));
            } else if (type == short.class || type == Short.class) {
                field.set(t, NumberUtils.toShort(value));
            } else if (type == double.class || type == Double.class) {
                field.set(t, NumberUtils.toDouble(value));
            } else if (type == float.class || type == Float.class) {
                field.set(t, NumberUtils.toFloat(value));
            } else if (type == char.class || type == Character.class) {
                field.set(t, CharUtils.toChar(value));
            } else if (type == boolean.class) {
                field.set(t, BooleanUtils.toBoolean(value));
            } else if (type == BigDecimal.class) {
                field.set(t, new BigDecimal(value));
            }
        } else if (type == Boolean.class) {
            field.set(t, BooleanUtils.toBoolean(value));
        } else if (type == Date.class) {
            //
            field.set(t, value);
        } else if (type == String.class) {
            field.set(t, value);
        } else {
            Constructor<?> constructor = type.getConstructor(String.class);
            field.set(t, constructor.newInstance(value));
        }
    }

    public static boolean isCellDateFormatted(Cell cell) {
        if (cell == null) {
            return false;
        } else {
            boolean bDate = false;
            double d = cell.getNumericCellValue();
            if (isValidExcelDate(d)) {
                CellStyle style = cell.getCellStyle();
                if (style == null) {
                    return false;
                }

                int i = style.getDataFormat();
                String f = style.getDataFormatString();
                bDate = isADateFormat(i, f);
            }

            return bDate;
        }
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            if (isCellDateFormatted(cell)) {
                return HSSFDateUtil.getJavaDate(cell.getNumericCellValue()).toString();
            } else {
                return new BigDecimal(cell.getNumericCellValue()).toString();
            }
        } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            return StringUtils.trimToEmpty(cell.getStringCellValue());
        } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            return StringUtils.trimToEmpty(cell.getCellFormula());
        } else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
            return "";
        } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
            return "ERROR";
        } else {
            return cell.toString().trim();
        }

    }

    public static <T> void writeExcel(HttpServletResponse response,String describle,List<String> topList, List<T> dataList, Class<T> cls,String fileName,String language){
        Field[] fields = cls.getDeclaredFields();
        List<Field> fieldList = Arrays.stream(fields)
                .filter(field -> {
                    ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    if (annotation != null && annotation.col() > 0) {
                        field.setAccessible(true);
                        return true;
                    }
                    return false;
                }).sorted(Comparator.comparing(field -> {
                    int col = 0;
                    ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    if (annotation != null) {
                        col = annotation.col();
                    }
                    return col;
                })).collect(Collectors.toList());

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Sheet1");

        AtomicInteger ai = new AtomicInteger();
        {
            if(StringUtils.isNotBlank(describle)){
                //头部描述
                Row rowOne = sheet.createRow(ai.getAndIncrement());
                AtomicInteger ajOne = new AtomicInteger();
                Cell cell = rowOne.createCell(ajOne.getAndIncrement());
                CellStyle cellStyle = wb.createCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                cellStyle.setFillPattern(CellStyle.ALIGN_CENTER);
                cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                cellStyle.setBorderRight((short) 1);
                //cellStyle.setBorderBottom((short) 1);
                cellStyle.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
                sheet.setColumnWidth(ajOne.get(),256*30);

                Font font = wb.createFont();
                font.setBoldweight(Font.COLOR_NORMAL);
                font.setFontHeight(fontSize);
                cellStyle.setFont(font);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(describle);
            }


            //写入头部 start
            Row row = sheet.createRow(ai.getAndIncrement());
            AtomicInteger aj = new AtomicInteger();
            fieldList.forEach(field -> {
                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                String columnName = "";
                if (annotation != null) {
                    if ("kor".equals(language)) {
                        columnName = annotation.valueKor();
                    }else if("chn".equals(language)){
                        columnName = annotation.valueChn();
                    }
                }
                Cell cell = row.createCell(aj.getAndIncrement());

                CellStyle cellStyle = wb.createCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
                cellStyle.setFillPattern(CellStyle.ALIGN_CENTER);
                cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                cellStyle.setBorderRight((short)1);
                cellStyle.setBorderBottom((short)1);

                sheet.setColumnWidth(aj.get(),256*30);

                cellStyle.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());

                Font font = wb.createFont();
                font.setBoldweight(Font.COLOR_NORMAL);
                font.setFontHeight(fontSize);
                cellStyle.setFont(font);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(columnName);
            });
            //写入头部 end
        }
        //导出内容列表 dataList列表数据
        if (CollectionUtils.isNotEmpty(dataList)) {
            dataList.forEach(t -> {
                Row row1 = sheet.createRow(ai.getAndIncrement());
                CellStyle cellStyle = wb.createCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                cellStyle.setFillPattern(CellStyle.ALIGN_CENTER);
                cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                cellStyle.setBorderRight((short)1);
                cellStyle.setBorderBottom((short)1);
                cellStyle.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
                Font font = wb.createFont();
                font.setBoldweight(Font.COLOR_NORMAL);
                font.setFontHeight(fontSize);
                cellStyle.setFont(font);
                String content = t.toString();
                if (!content.contains("endRow")) {
                    cellStyle.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
                }
                AtomicInteger aj = new AtomicInteger();
                fieldList.forEach(field -> {
                    sheet.setColumnWidth(aj.get(),256*30);
                    Class<?> type = field.getType();
                    Object value = "";
                    try {
                        value = field.get(t);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Cell cell = row1.createCell(aj.getAndIncrement());
                    if (value != null) {
                        if (type == Date.class) {
                            cell.setCellValue(value.toString());
                            cell.setCellStyle(cellStyle);
                        }
                        cell.setCellValue(value.toString());
                        cell.setCellStyle(cellStyle);
                    }else{
                        cell.setCellStyle(cellStyle);
                    }
                });
            });
        }
        //冻结窗格
        wb.getSheet("Sheet1").createFreezePane(0, 2, 0, 1);
        //浏览器下载excel
        buildExcelDocument(fileName.replace(" ","")+".xlsx",wb,response);
        //生成excel文件
        //   /Users/a/Downloads/公众号点餐系统/model.xls
        //buildExcelFile("/Users/a/Downloads/公众号点餐系统/model.xls",wb);
    }




    /**
     * 浏览器下载excel
     * @param fileName
     * @param wb
     * @param response
     */

    private static  void  buildExcelDocument(String fileName, Workbook wb,HttpServletResponse response){
        try {
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            response.flushBuffer();
            wb.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成excel文件
     * @param path 生成excel路径
     * @param wb
     */
    private static  void  buildExcelFile(String path, Workbook wb){

        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        try {
            wb.write(new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //excel 文件名称
    public static  String getFileName(String language, String titleStart,String type) {
        String fileName = "";
        if(STORESETTLES_TYPE.equals(type)){
            if (language.equals("kor")) {
                fileName += titleStart + RetailExcelUtils.FILENAME_STORESETTLES_KOR;
            } else {
                fileName += titleStart + RetailExcelUtils.FILENAME_STORESETTLES_CHN;
            }
        } else if (AGT_SETTLES_TYPE.equals(type)) {
            if (language.equals("kor")) {
                fileName += titleStart + RetailExcelUtils.FILENAME_AGT_SETTLES_KOR;
            } else {
                fileName += titleStart + RetailExcelUtils.FILENAME_AGT_SETTLES_CHN;
            }
        } else if (RETAIL_ORDER_TYPE.equals(type)) {
            if (language.equals("kor")) {
                fileName += titleStart + RetailExcelUtils.FILENAME_RETAIL_ORDER_KOR;
            } else {
                fileName += titleStart + RetailExcelUtils.FILENAME_RETAIL_ORDER_CHN;
            }
        }
        return fileName;
    }


    public static String getTitle(String language,String month, String name,String type) {
        String title = "";
        if(type.equals(STORESETTLES_TYPE)) {
            if (language.equals("kor")) {
                title += month + " " + ExcelUtils.FILENAME_STORESETTLES_KOR + name;
            } else {
                title += month + " " + ExcelUtils.FILENAME_STORESETTLES_CHN + name;
            }
        }else if(type.equals(AGT_SETTLES_TYPE)){
            if (language.equals("kor")) {
                title += month + " " + ExcelUtils.FILENAME_AGT_SETTLES_KOR + name;
            } else {
                title += month + " " + ExcelUtils.FILENAME_AGT_SETTLES_CHN + name;
            }
        }
        return title;
    }


}