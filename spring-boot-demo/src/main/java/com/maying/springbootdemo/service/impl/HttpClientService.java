package com.maying.springbootdemo.service.impl;


import okhttp3.*;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * description
 */
public class HttpClientService {

    private final static String url = "url";

    private final static String path = "C:\\Users\\yingma\\Desktop\\1.pdf";

    @Test
    public void downloadFile() throws Exception {
        OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(60000, TimeUnit.MILLISECONDS).readTimeout(60000, TimeUnit.MILLISECONDS).build();

        Request request = new Request.Builder().url(url).build();

        Response response = client.newCall(request).execute();
        //文件的总长度
        long max = response.body().contentLength();
        File file = new File(path);
        //当文件不存在，创建出来
        if (!file.exists()) {
            file.createNewFile();
        }

        System.out.println(response.body().toString());
        InputStream inputStream = response.body().byteStream();
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        byte[] bytes = new byte[1024];

        int readLength = 0;

        while ((readLength = inputStream.read(bytes)) != -1) {
            fileOutputStream.write(bytes, 0, readLength);
        }

        inputStream.close();
        fileOutputStream.close();
    }

}
