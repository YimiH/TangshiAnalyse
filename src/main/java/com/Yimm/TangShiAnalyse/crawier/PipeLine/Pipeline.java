package com.Yimm.TangShiAnalyse.crawier.PipeLine;

import com.Yimm.TangShiAnalyse.crawier.common.Page;

/**
 * Author: sy
 * Create:2019-06-14
 * 11:11
 */

/*清洗*/
public interface Pipeline {
    /*解析后的page传入进行页面清洗*/
    void pipeline(final Page page);
}
