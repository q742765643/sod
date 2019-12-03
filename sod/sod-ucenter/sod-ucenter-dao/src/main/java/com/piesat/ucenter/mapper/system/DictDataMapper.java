package com.piesat.ucenter.mapper.system;

import com.piesat.ucenter.entity.system.DictDataEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/12/3 17:42
 */
@Component
public interface DictDataMapper {
    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    public List<DictDataEntity> selectDictDataList(DictDataEntity dictData);
}
