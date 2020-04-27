package com.recruitsystem.myapplication.data;

import com.recruitsystem.myapplication.data.Bean.JobPostBean;

import java.util.ArrayList;
import java.util.List;

public class StaticJobListInfoData {
    //全局的数据
    public static List<JobPostBean> list = new ArrayList<>();
    public static List<JobPostBean> bossList = new ArrayList<>();
    public static List<JobPostBean> collectedList = new ArrayList<>();
    public static List<JobPostBean> bykeysearchList = new ArrayList<>();
    public static int oldCollectedCount = -1 ;
    public static int newCollectedCount;
    public static String collectionCount;


}
