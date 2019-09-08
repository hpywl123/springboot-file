package com.hpywl.controller;

import org.apache.commons.fileupload.FileItem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
public class Upload {

    @RequestMapping("/fileUploadController")
    public Map<String, Object> fileUpload(@RequestParam("fileObj") CommonsMultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String  filename = file.getFileItem().getName();
            System.out.println("开始上传。。。"+filename);
            //判断文件名称是否带有路径
            if (filename.lastIndexOf(":\\") != -1) {
                filename = filename.substring(filename.lastIndexOf("\\") + 1);
            }
            //获取后缀名
            String suffix = filename.substring(filename.lastIndexOf(".") + 1);
            //设置允许上传文件类型
            String suffixList = "jpg,png,ico,bmp,jpeg,txt,exe,mp4";
            //判断是否包含
            if (suffixList.contains(suffix.trim().toLowerCase())) {
                //获取项目路径request.getSession().getServletContext().getRealPath("")
                //保存文件的路径
                String path = "E:/slideImg/"+filename;
                System.out.println(path);
                //查看路径是否存在，不存在就创建
                if (!new File(path).exists()) {
                    new File(path).mkdirs();
                    file.transferTo(new File(path)); //保存文件
                    System.out.println( "上传成功");
                } else {
                    System.out.println("文件已存在");
                }
            } else {
                System.out.println("无法上传该格式文件");
            }
        } else {
            System.out.println("请选择需要上传的文件");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success",true);
        map.put("tag",true);
        return map;
    }

    private static void inputStreamToFile(InputStream ins, File file) {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = ins.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            throw new RuntimeException("调用inputStreamToFile异常" +e.getMessage());
        }finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (ins != null) {
                    ins.close();
                }
            } catch (Exception e) {
                throw new RuntimeException("调用inputStreamToFile异常" +e.getMessage());
            }
        }
    }
}
