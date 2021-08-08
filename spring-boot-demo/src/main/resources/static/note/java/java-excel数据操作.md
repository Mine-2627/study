# 1.Poi

POI是Apache软件基金会的，POI为“Poor Obfuscation Implementation”的首字母缩写，意为“简洁版的模糊实现”。
所以POI的主要功能是可以用Java操作Microsoft Office的相关文件，这里我们主要讲Excel

## 1 .导入依赖

```
    <dependency>
        <groupId>org.apache.poi</groupId>
        <artifactId>poi</artifactId>
        <version>3.17</version>
    </dependency>
    //下面是07(xlsx)版本的，上面是03(xls)
    <dependency>
        <groupId>org.apache.poi</groupId>
        <artifactId>poi-ooxml</artifactId>
        <version>3.17</version>
    </dependency>
```
## 2 .开启读写操作，代码走起

无非就是对api的充分认识，接下来我们先去了解他的api

```java
 Workbook wordkbook =new HSSFWorkbook();//创建一个Workbook对象
 wordkbook.createSheet();//创建表名，如果不写参数，会有默认值
 Row row1=sheet.createRow(0);//根据里面的数字拿到对应的行，0默认为第一行
 Cell cell = row1.createCell(0);//根据行对象创建单元格，这里0为第一个
 cell.setCellValue("");//可以给单元格赋值
```

写入一个Excel

```
package com.example.test;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelWriter {
    //先要有个路劲
    static String path="F:\\demo\\javapoi\\demopoi\\";
    public static void main(String[] args) throws IOException {
        //1，创建一个工作薄
        Workbook wordkbook =new HSSFWorkbook();
        //表名
        Sheet sheet=wordkbook.createSheet("灰灰统计表");
        //创建行
        Row row1=sheet.createRow(0);
        //4.创建一个单元格
        Cell cell = row1.createCell(0);
        cell.setCellValue("今日新增观众");
        Cell cell2 = row1.createCell(1);
        cell2.setCellValue("卢本伟");

​    //创建行
​    Row row2=sheet.createRow(1);
​    //4.创建一个单元格
​    Cell cell3 = row2.createCell(0);
​    cell3.setCellValue("统计时间");
​    Cell cell24= row2.createCell(1);
​    String time=new DateTime().toString("yyyy-MM-dd HH:mm:ss");
​    cell24.setCellValue(time);

​    //生成一张表 03是xls 07是xlsx
​    FileOutputStream fileOutputStream = new FileOutputStream(path + "灰灰统计表07.xlsx");
   wordkbook.write(fileOutputStream);

   fileOutputStream.close();
    System.out.println("灰灰统计表03已生成");
}

}
```

上面写完后会在项目目录下生成一个表格

读取我们所写的表格

这个操作跟上述的写并没有什么不同，不同就是方法是get而不是set

```java
 static String path="F:\\demo\\javapoi\\demopoi";
    @Test
    public void testread03() throws IOException {

   //Sheet sheet=workbook.createSheet("统计表");
   //sheet操作表中元素
   FileInputStream fileInputStream = new FileInputStream(path + "灰灰统计表03.xls");
   Workbook workbook=new HSSFWorkbook(fileInputStream);
    Sheet sheet = workbook.getSheetAt(0);
    Sheet sheet2 = workbook.getSheet("灰灰统计表");
    Row row = sheet.getRow(1);
    Cell cell = row.getCell(0);
    Cell cell2 = row.getCell(1);
    System.out.println(cell.getStringCellValue());
   System.out.println(cell2.getNumericCellValue());
    fileInputStream.close();
}
```


这里值得注意的是，使用表格对象要注意三种创建方式

```
POI-HSSF：Excel97-2003版本，扩展名为.xls。一个sheet最大行数65536，最大列数256。

POI-XSSF：Excel2007版本开始，扩展名为.xlsx。一个sheet最大行数1048576，最大列数16384。

SXSSF：是在XSSF基础上，POI3.8版本开始提供的支持低内存占用的操作方式，扩展名为.xlsx。
```

Excel版本兼容性是向下兼容。

在读取数据的时候我们需要先判断值类型，才能用对应API

下面这个是先拿到表头那一行，相当于数据库的字段



        FileInputStream fileInputStream = new FileInputStream(path + "数据表07.xlsx");
        Workbook workbook=new XSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Row rowTitle = sheet.getRow(0);
        if(rowTitle!=null){
            int cellCount=rowTitle.getPhysicalNumberOfCells();    //拿到第row行的那一行的总个数
            for (int i = 0; i <cellCount ; i++) {  //循环个数取出
                Cell cell = rowTitle.getCell(i);
                if(cell!=null){          //如果不等于空取出值
                    int cellType = cell.getCellType();   //这里是知道我们标题是String，考虑不确定的时候怎么取
                    String cellValue = cell.getStringCellValue();
                    System.out.print(cellValue+"|");
                }
            }
            System.out.println();
        }

下面接着读取对应的数据，这里就需要我们刚刚讲的类型判断

```
int cellType=cell.getCellType();利用这个，然后判断它的XSSFCell类型再具体输出

  //获取表中内容
        int rowCount=sheet.getPhysicalNumberOfRows();
        for(int rowNum=1;rowNum<rowCount;rowNum++){
            Row rowData=sheet.getRow(rowNum); //取出对应的行
            if(rowData!=null){
                int cellCount=rowTitle.getPhysicalNumberOfCells();
                for(int cellNum=0;cellNum<cellCount;cellNum++){
                    System.out.print("["+(rowNum+1+"-"+(cellNum+1)+"]"));
                    Cell cell = rowData.getCell(cellNum);
                    //匹配数据类型
                    if(cell!=null){
                        int cellType=cell.getCellType();
                        switch (cellType){
                            case XSSFCell.CELL_TYPE_STRING: System.out.print("字符串："+cell.getStringCellValue());break;
                            case XSSFCell.CELL_TYPE_BOOLEAN: System.out.print("布尔："+cell.getBooleanCellValue());break;
                            case XSSFCell.CELL_TYPE_NUMERIC:
                                if(HSSFDateUtil.isCellDateFormatted(cell)){
                                    System.out.println("日期格式："+new DateTime(cell.getDateCellValue()).toString("yyyy-MM-dd HH:mm:ss"));break;
                                }else
                                    cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                                System.out.print("整形："+cell.toString());break;
                            case XSSFCell.CELL_TYPE_BLANK: System.out.print("空");break;
                            case XSSFCell.CELL_TYPE_ERROR: System.out.print("数据类型错误");break;
                            case Cell.CELL_TYPE_FORMULA:
                String formula=cell.getCellFormula();
                System.out.println("公式:"+formula);

​            //
​            CellValue evaluate = formulaEvaluator.evaluate(cell);
​            String cellValue=evaluate.formatAsString();
​            System.out.println(cellValue);
​            break;
​                        default:break;
​                    }
​                }
​            }
​        }
​    }
​     fileInputStream.close();
```



# 2.EasyExcel

这个的出现比poi简单非常多，只需要认清他的对应API就可以进行操作了，即使记不清楚了，我们也可以去网站上在线COPY

https://alibaba-easyexcel.github.io/

导入依赖

     //注意它里面自带poi依赖，如果重复带入会报ClassNotfound        
        <dependency>   
            <groupId>com.alibaba</groupId>
            <artifactId>easyexcel</artifactId>
            <version>2.2.0-beta2</version>
        </dependency>


## 2.1 读写操作


我们以上面这个表格为例来进行读写操作，触类旁通

### 2.1.1 写操作

先来个实体类方便插入数据

```
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

@Data   //lombok
public class DemoData {
    @ExcelProperty("字符串标题")
    private String string;
    @ExcelProperty("日期标题")
    private Date date;
    @ExcelProperty("数字标题")
    private Double doubleData;
    /**
     * 忽略这个字段
     */
    @ExcelIgnore   //注意这个注解是高版本的easyexcel依赖才有
    private String ignore;
}
```


再来一个工具类方便我们写数据
     * 忽略这个字段
          */
        @ExcelIgnore   //注意这个注解是高版本的easyexcel依赖才有
        private String ignore;
}

再来一个工具类方便我们写数据

```
public class utilList {
    public static List<DemoData> data() {
        List<DemoData> list = new ArrayList<DemoData>();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setString("字符串" + i);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            list.add(data);
        }
        return list;
    }
}
```


进行写

```
public class EasyTest {
    static String path="F:\\demo\\javapoi\\demopoi\\";
    @Test
    public void simpleWrite() {
        // 写法1
        String fileName = path + "EasyTest.xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(fileName, DemoData.class).sheet("模板").doWrite(utilList.data());



 // 写法2

//        fileName = TestFileUtil.getPath() + "simpleWrite" + System.currentTimeMillis() + ".xlsx";
//        // 这里 需要指定写用哪个class去写
//        ExcelWriter excelWriter = EasyExcel.write(fileName, DemoData.class).build();
//        WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
//        excelWriter.write(data(), writeSheet);
//        // 千万别忘记finish 会帮忙关闭流
//       excelWriter.finish();
    }
}
```

写完就有了这样一个表格

扩展知识

重复多次写,根据数据库的分页查询再写入到表格中，这里如果要写到多个表的话，需要建立多个writeSheet对象

    @Test
    public void repeatedWrite() {
        // 方法1 如果写到同一个sheet
        String fileName = TestFileUtil.getPath() + "repeatedWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写
        ExcelWriter excelWriter = EasyExcel.write(fileName, DemoData.class).build();
        // 这里注意 如果同一个sheet只要创建一次
        WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
        // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来
        for (int i = 0; i < 5; i++) {
            // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
            List<DemoData> data = data();
            excelWriter.write(data, writeSheet);
        }
        /// 千万别忘记finish 会帮忙关闭流
        excelWriter.finish();

WEB中的写（重点）

这个方法经常用到，导出Excel，失败的时候返回json

    /**
     * 文件下载并且失败的时候返回json（默认失败了会返回一个有部分数据的Excel）
     *
     * @since 2.1.1
     */
    @GetMapping("downloadFailedUsingJson")
    public void downloadFailedUsingJson(HttpServletResponse response) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("测试", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), DownloadData.class).autoCloseStream(Boolean.FALSE).sheet("模板")
                .doWrite(data());
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<String, String>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }
    }


### 2.1.2 读操作

首先我们一个监听器，因为和poi的不同，easyExcel是spring接管的，自己监控和改写方法

```
package com.example.easyExcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.example.Dao.DemoDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
// 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
public class DemoDataListener extends AnalysisEventListener<DemoData> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoDataListener.class);
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;
    List<DemoData> list = new ArrayList<DemoData>();
    /**
     * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
     */
    private DemoDAO demoDAO;
    public DemoDataListener() {
        // 这里是demo，所以随便new一个。实际使用如果到了spring,请使用下面的有参构造函数
        demoDAO = new DemoDAO();
    }
    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     *
     * @param demoDAO
     */
    public DemoDataListener(DemoDAO demoDAO) {
        this.demoDAO = demoDAO;
    }
    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data
     *            one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(DemoData data, AnalysisContext context) {
        LOGGER.info("解析到一条数据:{}", JSON.toJSONString(data));
        System.out.println( JSON.toJSONString(data));
        list.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            list.clear();
        }
    }
    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        LOGGER.info("所有数据解析完成！");
    }
    /**
     * 加上存储数据库
     */
    private void saveData() {
        LOGGER.info("{}条数据，开始存储数据库！", list.size());
        demoDAO.save(list);
        LOGGER.info("存储数据库成功！");
    }
}
```


这里的saveData是为了给读取前台的表格之后可以执行这个然后通过下面的方法持久化到数据库，而且这里默认是5条持久一次
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
          */
        private static final int BATCH_COUNT = 5;
        List<DemoData> list = new ArrayList<DemoData>();
        /**
          * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
               */
            private DemoDAO demoDAO;
            public DemoDataListener() {
        // 这里是demo，所以随便new一个。实际使用如果到了spring,请使用下面的有参构造函数
        demoDAO = new DemoDAO();
            }
            /**
               * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
                    *
                    * @param demoDAO
                         */
                    public DemoDataListener(DemoDAO demoDAO) {
        this.demoDAO = demoDAO;
                    }
                    /**
                         * 这个每一条数据解析都会来调用
                              *
                              * @param data
                                   *            one row value. Is is same as {@link AnalysisContext#readRowHolder()}
                                   * @param context
                                        */
                                @Override
                                public void invoke(DemoData data, AnalysisContext context) {
        LOGGER.info("解析到一条数据:{}", JSON.toJSONString(data));
        System.out.println( JSON.toJSONString(data));
        list.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {
                                    saveData();
                                    // 存储完成清理 list
                                    list.clear();
        }
                                }
                                /**
                                        * 所有数据解析完成了 都会来调用
                                             *
                                             * @param context
                                                  */
                                        @Override
                                        public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        LOGGER.info("所有数据解析完成！");
                                        }
                                        /**
                                                  * 加上存储数据库
                                                       */
                                            private void saveData() {
        LOGGER.info("{}条数据，开始存储数据库！", list.size());
        demoDAO.save(list);
        LOGGER.info("存储数据库成功！");
                                            }
}

这里的saveData是为了给读取前台的表格之后可以执行这个然后通过下面的方法持久化到数据库，而且这里默认是5条持久一次

 * 假设这个是你的DAO存储。当然还要这个类让spring管理，当然你不用需要存储，也不需要这个类。

    ```
    public class DemoDAO {
    public void save(List<DemoData> list) {
        // 如果是mybatis,尽量别直接调用多次insert,自己写一个mapper里面新增一个方法batchInsert,所有数据一次性插入
    }
    }
    ```


    开始读的操作
    
     * 最简单的读
     * <p>1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link DemoDataListener}
     * 3. 直接读即可
     
       ```
       @Test
          public void simpleRead() {
          String fileName = path + "EasyTest.xlsx";
       EasyExcel.read(fileName, DemoData.class, new DemoDataListener()).sheet().doRead();
       }
       ```


​       


WEB中的读

    /**
     * 文件上传
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link UploadData}
     * <p>
     * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link UploadDataListener}
     * <p>
     * 3. 直接读即可
     */
    @PostMapping("upload")
    @ResponseBody
    public String upload(MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), UploadData.class, new UploadDataListener(uploadDAO)).sheet().doRead();
        return "success";
    }
4.总结

- easyExcel的确比poi方便，但是它的读需要编写监听器
- 建议大数据用easyExcel,因为大数据时poi对于内存消耗非常大
- 由于apache poi和jxl，excelPOI都有一个严重的问题，就是非常消耗内存，特别处理数据量多时，速度慢并且时有异常发生，所以改用由阿里研发的easyExcel更可靠一些,它的官方建议对于1000行以内的采用原来poi的写法一次读写，但于1000行以上的数据，有用了一行行进行解析的方案，这样避免了内存的溢出。
- EasyExcel扩展功能很多，且Api式调用真的轻松很多



## 3.1详细参数介绍

### 详细参数介绍

#### 关于常见类解析

- EasyExcel 入口类，用于构建开始各种操作

- ExcelReaderBuilder ExcelWriterBuilder 构建出一个 ReadWorkbook WriteWorkbook，可以理解成一个excel对象，一个excel只要构建一个

- ExcelReaderSheetBuilder ExcelWriterSheetBuilder 构建出一个 ReadSheet WriteSheet对象，可以理解成excel里面的一页,每一页都要构建一个

- ReadListener 在每一行读取完毕后都会调用ReadListener来处理数据

- WriteHandler 在每一个操作包括创建单元格、创建表格等都会调用WriteHandler来处理数据

- 所有配置都是继承的，Workbook的配置会被Sheet继承，所以在用EasyExcel设置参数的时候，在EasyExcel…sheet()方法之前作用域是整个sheet,之后针对单个sheet

  ### 读

  #### 注解

- `ExcelProperty` 指定当前字段对应excel中的那一列。可以根据名字或者Index去匹配。当然也可以不写，默认第一个字段就是index=0，以此类推。千万注意，要么全部不写，要么全部用index，要么全部用名字去匹配。千万别三个混着用，除非你非常了解源代码中三个混着用怎么去排序的。

- `ExcelIgnore` 默认所有字段都会和excel去匹配，加了这个注解会忽略该字段

- `DateTimeFormat` 日期转换，用`String`去接收excel日期格式的数据会调用这个注解。里面的`value`参照`java.text.SimpleDateFormat`

- `NumberFormat` 数字转换，用`String`去接收excel数字格式的数据会调用这个注解。里面的`value`参照`java.text.DecimalFormat`

- ```
  ExcelIgnoreUnannotated
  ```

   

  默认不加

  ```
  ExcelProperty
  ```

   

  的注解的都会参与读写，加了不会参与

  ### 参数

  #### 通用参数

  ```
  ReadWorkbook
  ```

  ,

  ```
  ReadSheet
  ```

   

  都会有的参数，如果为空，默认使用上级。

- `converter` 转换器，默认加载了很多转换器。也可以自定义。

- `readListener` 监听器，在读取数据的过程中会不断的调用监听器。

- `headRowNumber` 需要读的表格有几行头数据。默认有一行头，也就是认为第二行开始起为数据。

- `head` 与`clazz`二选一。读取文件头对应的列表，会根据列表匹配数据，建议使用class。

- `clazz` 与`head`二选一。读取文件的头对应的class，也可以使用注解。如果两个都不指定，则会读取全部数据。

- `autoTrim` 字符串、表头等数据自动trim

- ```
  password
  ```

   

  读的时候是否需要使用密码

  #### ReadWorkbook（理解成excel对象）参数

- `excelType` 当前excel的类型 默认会自动判断

- `inputStream` 与`file`二选一。读取文件的流，如果接收到的是流就只用，不用流建议使用`file`参数。因为使用了`inputStream` easyexcel会帮忙创建临时文件，最终还是`file`

- `file` 与`inputStream`二选一。读取文件的文件。

- `autoCloseStream` 自动关闭流。

- `readCache` 默认小于5M用 内存，超过5M会使用 `EhCache`,这里不建议使用这个参数。

- ```
  useDefaultListener
  ```

   

  ```
  @since 2.1.4
  ```

   

  默认会加入

  ```
  ModelBuildEventListener
  ```

   

  来帮忙转换成传入

  ```
  class
  ```

  的对象，设置成

  ```
  false
  ```

  后将不会协助转换对象，自定义的监听器会接收到

  ```
  Map<Integer,CellData>
  ```

  对象，如果还想继续接听到

  ```
  class
  ```

  对象，请调用

  ```
  readListener
  ```

  方法，加入自定义的

  ```
  beforeListener
  ```

  、

   

  ```
  ModelBuildEventListener
  ```

  、 自定义的

  ```
  afterListener
  ```

  即可。

  #### ReadSheet（就是excel的一个Sheet）参数

- `sheetNo` 需要读取Sheet的编码，建议使用这个来指定读取哪个Sheet

- ```
  sheetName
  ```

   

  根据名字去匹配Sheet,excel 2003不支持根据名字去匹配

  ## 写

  ### 注解

- `ExcelProperty` index 指定写到第几列，默认根据成员变量排序。`value`指定写入的名称，默认成员变量的名字，多个`value`可以参照快速开始中的复杂头

- `ExcelIgnore` 默认所有字段都会写入excel，这个注解会忽略这个字段

- `DateTimeFormat` 日期转换，将`Date`写到excel会调用这个注解。里面的`value`参照`java.text.SimpleDateFormat`

- `NumberFormat` 数字转换，用`Number`写excel会调用这个注解。里面的`value`参照`java.text.DecimalFormat`

- ```
  ExcelIgnoreUnannotated
  ```

   

  默认不加

  ```
  ExcelProperty
  ```

   

  的注解的都会参与读写，加了不会参与

  ### 参数

  #### 通用参数

  ```
  WriteWorkbook
  ```

  ,

  ```
  WriteSheet
  ```

   

  ,

  ```
  WriteTable
  ```

  都会有的参数，如果为空，默认使用上级。

- `converter` 转换器，默认加载了很多转换器。也可以自定义。

- `writeHandler` 写的处理器。可以实现`WorkbookWriteHandler`,`SheetWriteHandler`,`RowWriteHandler`,`CellWriteHandler`，在写入excel的不同阶段会调用

- `relativeHeadRowIndex` 距离多少行后开始。也就是开头空几行

- `needHead` 是否导出头

- `head` 与`clazz`二选一。写入文件的头列表，建议使用class。

- `clazz` 与`head`二选一。写入文件的头对应的class，也可以使用注解。

- ```
  autoTrim
  ```

   

  字符串、表头等数据自动trim

  #### WriteWorkbook（理解成excel对象）参数

- `excelType` 当前excel的类型 默认`xlsx`

- `outputStream` 与`file`二选一。写入文件的流

- `file` 与`outputStream`二选一。写入的文件

- `templateInputStream` 模板的文件流

- `templateFile` 模板文件

- `autoCloseStream` 自动关闭流。

- `password` 写的时候是否需要使用密码

- ```
  useDefaultStyle
  ```

   

  写的时候是否是使用默认头

  #### WriteSheet（就是excel的一个Sheet）参数

- `sheetNo` 需要写入的编码。默认0

- ```
  sheetName
  ```

   

  需要些的Sheet名称，默认同

  ```
  sheetNo
  ```

  #### WriteTable（就把excel的一个Sheet,一块区域看一个table）参数

- `tableNo` 需要写入的编码。默认0
