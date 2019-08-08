package com.Yimm.TangShiAnalyse.web;

import com.Yimm.TangShiAnalyse.analyse.model.AuthorCount;
import com.Yimm.TangShiAnalyse.analyse.model.WordCount;
import com.Yimm.TangShiAnalyse.analyse.service.AnalyzeService;
import com.Yimm.TangShiAnalyse.config.ObjectFactory;
import com.Yimm.TangShiAnalyse.crawier.Crawier;
import com.google.gson.Gson;
import spark.*;

import java.util.List;

/**
 * Web Api
 *
 * 1.Sparkjava 框架完成Web API开发
 * 2.Servlet技术实现Web API（后续可以修改）
 * Author: sy
 * Create:2019-07-30
 * 14:47
 */
public class WebController {

    /*传入分析服务对象---调用诗频以及词频的List链表*/
    private final AnalyzeService analyzeService;

    public WebController(AnalyzeService analyzeService) {
        this.analyzeService = analyzeService;
    }

    /*
    * URL地址
    * http
    *
    * ->  http://127.0.0.1:4567/
          /analyze/author_count
     */
    private List<AuthorCount>  analyzeAuthorCount(){
        return analyzeService.analyzeAuthorCount();
    }


    /*->  http://127.0.0.1:4567/
    ->  /analyze/word_cloud*/
    private List<WordCount> analyzeWordCloud(){
        return analyzeService.analyzeWordCount();
    }


    /*设置开启web服务的方法*/
    public void launch(){
        ResponseTransformer transformer=new JSONResponseTransformer();

        /*前端静态文件目录*/
        Spark.staticFileLocation("/static");

        /*服务端接口*/
        Spark.get("/analyze/author_count",
                ((request, response) -> analyzeAuthorCount()),
                transformer);

        Spark.get("/analyze/word_cloud",
                ((request, response) -> analyzeWordCloud()),
                transformer);

        Spark.get("/crawier/stop",
                ((request,response)->{
                    Crawier crawier  = ObjectFactory.getInstance().getObject(Crawier.class);
                    crawier.stop();
                    return "crawier stop";
                }));
    }


    /*转换器*/
    public static class JSONResponseTransformer implements ResponseTransformer{
        //Object-->String
        private Gson gson=new Gson();

        /**
         * @name: render
         * @description: 将一个Java对象转为字符串(Object-->String)
         * @param o
         * @return: java.lang.String
         *
        */
        @Override
        public String render(Object o) throws Exception {
            return gson.toJson(o);
        }
    }
}
