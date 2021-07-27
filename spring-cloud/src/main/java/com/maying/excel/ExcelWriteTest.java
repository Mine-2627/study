package com.maying.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.junit.Test;

import java.io.FileOutputStream;

/**
 * description
 *
 */
public class ExcelWriteTest {
    // 构建路径
    String PATH = "D:\\IDEAProject\\java\\excelTest";

    @Test
    public void testWrite03() throws Exception {
        // 创建工作簿
        Workbook workbook = new HSSFWorkbook();
        // 创建工作表
        Sheet sheet = workbook.createSheet("考核成绩表");
        // 创建第一行
        Row row1 = sheet.createRow(0);// 第一行
        // 创建单元格
        Cell cell1 = row1.createCell(0);// 第一行的第一列
        cell1.setCellValue("数学");
        Cell cell2 = row1.createCell(1);
        cell2.setCellValue(100);
        // 第二行
        Row row2 = sheet.createRow(1);// 第一行
        Cell cell21 = row2.createCell(0);// 第一行的第一列
        cell21.setCellValue("时间");
        Cell cell22 = row2.createCell(1);
        cell22.setCellValue(new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
        // 生成表,IO流,03版本使用xls后缀
        FileOutputStream fileOutputStream = new FileOutputStream(PATH + "考核成绩表03.xls");
        workbook.write(fileOutputStream);
        // 关闭流
        fileOutputStream.close();
        System.out.println("考核成绩表03输出完毕");
    }

    @Test
    public void testWrite07() throws Exception {
        // 创建工作簿
        Workbook workbook = new XSSFWorkbook();
        // 创建工作表
        Sheet sheet = workbook.createSheet("考核成绩表");
        // 创建第一行
        Row row1 = sheet.createRow(0);// 第一行
        // 创建单元格
        Cell cell1 = row1.createCell(0);// 第一行的第一列
        cell1.setCellValue("语文");
        Cell cell2 = row1.createCell(1);
        cell2.setCellValue(100);
        // 第二行
        Row row2 = sheet.createRow(1);// 第一行
        Cell cell21 = row2.createCell(0);// 第一行的第一列
        cell21.setCellValue("时间");
        Cell cell22 = row2.createCell(1);
        cell22.setCellValue(new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
        // 生成表,IO流,07版本使用xlsx后缀
        FileOutputStream fileOutputStream = new FileOutputStream(PATH + "考核成绩表07.xlsx");
        workbook.write(fileOutputStream);
        // 关闭流
        fileOutputStream.close();
        System.out.println("考核成绩表07输出完毕");
    }

    @Test
    public void testWrite03BigData() throws Exception {
        long startDate = System.currentTimeMillis();
        // 创建工作簿
        Workbook workbook = new HSSFWorkbook();
        // 创建工作表
        Sheet sheet = workbook.createSheet("考核成绩表");
        for (int i=0; i<65537; i++){
            Row row = sheet.createRow(i);
            for(int j=0; j<10; j++){
                Cell cell = row.createCell(j);
                cell.setCellValue(j);
            }
        }
        // 生成表,IO流,03版本使用xls后缀
        FileOutputStream fileOutputStream = new FileOutputStream(PATH + "大数量测试表03.xls");
        workbook.write(fileOutputStream);
        // 关闭流
        fileOutputStream.close();
        System.out.println("大数量测试表03.xls输出完毕");
        long endDate = System.currentTimeMillis();
        System.out.println("耗时为："+(endDate-startDate));
    }


    @Test
    public void testWrite07BigData() throws Exception {
        long startDate = System.currentTimeMillis();
        // 创建工作簿
        Workbook workbook = new XSSFWorkbook();
        // 创建工作表
        Sheet sheet = workbook.createSheet("考核成绩表");
        for (int i=0; i<105537; i++){
            Row row = sheet.createRow(i);
            for(int j=0; j<10; j++){
                Cell cell = row.createCell(j);
                cell.setCellValue(j);
            }
        }
        // 生成表,IO流,03版本使用xls后缀
        FileOutputStream fileOutputStream = new FileOutputStream(PATH + "大数量测试表07.xlsx");
        workbook.write(fileOutputStream);
        // 关闭流
        fileOutputStream.close();
        System.out.println("大数量测试表07.xlsx输出完毕");
        long endDate = System.currentTimeMillis();
        System.out.println("耗时为："+(endDate-startDate));
    }

    @Test
    public void testWrite07BigDataSXXF() throws Exception {
        long startDate = System.currentTimeMillis();
        // 创建工作簿
        Workbook workbook = new SXSSFWorkbook();
        // 创建工作表
        Sheet sheet = workbook.createSheet("考核成绩表");
        for (int i=0; i<165537; i++){
            Row row = sheet.createRow(i);
            for(int j=0; j<10; j++){
                Cell cell = row.createCell(j);
                cell.setCellValue(j);
            }
        }
        // 生成表,IO流,03版本使用xls后缀
        FileOutputStream fileOutputStream = new FileOutputStream(PATH + "大数量测试表07S.xlsx");
        workbook.write(fileOutputStream);
        // 关闭流
        fileOutputStream.close();
        System.out.println("大数量测试表07S.xlsx输出完毕");

        long endDate = System.currentTimeMillis();
        System.out.println("耗时为："+(endDate-startDate));
    }
}
