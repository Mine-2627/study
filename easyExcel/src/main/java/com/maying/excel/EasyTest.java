package com.maying.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * description
 *
 * @author ying.ma01@hand-china.com 2021/07/21 8:56
 */
public class EasyTest {

    String PATH = "D:\\IDEAProject\\java\\excelTest\\easyExcel";

    private List<DemoData> data(){
        List<DemoData> result = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            DemoData demo = new DemoData();
            demo.setAget(i);
            demo.setBirthday(new Date());
            demo.setString("dddd");
            demo.setName("字符串："+i);
            result.add(demo);
        }
        return result;
    }

    /**
     * 最简单的写
     * <p>1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>2. 直接写即可
     */
    @Test
    public void simpleWrite() {

        // 写法1
        String fileName = PATH + "simpleWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(fileName, DemoData.class).sheet("模板").doWrite(data());

    }
}
