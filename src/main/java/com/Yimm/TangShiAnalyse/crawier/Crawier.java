package com.Yimm.TangShiAnalyse.crawier;

import com.Yimm.TangShiAnalyse.crawier.PipeLine.Pipeline;
import com.Yimm.TangShiAnalyse.crawier.common.Page;
import com.Yimm.TangShiAnalyse.crawier.parse.Parse;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: sy
 * Create:2019-06-14
 * 10:59
 */
public class Crawier {

    private final Logger logger= LoggerFactory.getLogger(Crawier.class);

    /*放置文档页面（超链接）
    * 放置详情页面 */
    private final Queue<Page> docQueue=new LinkedBlockingDeque<>();

    /*放置详情页面*/
    private final Queue<Page> detailQueue=new LinkedBlockingDeque<>();

    /*采集器*/
    private final WebClient webClient;

    /*所有解析器*/
    private final List<Parse> parseList=new LinkedList<>();

    /*所有的清洗器（管道）*/
    private final List<Pipeline> pipelineList=new LinkedList<>();

    /*线程执行器*/
    private final ExecutorService executorService;

    public Crawier(){
        this.webClient=new WebClient(BrowserVersion.CHROME);
        this.webClient.getOptions().setJavaScriptEnabled(false);
        /*创建线程执行器*/
        this.executorService= Executors.newFixedThreadPool(8, new ThreadFactory() {
            private final AtomicInteger id=new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                Thread thread=new Thread(r);
                thread.setName("Crawier-Thread-"+id.getAndIncrement());
                return thread;
            }
        });
    }

    /*爬虫启动*/
    public void start(){

        /*多线程实现同步解析以及清洗--->提高效率
        由lambda-->方法引用*/
        this.executorService.submit(this::parse);

        this.executorService.submit(this::pipeline);
    }

    private void parse(){
        while(true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error("Parse occur exception{} .",
                        e.getMessage());
            }

            final Page page = this.docQueue.poll();
            if (page == null) {
                continue;
            }

            /*采集解析的多线程调度*/
            this.executorService.submit(() -> {
                try {
                    //采集（爬取）
                    HtmlPage htmlPage = Crawier.this.webClient.getPage(page.getURL());
                    page.setHtmlPage(htmlPage);

                    for (Parse parse : parseList) {
                        parse.parse(page);
                    }

                    //不是详情页面执行
                    if (page.isDetail()) {
                        //此处为详情页面
                        Crawier.this.detailQueue.add(page);

                    } else {
                        Iterator<Page> iterator = page.getSubPage().iterator();
                        while (iterator.hasNext()) {
                            Page subPage = iterator.next();
                            Crawier.this.docQueue.add(subPage);
                            iterator.remove();
                            //this.detailQueue.add(iterator.next());
                        }
                    }
                } catch (IOException e) {
                    logger.error("Parse task occur exception{} .",
                            e.getMessage());
                }
            });
        }
    }

    public void pipeline(){
        while(true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error("Pipeline occur exception{} .",
                        e.getMessage());
            }

            /*detail队列的page对象*/
            final Page page=this.detailQueue.poll();
            if(page==null){
                continue;
            }


            /*清洗的多线程调度*/
            this.executorService.submit(() -> {
                for(Pipeline pipeline:Crawier.this.pipelineList){
                    pipeline.pipeline(page);
                }
            });
        }
    }


    public void addPage(Page page){
        this.docQueue.add(page);
    }


    public void addParse(Parse parse){
        this.parseList.add(parse);
    }

    public void addPipeLine(Pipeline pipeline){
        this.pipelineList.add(pipeline);
    }


    /*爬虫停止*/
    public void stop(){
        if(this.executorService!=null && !this.executorService.isShutdown()){
            this.executorService.shutdown();
        }
        logger.info("Crawier stop...");
    }
}
