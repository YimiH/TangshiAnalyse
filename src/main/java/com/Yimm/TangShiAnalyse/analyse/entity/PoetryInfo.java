package com.Yimm.TangShiAnalyse.analyse.entity;

import lombok.Data;

/**
 * Author: sy
 * Create:2019-07-26
 * 23:19
 */


/*与数据库关联的对象*/

@Data
public class PoetryInfo {

    /*标题*/
    private String title;
    /*作者朝代*/
    private String dynasty;
    /*作者*/
    private String author;
    /*正文*/
    private String content;
}
