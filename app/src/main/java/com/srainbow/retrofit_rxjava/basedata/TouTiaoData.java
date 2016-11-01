package com.srainbow.retrofit_rxjava.basedata;

import java.util.List;

/**
 * Created by SRainbow on 2016/9/29.
 */
public class TouTiaoData {
    public String reason;
    public String error_code;
    public result result;

    public class result{
        public String state;
        public List<TouTiaoDetail>data;
    }
}
