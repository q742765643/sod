package com.piesat.schedule.rpc.api.move;

import com.piesat.schedule.rpc.dto.move.MoveDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;
import java.util.Map;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-24 17:16
 **/
public interface MoveService {
    public PageBean selectMoveList(PageForm<MoveDto> pageForm);

    public MoveDto selectmoveByParam(String databaseId, String dataClassId);

    public MoveDto findMoveById(String moveId);

    public void saveMove(MoveDto moveDto);

    public void updateMove(MoveDto moveDto);

    public void deleteMoveByIds(String[] moveIds);

    public List<Map<String,Object>> findDatabase();

    public List<Map<String,Object>> findDataClassId(String dataBaseId,String dataClassId);

    public void exportExcel(MoveDto moveDto);

    public List<MoveDto> findByDataClassId(String dataClassId);
}

