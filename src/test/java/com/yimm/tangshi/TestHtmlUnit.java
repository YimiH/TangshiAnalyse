package com.yimm.tangshi;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;

/**
 * Author: sy
 * Create:2019-06-14
 * 10:00
 */
public class TestHtmlUnit {

    public static void main(String[] args) {
        try(WebClient webClient=new WebClient(BrowserVersion.CHROME)){
            try {
                /*不允许js文件启用*/
                webClient.getOptions().setJavaScriptEnabled(false);
                HtmlPage htmlPage=webClient.getPage("https://so.gushiwen.org/shiwenv_eeb217f8cb2d.aspx");


                /*String text=bodyElement.asText();
                String xml=bodyElement.asXml();
                System.out.println(text);*/
               /* HtmlArea area= (HtmlArea) htmlPage.getElementById("contson5da6d2de5a94");
                System.out.println(area.asText());
*/
                /*HtmlDivision domElement= (HtmlDivision) htmlPage.getElementById("contsonb9f0a5c5d3d3");
                String divContent=domElement.asText();
                System.out.println(divContent);*/

                /*HtmlDivision domElement= (HtmlDivision) htmlPage.getElementById("txtare582940");
                String divContent=domElement.asText();
                System.out.println(divContent);
                */


                //标题        //div[@class='yizhu']  text( )将内容转换为字符串
                String titlePath="//div[@class='cont']/h1/text()";
                DomText titleDom= (DomText) htmlPage.getByXPath(titlePath).get(0);
                String title=titleDom.asText();
                //System.out.println(title);

                //朝代
                String dynastyPath="//div[@class='cont']/p/a[1]";
                HtmlAnchor dynastyDom= (HtmlAnchor) htmlPage.getByXPath(dynastyPath).get(0);
                String dynasty=dynastyDom.asText();
                System.out.println(dynasty);

                //作者
                String authorPath="//div[@class='cont']/p/a[2]";
                HtmlAnchor authorDom= (HtmlAnchor) htmlPage.getByXPath(authorPath).get(0);
                String author=authorDom.asText();
                System.out.println(author);

                //正文
                String contentPath="//div[@class='cont']/div[@class='contson']";
                /*htmlPage.getByXPath(contentPath)
                        .forEach(o -> {
                            System.out.println(o.getClass().getName());
                            }
                        );*/
                HtmlDivision contentDom= (HtmlDivision) htmlPage.getByXPath(contentPath).get(0);
                String content=contentDom.asText();
                System.out.println(content);


                /*将其封装为对象*/
               /* PoetryInfo poetryInfo=new PoetryInfo();
                poetryInfo.setTitle(title);
                poetryInfo.setDynasty(dynasty);
                poetryInfo.setAuthor(author);
                poetryInfo.setContent(content);
*/


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
