package com.recruitsystem.myapplication.data;

import com.recruitsystem.myapplication.data.Bean.JobPostBean;

import java.util.List;

public class JobPostStaticList {
    public static List<JobPostBean> list;

    public static List<JobPostBean> getList() {
        return list;
    }

    public static void setList(List<JobPostBean> list) {
        JobPostStaticList.list = list;
    }

    @Override
    public String toString() {
        return list.toString();
    }
}
