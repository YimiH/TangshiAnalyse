package com.Yimm.TangShiAnalyse;

import com.Yimm.TangShiAnalyse.config.ObjectFactory;
import com.Yimm.TangShiAnalyse.crawier.Crawier;
import com.Yimm.TangShiAnalyse.web.WebController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: sy
 * Create:2019-03-10
 * 19:21
 */
public class TangShiAnalyseApplication {
    //打印日志信息
    private static final Logger LOGGER= LoggerFactory.getLogger(TangShiAnalyseApplication.class);
    public static void main(String[] args) {

        LOGGER.info("唐诗分析程序启动了！！！");
        /*运行web服务，提供接口*/
        WebController webController= ObjectFactory.getInstance().getObject(WebController.class);
        LOGGER.info("Web Server launch...");
        webController.launch();

        /*启动爬虫*/
        if(args.length==1 && args[0].equals("run-crawier")) {
            Crawier crawier = ObjectFactory.getInstance().getObject(Crawier.class);
            LOGGER.info("Crawier start...");
            crawier.start();
        }
    }
}
