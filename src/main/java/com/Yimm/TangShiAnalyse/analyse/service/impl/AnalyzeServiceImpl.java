package com.Yimm.TangShiAnalyse.analyse.service.impl;

import com.Yimm.TangShiAnalyse.analyse.dao.AnalyzeDao;
import com.Yimm.TangShiAnalyse.analyse.entity.PoetryInfo;
import com.Yimm.TangShiAnalyse.analyse.model.AuthorCount;
import com.Yimm.TangShiAnalyse.analyse.model.WordCount;
import com.Yimm.TangShiAnalyse.analyse.service.AnalyzeService;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

import java.util.*;

/**
 * Author: sy
 * Create:2019-07-30
 * 12:53
 */
public class AnalyzeServiceImpl implements AnalyzeService {
    /*业务层依赖查询层*/
    private  final AnalyzeDao analyzeDao;

    public AnalyzeServiceImpl(AnalyzeDao analyzeDao) {
        this.analyzeDao = analyzeDao;
    }

    @Override
    public List<AuthorCount> analyzeAuthorCount() {
        //此处结果并未排序
        /*排序方式
        * 1.DAO层SQL排序
        * 2.Service层进行排序---此处按照count升序*/
        List<AuthorCount> authorCounts=analyzeDao.analyzeAuthorCount();
        authorCounts.sort(
                Comparator.comparing(AuthorCount::getCount));
        return authorCounts;
    }

    @Override
    public List<WordCount> analyzeWordCount() {
        /*1.查询出所有数据
        * 2.取出title以及内容
        * 3.分词  考虑：过滤词性为w,null,空，len<2
        * 4.统计 k-v k:词语 v:词频*/

        Map<String,Integer> map=new HashMap<>();
        List<PoetryInfo> poetryInfos=analyzeDao.queryAllPoetryInfo();

        for(PoetryInfo poetryInfo:poetryInfos){
            List<Term> terms=new ArrayList<>();
            String title=poetryInfo.getTitle();
            String content=poetryInfo.getContent();

            terms.addAll(NlpAnalysis.parse(title).getTerms());
            terms.addAll(NlpAnalysis.parse(content).getTerms());

            Iterator<Term> iterator = terms.iterator();
            while(iterator.hasNext()){
                Term term=iterator.next();
                /*词性的过滤*/
                if(term.getNatureStr()==null || term.getNatureStr().equals("w")){
                    iterator.remove();
                    continue;
                }

                /*词的过滤--过滤长度<2的词语*/
                if(term.getRealName().length()<2){
                    iterator.remove();
                    continue;
                }

                //统计
                String realName=term.getRealName();
                int count;
                if(map.containsKey(realName)){
                    count=map.get(realName)+1;
                }else{
                    count=1;
                }
                map.put(realName,count);
            }
        }

        /*统计结果转换为链表*/
        List<WordCount> wordCounts=new ArrayList<>();
        for(Map.Entry<String,Integer> entry:map.entrySet()){
            WordCount wordCount=new WordCount();
            wordCount.setWord(entry.getKey());
            wordCount.setCount(entry.getValue());
            wordCounts.add(wordCount);
        }
        return wordCounts;
    }
}
