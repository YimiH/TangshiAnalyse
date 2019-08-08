package com.Yimm.TangShiAnalyse.config;

import com.Yimm.TangShiAnalyse.analyse.dao.AnalyzeDao;
import com.Yimm.TangShiAnalyse.analyse.dao.impl.AnalyzeDaoImpl;
import com.Yimm.TangShiAnalyse.analyse.service.AnalyzeService;
import com.Yimm.TangShiAnalyse.analyse.service.impl.AnalyzeServiceImpl;
import com.Yimm.TangShiAnalyse.crawier.Crawier;
import com.Yimm.TangShiAnalyse.crawier.PipeLine.ConsolePipeline;
import com.Yimm.TangShiAnalyse.crawier.PipeLine.DatabasePipeline;
import com.Yimm.TangShiAnalyse.crawier.common.Page;
import com.Yimm.TangShiAnalyse.crawier.parse.DataPageParse;
import com.Yimm.TangShiAnalyse.crawier.parse.DocumentParse;
import com.Yimm.TangShiAnalyse.web.WebController;
import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: sy
 * Create:2019-07-30
 * 13:47
 */
public final class ObjectFactory {
    private final Logger logger= LoggerFactory.getLogger(ObjectFactory.class);

    /*饿汉单例*/
    private static final ObjectFactory instance=new ObjectFactory();

    /*存放所有对象*/
   // private static final Map<Class,Object> objectHashMap =new HashMap<>();
    private  final Map<Class,Object> objectHashMap=new HashMap<>();

    private ObjectFactory(){

        //1.初始化配置对象
        initConfigProperties();

        //2.创建数据源对象
        initDataSource();

        //3.初始化爬虫对象
        initCrawier();

        //4.web对象
        initWebController();

        //5.对象清单打印输出
        printObjectList();
    }


    private void initWebController() {
        DataSource dataSource=getObject(DataSource.class);
        AnalyzeDao analyzeDao=new AnalyzeDaoImpl(dataSource);
        AnalyzeService analyzeService=new AnalyzeServiceImpl(analyzeDao);

        WebController webController=new WebController(analyzeService);

        objectHashMap.put(WebController.class,webController);
    }

    private void initCrawier() {
        ConfigProperties configProperties =getObject(ConfigProperties.class);
        DataSource dataSource=getObject(DataSource.class);

        //构造自己的page页面
        final Page page=new Page(configProperties.getCrawierBase(),
                configProperties.getCrawierPath(),
                configProperties.isCrawierDetail());

        Crawier crawier=new Crawier();
        crawier.addParse(new DataPageParse());
        crawier.addParse(new DocumentParse());
        if(configProperties.isEnableConsole()) {
            crawier.addPipeLine(new ConsolePipeline());
        }
        crawier.addPipeLine(new DatabasePipeline(dataSource));
        /*爬虫的根page*/
        crawier.addPage(page);

        objectHashMap.put(Crawier.class,crawier);
    }

    private void initDataSource() {
        ConfigProperties configProperties =getObject(ConfigProperties.class);
        DruidDataSource dataSource=new DruidDataSource();
        dataSource.setUsername(configProperties.getDbUsername());
        dataSource.setPassword(configProperties.getDbPassword());
        dataSource.setDriverClassName(configProperties.getDbDriverClass());
        dataSource.setUrl(configProperties.getDbUrl());

        objectHashMap.put(DataSource.class,dataSource);
    }

    private void initConfigProperties() {
        ConfigProperties configProperties = new ConfigProperties();

        objectHashMap.put(ConfigProperties.class, configProperties);

        logger.info("ConfigProperties info:\n{}",configProperties.toString());
    }


    public  <T> T  getObject(Class classz){
        if(!objectHashMap.containsKey(classz)){
            throw new IllegalArgumentException("Class"+classz.getName()+"not found Object");
        }
        return (T) objectHashMap.get(classz);
    }

    public static ObjectFactory getInstance(){
        return instance;
    }


    private void printObjectList(){
        logger.info("\n=======================ObjectFactory List========================");
        for(Map.Entry<Class,Object> entry:objectHashMap.entrySet()){
            logger.info(String.format("[%-5s]  ==>  [%s]",
                    /*getCanonicalName---取全名称*/
                    entry.getKey().getCanonicalName(),entry.getValue().getClass().getCanonicalName()));
        }
        logger.info("\n=================================================================\n");

    }
}
