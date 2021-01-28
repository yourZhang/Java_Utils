package com.store.pay_test.utils;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * @program: road-health
 * @description: 导出和下载封装
 * @author: xiaozhang6666
 * @create: 2021-01-27 15:13
 **/
public class DownLoadExcelUtils {
    /**
     * 功能描述: <br>
     * 〈XSSFWorkbook 导出excel〉
     *
     * @Param: [request, response, fileName, xssfWorkbook]
     * @return: void
     * @Author: xiaozhang666
     * @Date: 2021/1/27 15:23
     */
    public static void downLoadExcel(HttpServletRequest request, HttpServletResponse response, String fileName, XSSFWorkbook xssfWorkbook) throws UnsupportedEncodingException {
        writeExcelHandler(request, response, fileName, xssfWorkbook);
    }

    /**
     * 功能描述: <br>
     * 〈SXSSFWorkbook 导出〉
     *
     * @Param: [request, response, fileName, sxssfWorkbook]
     * @return: void
     * @Author: xiaozhang666
     * @Date: 2021/1/27 17:17
     */
    public static void downLoadExcel(HttpServletRequest request, HttpServletResponse response, String fileName, SXSSFWorkbook sxssfWorkbook) throws UnsupportedEncodingException {
        writeExcelHandler(request, response, fileName, sxssfWorkbook);
    }

    /**
     * 功能描述: <br>
     * 〈处理导出文件〉
     *
     * @Param: [request, response, filelName]
     * @return: void
     * @Author: xiaozhang666
     * @Date: 2021/1/27 15:24
     */
    public static void cors(HttpServletRequest request, HttpServletResponse response, String filelName) throws UnsupportedEncodingException {
        response.reset();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        if (StringUtils.isEmpty(filelName)) {
            response.setHeader("Content-Disposition", "attachment;filename=download");
        } else {
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(filelName.getBytes("utf-8"), "iso-8859-1"));
        }
//        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PATCH, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Request-With, Content-Type, Accept");
    }

    /**
     * 功能描述: <br>
     * 〈具体导出数据逻辑〉
     *
     * @Param: [request, response, fileName, workbook]
     * @return: void
     * @Author: xiaozhang666
     * @Date: 2021/1/27 17:21
     */
    private static void writeExcelHandler(HttpServletRequest request, HttpServletResponse response, String fileName, Workbook workbook) throws UnsupportedEncodingException {
        cors(request, response, fileName);
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            workbook.write(out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
