package com.piesat.dm.rpc.service.dataclass;

import com.alibaba.fastjson.JSONArray;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.common.tree.BaseParser;
import com.piesat.dm.common.tree.TreeLevel;
import com.piesat.dm.dao.dataclass.DatumTypeInfoDao;
import com.piesat.dm.entity.dataclass.DatumTypeInfoEntity;
import com.piesat.dm.rpc.api.dataclass.DatumTypeInfoService;
import com.piesat.dm.rpc.dto.dataclass.DatumTypeInfoDto;
import com.piesat.dm.rpc.mapper.dataclass.DatumTypeInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 公共元数据
 *
 * @author cwh
 * @date 2019年 11月29日 10:03:19
 */
@Service
public class DatumTypeInfoServiceImpl extends BaseService<DatumTypeInfoEntity> implements DatumTypeInfoService {
    @Autowired
    private DatumTypeInfoDao datumTypeInfoDao;
    @Autowired
    private DatumTypeInfoMapper datumTypeInfoMapper;


    @Override
    public BaseDao<DatumTypeInfoEntity> getBaseDao() {
        return this.datumTypeInfoDao;
    }

    @Override
    public DatumTypeInfoDto saveDto(DatumTypeInfoDto datumTypeInfoDto) {
        DatumTypeInfoEntity datumTypeInfoEntity = this.datumTypeInfoMapper.toEntity(datumTypeInfoDto);
        datumTypeInfoEntity = this.saveNotNull(datumTypeInfoEntity);
        return this.datumTypeInfoMapper.toDto(datumTypeInfoEntity);
    }

    @Override
    public List<DatumTypeInfoDto> all() {
        List<DatumTypeInfoEntity> all = this.getAll();
        return this.datumTypeInfoMapper.toDto(all);
    }

    @Override
    public JSONArray getTree() {
        String sql = "SELECT C_DATUM_CODE ID,C_DATUMTYPE as NAME,C_DATUMPARENT_CODE PID,  " +
                " (SELECT DISTINCT 'el-icon-tickets' FROM T_SOD_DATA_CLASS C INNER JOIN " +
                "T_SOD_DATA_LOGIC CA ON C.DATA_CLASS_ID = CA.DATA_CLASS_ID " +
                " WHERE C.D_DATA_ID = C_DATUM_CODE AND C.TYPE = 2  ) ICON " +
                " FROM TAB_OMIN_CM_CC_DATUMTYPEINFO A " +
                " WHERE  (SUBSTR(C_DATUM_CODE,13,1) NOT IN ('P','R') AND C_DATUMPARENT_CODE != 'G.0019.0001' " +
                " AND C_DATUMPARENT_CODE != 'G.0019.0002' AND EXISTS( SELECT 1 FROM TAB_OMIN_CM_CC_DATUMTYPEINFO B " +
                "WHERE B.C_DATUM_CODE = A.C_DATUMPARENT_CODE)) " +
                " OR C_DATUMPARENT_CODE = '1' ORDER BY C_DATUM_CODE";
        List<Map<String, Object>> maps = this.queryByNativeSQL(sql);
        List l = new ArrayList();
        for (Map<String, Object> m : maps) {
            TreeLevel tl = new TreeLevel();
            tl.setId(m.get("ID").toString());
            tl.setParentId(m.get("PID").toString());
            tl.setName(m.get("NAME").toString());
            if (m.get("ICON") == null) {
                tl.setType("1");
                tl.setIcon("");
            } else {
                tl.setType("0");
                tl.setIcon(m.get("ICON").toString());
            }
            l.add(tl);
        }
        return JSONArray.parseArray(BaseParser.parserListToLevelTree(l));
    }

    @Override
    public JSONArray getSimpleTree() {
        String sql = "SELECT C_DATUM_CODE ID,C_DATUMTYPE NAME,C_DATUMPARENT_CODE PID  " +
                " FROM TAB_OMIN_CM_CC_DATUMTYPEINFO A " +
                " WHERE  (SUBSTR(C_DATUM_CODE,13,1) NOT IN ('P','R') AND C_DATUMPARENT_CODE != 'G.0019.0001' " +
                " AND C_DATUMPARENT_CODE != 'G.0019.0002' AND EXISTS( SELECT 1 FROM TAB_OMIN_CM_CC_DATUMTYPEINFO B " +
                "WHERE B.C_DATUM_CODE = A.C_DATUMPARENT_CODE)) " +
                " OR C_DATUMPARENT_CODE = 1 ORDER BY C_DATUM_CODE";
        List<Map<String, Object>> maps = this.queryByNativeSQL(sql);
        List l = new ArrayList();
        for (Map<String, Object> m : maps) {
            TreeLevel tl = new TreeLevel();
            tl.setId(m.get("ID").toString());
            tl.setParentId(m.get("PID").toString());
            tl.setName(m.get("NAME").toString());
            if (m.get("ICON") == null) {
                tl.setType("1");
                tl.setIcon("");
            } else {
                tl.setType("0");
                tl.setIcon(m.get("ICON").toString());
            }
            l.add(tl);
        }
        return JSONArray.parseArray(BaseParser.parserListToLevelTree(l));
    }

    @Override
    public DatumTypeInfoDto getDotById(String id) {
        DatumTypeInfoEntity datumTypeInfoEntity = this.getById(id);
        return this.datumTypeInfoMapper.toDto(datumTypeInfoEntity);
    }
}
