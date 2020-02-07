package com.piesat.schedule.mapper.test;

import com.piesat.schedule.entity.test.Test;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-07 11:28
 **/
@Component
public interface TestMapper {
    List<Test> findList();
}

