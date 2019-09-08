package com.hpywl.controller;

import com.hpywl.entity.Progress;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UploadInfo {
    @ResponseBody
    @RequestMapping("/getInfo")
    public Map<String, Object> getUploadInfo(HttpServletRequest request, HttpServletResponse response){
        Map<String, Object> result = new HashMap<>();
        response.setHeader("Cache-Control", "no-store"); //禁止浏览器缓存
        response.setHeader("Pragrma", "no-cache");  //禁止浏览器缓存
        response.setDateHeader("Expires", 0);   //禁止浏览器缓存

        Progress status = (Progress) request.getSession(true).getAttribute("status");//从session中读取上传信息
        if(status == null){
            result.put("error", "没发现上传文件!");
            return result;
        }

        long startTime = status.getStartTime();  //上传开始时间
        long currentTime = System.currentTimeMillis(); //现在时间
        long time = (currentTime - startTime) /1000 +1;//已经传顺的时间 单位：s
        double velocity = status.getBytesRead()/time; //传输速度：byte/s
        double totalTime = status.getContentLength()/velocity; //估计总时间
        double timeLeft = totalTime -time;    //估计剩余时间
        int percent = (int)(100*(double)status.getBytesRead()/(double)status.getContentLength()); //百分比
        double length = status.getBytesRead()/1024/1024; //已完成数
        double totalLength = status.getContentLength()/1024/1024; //总长度  M
        result.put("startTime",startTime);
        result.put("currentTime",currentTime);
        result.put("time",time);
        result.put("velocity",velocity);
        result.put("totalTime",totalTime);
        result.put("timeLeft",timeLeft);
        result.put("percent",percent);
        result.put("length",length);
        result.put("totalLength",totalLength);
        return result;
    }
}
