package com.maying.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import java.io.FileInputStream;

/**
 * description
 *
 */
public class ExcelReadTest {
    // 构建路径
    String PATH = "D:\\IDEAProject\\java\\excelTest";

    @Test
    public void testRead03() throws Exception {
        // 生成表,IO流,03版本使用xls后缀
        FileInputStream fileInputStream = new FileInputStream(PATH + "考核成绩表03.xls");
        Workbook workbook = new HSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(0);
        Cell cell = row.getCell(0);
        Cell cell2 = row.getCell(1);
        Row row2 = sheet.getRow(1);
        Cell cell3 = row2.getCell(0);
        Cell cell4 = row2.getCell(1);

        System.out.println(cell.getStringCellValue());
        System.out.println(cell2.getNumericCellValue());
        System.out.println(cell3.getStringCellValue());
        System.out.println(cell4.getStringCellValue());
        // 关闭流
        fileInputStream.close();
        System.out.println("考核成绩表03输出完毕");
    }

}
