package com.piesat.dm.common.tree;

import java.util.List;

/**
 * 解析数据
 *
 * @author cwh
 * @date 2020年 01月06日 15:36:07
 */
public interface ITreeParser<T> {
    /**
     * 解析数据
     * @param data
     * @return
     */
    List<T> parser(Object data);
}
