package com.Yimm.TangShiAnalyse.crawier.common;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: sy
 * Create:2019-06-14
 * 11:09
 */



@Data
public class Page {

    /*
    * 数据网站的根地址
    * 比如：https://www.gushiwen.org
    * */
    private final String base;

    /*
    * 具体网页路径
    * /shiwenv_a51c59087fc8.aspx
    * */
    private  final String path;

    /*
    * 网页的DOM对象---文档对象模型
    */
    private HtmlPage htmlPage;


    /*
    * 表示网页是否是详情页
    * */
    private final boolean detail;


    /*
    * 子页面对象集合
    */
    private Set<Page> subPage=new HashSet<>();


    /*
     * 数据对象
     */
    private DataSet dataSet=new DataSet();

    public String getURL(){
        return this.base+this.path;
    }

}
