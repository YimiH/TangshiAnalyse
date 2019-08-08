package com.Yimm.TangShiAnalyse.analyse.service;

import com.Yimm.TangShiAnalyse.analyse.model.AuthorCount;
import com.Yimm.TangShiAnalyse.analyse.model.WordCount;

import java.util.List;

/**
 * Author: sy
 * Create:2019-07-30
 * 12:49
 */
public interface AnalyzeService {

    /*分析唐诗中作者的创作数量*/
    List<AuthorCount> analyzeAuthorCount();

    /*词云分析*/
    List<WordCount> analyzeWordCount();
}
