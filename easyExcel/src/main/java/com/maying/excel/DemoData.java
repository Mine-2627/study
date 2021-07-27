package com.maying.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * description
 *
 * @author ying.ma01@hand-china.com 2021/07/21 8:57
 */
@Data
public class DemoData {

    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("生日")
    private Date birthday;
    @ExcelProperty("年龄")
    private int aget;

    @ExcelIgnore
    private String string;
}
