package com.longfor.smp.utils.excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author GuShiYe
 * @version 1.0
 * @description
 * @date 2021/1/26 16:43
 */
public class ImportExcelUtil {

    /**
     * 解析Excel文件
     *
     * @param in
     * @param fileName
     */
    public static Map<String, List<List<String>>> parseExcel(InputStream in, String fileName) throws Exception {

        List<List<String>> list = new ArrayList<>();
        List<List<String>> listError = new ArrayList<>();


        //创建Excel工作薄
        Workbook work = getWorkbook(in, fileName);
        if (null == work) {
            throw new Exception("创建Excel工作薄为空！");
        }

        HashMap<String, List<List<String>>> map = new HashMap<>(16);
        //获取页
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            Sheet sheet = work.getSheetAt(i);
            if (sheet == null) {
                continue;
            }




            //获取行
            for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
                Row row = sheet.getRow(j);

                if (row == null || row.getFirstCellNum() == j) {
                    continue;
                }

                Cell cellPhone = row.getCell(1);
                String cellPhoneValue = getCellValue(cellPhone);

                List<String> li = new ArrayList<>();
                List<String> liError = new ArrayList<>();


                if (!cellPhoneValue.matches("^1[3|4|5|7|8][0-9]{9}$")){
                    liError.add(cellPhoneValue);
                    listError.add(liError);
                   continue;
                }

                    for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                        String cellValue = getCellValue(row.getCell(y));
                        li.add(cellValue);
                    }
                        list.add(li);
                }
            map.put("true", list);
            map.put("false", listError);
        }
        return map;
    }


    private static String getCellValue(Cell cell) {
        String value;
        switch (cell.getCellType()) {
            // 数字
            case HSSFCell.CELL_TYPE_NUMERIC:
                //如果为时间格式的内容
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    //注：format格式 yyyy-MM-dd hh:mm:ss 中小时为12小时制，若要24小时制，则把小h变为H即可，yyyy-MM-dd HH:mm:ss
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
                    return value;
                } else {
                    cell.setCellType(1);
                    value = cell.getStringCellValue();
                    return value;
                }
                // 字符串
            case HSSFCell.CELL_TYPE_STRING:
                value = cell.getStringCellValue();

                return value;
            // Boolean
            case HSSFCell.CELL_TYPE_BOOLEAN:
                value = cell.getBooleanCellValue() + "";

                return value;
            // 公式
            case HSSFCell.CELL_TYPE_FORMULA:
                value = cell.getCellFormula() + "";

                return value;
            // 空值
            case HSSFCell.CELL_TYPE_BLANK:
                value = "";

                return value;
            // 故障
            case HSSFCell.CELL_TYPE_ERROR:
                value = "非法字符";

                return value;
            default:
                value = "未知类型";

                return value;
        }
    }


    /**
     * 判断文件格式
     *
     * @param inStr
     * @param fileName
     * @return
     * @throws Exception
     */
    public static Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
        Workbook workbook = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (".xls".equals(fileType)) {
            workbook = new HSSFWorkbook(inStr);
        } else if (".xlsx".equals(fileType)) {
            workbook = new XSSFWorkbook(inStr);
        } else {
            throw new Exception("请上传excel文件！");
        }
        return workbook;
    }

}
