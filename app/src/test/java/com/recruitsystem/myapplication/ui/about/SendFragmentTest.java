package com.recruitsystem.myapplication.ui.about;

import org.junit.Test;

import java.util.Random;

/**
 * @author 威威君
 * @date 2020/2/26 10:49
 * QQ: 1214585092
 * WeChat:wxid508133793
 * E-mail: 1214585092@qq.com
 * GitHub: https://github.com/huicunjun
 */

public class SendFragmentTest {
    public static void main(String[] args) {
        System.out.println( new Random().nextInt(2));
    }
    @Test
    public  void getInstance2342() {
        for (int i = 0; i < 99; i++) {
            System.out.println( new Random().nextInt(3));
        }
        System.out.println( new Random().nextInt(2));
    }
}