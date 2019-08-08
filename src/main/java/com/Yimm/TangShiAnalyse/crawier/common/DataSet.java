package com.Yimm.TangShiAnalyse.crawier.common;

import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: sy
 * Create:2019-06-14
 * 11:24
 */


/*存储清洗数据集合*/
    @ToString
public class DataSet {

    /*
    * data:将DOM解析，清洗之后存储的数据--->目标数据
    * 比如：
    * KEY   VALUE
    * 标题：送孟浩然之广陵
    * 作者：李白
    * 正文：xX
    * */
    private Map<String,Object> data=new HashMap<>();

    public void putData(String key,Object value){
        this.data.put(key, value);
    }

    public Object getData(String key){
        return this.data.get(key);
    }

    public Map<String,Object> getData(){
        /*安全起见，不叫当前data直接返回*/
        return new HashMap<>(this.data);
    }
}
