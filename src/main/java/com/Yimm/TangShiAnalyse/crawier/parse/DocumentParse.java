package com.Yimm.TangShiAnalyse.crawier.parse;

import com.Yimm.TangShiAnalyse.crawier.common.Page;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.List;

/**
 * Author: sy
 * Create:2019-07-26
 * 19:44
 */

/*链接解析*/
public class DocumentParse implements Parse {


    @Override
    /*final--防止再次new Page对象*/
    public void parse(final Page page) {
        if(page.isDetail())
            return;

        /*因为lambda表达式其外部变量必须用final修饰，所有不能使用int*/
        //final AtomicInteger count=new AtomicInteger(0);

        HtmlPage htmlPage=page.getHtmlPage();

        htmlPage.getBody()
                /*此处参数为原页面分析得到,分析文档得到*/
                .getElementsByAttribute("div",
                "class","typecont")
                /*取得每个div*/

                /*此处使用了Consumer接口（java.util.functio.Consumer类），天然支持lamdba表达式
                * Consumer接口：给定义一个参数,对其进行(消费)处理,处理的方式可以是任意操作*/
                .forEach(div -> {
                    List<HtmlElement> nodeList=
                            div.getHtmlElementsByTagName("a");
                    /*取得超链接*/
                   nodeList.forEach(aNode -> {

                       String path=aNode.getAttribute("href");
                      // count.getAndIncrement();

                       /*遍历完成后将子页面加到当前页面的子页面*/
                       Page subPage=new Page(
                               page.getBase(),
                               path,
                               true
                       );
                       page.getSubPage().add(subPage);

                       //System.out.println(path);
                   });
                });
        //System.out.println("总共："+count.get()+"个地址");
    }
}
