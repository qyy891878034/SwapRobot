package com.liuqi.utils;


import cn.hutool.extra.qrcode.QrCodeUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * 生成二维码
 */
public class QrcodeUtils {

    public static void QRCode(String content, OutputStream out){
        QrCodeUtil.generate(content,200,200,"jpg",out);
    }




    public static void main(String[] args){
        try {
            FileOutputStream os=new FileOutputStream(new File("E:/1.png"));

            QrcodeUtils.QRCode("https://www.ehgex.com",os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
