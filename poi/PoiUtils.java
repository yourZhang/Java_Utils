package com.store.pay_test.utils;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: road-health
 * @description: poi样式和数据处理
 * @author: xiaozhang6666
 * @create: 2021-01-27 17:23
 **/
@RestController
@RequestMapping("test")
public class PoiUtils {

    @Autowired
    private HttpServletRequest request;

    @Resource
    private HttpServletResponse response;


    @RequestMapping("download")
    public void showIndex() {
        List<User> userList = new ArrayList<>();
        User user1 = new User("小刘", 200);
        User user2 = new User("小张", 300);
        userList.add(user1);
        userList.add(user2);
        try {
            objectDataSetRow(request, response, userList);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //数组后每个字符串-后的数字为此列的宽度
    public static String[] titles = {"第一列-2000", "第二列-9000"};

    /**
     * 功能描述: <br>
     * 〈构建sheet页，并构建表头和样式〉
     *
     * @Param: [titles]
     * @return: org.apache.poi.xssf.streaming.SXSSFWorkbook
     * @Author: xiaozhang666
     * @Date: 2021/1/27 17:27
     */
    public static SXSSFWorkbook createSXSSFWorkbook(String[] titles) {
        //初始化一个workbook
        SXSSFWorkbook workbook = new SXSSFWorkbook(100);
        //允许创建样式
        workbook.setCompressTempFiles(true);
        final CellStyle cellStyle = workbook.createCellStyle();
        //垂直居中
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        //水平居中
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        //边框
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        //颜色 暂不设置
        final Font font = workbook.createFont();
        font.setBold(true);
        //将字体设置到全局样式
        cellStyle.setFont(font);
        //开始创建sheet
        List<String[]> stringArr = new ArrayList<>();
        final SXSSFSheet sheet1 = workbook.createSheet("Sheet1");
        for (int i = 0; i < titles.length; i++) {
            String[] split = titles[i].split("-");
            stringArr.add(split);
            //设置头宽度
            sheet1.setColumnWidth(i, Integer.parseInt(split[1]));
        }
        //创建表格头
        final SXSSFRow row = sheet1.createRow(0);
        for (int cellIndex = 0; cellIndex < titles.length; cellIndex++) {
            final SXSSFCell cell = row.createCell(cellIndex);
            cell.setCellValue(stringArr.get(cellIndex)[0]);
            cell.setCellStyle(cellStyle);
        }
        return workbook;
    }

    /**
     * 功能描述: <br>
     * 〈封装每一行中每个单元格的样式〉
     *
     * @Param: [sxssfWorkbook, rowList]
     * @return: void
     * @Author: xiaozhang666
     * @Date: 2021/1/28 9:50
     */
    public static void setRowStyle(SXSSFWorkbook sxssfWorkbook, List<SXSSFCell> rowList) {
        final CellStyle cellStyle = sxssfWorkbook.createCellStyle();
        sxssfWorkbook.setCompressTempFiles(true);
        //垂直居中
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        //水平居中
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        rowList.forEach((value) -> {
            value.setCellStyle(cellStyle);
        });
    }

    /**
     * 功能描述: <br>
     * 〈批量创建单元格，方便添加样式〉
     *
     * @Param: [dataTitles, row]
     * @return: java.util.List<org.apache.poi.xssf.streaming.SXSSFCell>
     * @Author: xiaozhang666
     * @Date: 2021/1/28 10:02
     */
    public static List<SXSSFCell> initCellList(String[] dataTitles, SXSSFRow row) {
        List<SXSSFCell> rowList = new ArrayList<>();
        for (int i = 0; i < dataTitles.length; i++) {
            final SXSSFCell cell = row.createCell(i);
            rowList.add(cell);
        }
        return rowList;
    }

    /**
     * 功能描述: <br>
     * 〈导出时调用此方法〉
     *
     * @Param: [request, response, user]
     * @return: void
     * @Author: xiaozhang666
     * @Date: 2021/1/28 10:08
     */
    public static void objectDataSetRow(HttpServletRequest request, HttpServletResponse response, List<User> user) throws UnsupportedEncodingException {
        final SXSSFWorkbook sxssfWorkbook = createSXSSFWorkbook(titles);
        SXSSFSheet sheet = sxssfWorkbook.getSheetAt(0);
        int rowIndex = 0;
        for (User userContent : user) {
            rowIndex++;
            final SXSSFRow row = sheet.createRow(rowIndex);
            final List<SXSSFCell> sxssfCells = initCellList(titles, row);
            sxssfCells.get(0).setCellValue(userContent.getName());
            sxssfCells.get(1).setCellValue(userContent.getAge());
            //添加完数据后批量添加样式
            setRowStyle(sxssfWorkbook, sxssfCells);
        }
        //下载
        DownLoadExcelUtils.downLoadExcel(request, response, "导出.xlsx", sxssfWorkbook);
    }
}


class User {
    private String name;
    private Integer age;

    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
