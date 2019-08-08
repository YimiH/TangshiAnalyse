package com.Yimm.TangShiAnalyse.crawier.parse;

import com.Yimm.TangShiAnalyse.crawier.common.Page;
import com.gargoylesoftware.htmlunit.html.*;

/**
 * Author: sy
 * Create:2019-06-14
 * 11:12
 */

/*
* 详情（数据）页面解析
* */
public class DataPageParse implements Parse {
    @Override
    public void parse(final Page page) {
        /*
        * 非详情页直接返回
        */
        if(!page.isDetail())
            return;
        HtmlPage htmlPage=page.getHtmlPage();
        HtmlBody body= (HtmlBody) htmlPage.getBody();

        //标题        //div[@class='yizhu']  text( )将内容转换为字符串
        String titlePath="//div[@class='cont']/h1/text()";
        DomText titleDom= (DomText) body.getByXPath(titlePath).get(0);
        String title=titleDom.asText();
        //System.out.println(title);

        //朝代
        String dynastyPath="//div[@class='cont']/p/a[1]";
        HtmlAnchor dynastyDom= (HtmlAnchor) body.getByXPath(dynastyPath).get(0);
        String dynasty=dynastyDom.asText();
        //System.out.println(dynasty);

        //作者
        String authorPath="//div[@class='cont']/p/a[2]";
        HtmlAnchor authorDom= (HtmlAnchor) body.getByXPath(authorPath).get(0);
        String author=authorDom.asText();
        //System.out.println(author);

        //正文
        String contentPath="//div[@class='cont']/div[@class='contson']";
        HtmlDivision contentDom= (HtmlDivision) body.getByXPath(contentPath).get(0);
        String content=contentDom.asText();
        //System.out.println(content);

        /*此处暂时不封装以便于添加更多数据*/
        page.getDataSet().putData("title",title);
        page.getDataSet().putData("dynasty",dynasty);
        page.getDataSet().putData("author",author);
        page.getDataSet().putData("content",content);

    }
}
