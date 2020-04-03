package org.util;

import com.github.pagehelper.PageHelper;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-04-01 15:33
 **/
public class PageUtil {

    public static void startPage(int pageNum, int pageSize){
        PageHelper.startPage(pageNum,pageSize);
    }
}

