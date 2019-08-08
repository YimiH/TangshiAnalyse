package com.Yimm.TangShiAnalyse.crawier.PipeLine;

import com.Yimm.TangShiAnalyse.crawier.common.Page;

import java.util.Map;

/**
 * Author: sy
 * Create:2019-07-26
 * 17:53
 */
public class ConsolePipeline implements Pipeline {
    @Override
    public void pipeline(final Page page) {
        Map<String,Object> data=page.getDataSet().getData();
        //存储
        System.out.println(data);
    }
}
