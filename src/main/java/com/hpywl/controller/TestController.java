package com.hpywl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class TestController {

    @RequestMapping("/testJson")
    @ResponseBody
    public Map<String,String> testJson(){
        Map<String,String> map = new HashMap<String,String>();
        map.put("author","袁文郎");
        map.put("address","北京东城区光明楼小区6号楼2单元203室");
        return map;
    }

    @RequestMapping("/testJsp")
    public String testJsp(){
        return "/index.jsp";
    }

    @RequestMapping("/testHtml")
    public String testHtml(){
        return "/index.html";
    }
}
