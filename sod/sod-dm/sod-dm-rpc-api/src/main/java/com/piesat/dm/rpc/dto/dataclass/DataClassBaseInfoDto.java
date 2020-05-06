package com.piesat.dm.rpc.dto.dataclass;

import com.piesat.util.BaseDto;
import lombok.Data;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * 资料基础信息
 *
 * @author cwh
 * @date 2020年 04月01日 15:45:12
 */
@Data
public class DataClassBaseInfoDto extends BaseDto {
    private String dataClassId;

    private String cCoremetaId;

    private String cIdabs;

    private String cMainfreq;

    private String cDatascal;

    private String cWestbl;

    private String cEastbl;

    private String cSouthbl;

    private String cNorthbl;

    private String cGeodesc;

    private String cSource;

    private String cNetname;

    private String cNettype;

    private String cObsfreq;

    private String cBegin;

    private String cEnd;

    private String cCreatName;

    private String cChecker;

    private String cRpindname;

    private String cRporgname;

    private String cDocindnname;

    private String useBaseInfo;

    /**
     * 该方法是用于相同对象不同属性值的合并，如果两个相同对象中同一属性都有值，那么sourceBean中的值会覆盖tagetBean重点的值
     * @param sourceBean    被提取的对象bean
     * @param targetBean    用于合并的对象bean
     * @return targetBean,合并后的对象
     */
    public DataClassBaseInfoDto combineSydwCore(DataClassBaseInfoDto sourceBean,DataClassBaseInfoDto targetBean){
        Class sourceBeanClass = sourceBean.getClass();
        Class targetBeanClass = targetBean.getClass();

        Field[] sourceFields = sourceBeanClass.getDeclaredFields();
        Field[] targetFields = targetBeanClass.getDeclaredFields();
        for(int i=0; i<sourceFields.length; i++){
            Field sourceField = sourceFields[i];
            if(Modifier.isStatic(sourceField.getModifiers())){
            continue;
            }
            Field targetField = targetFields[i];
            if(Modifier.isStatic(targetField.getModifiers())){
            continue;
            }
            sourceField.setAccessible(true);
            targetField.setAccessible(true);
            try {
                if( !(sourceField.get(sourceBean) == null) &&  !"serialVersionUID".equals(sourceField.getName().toString())){
                    targetField.set(targetBean,sourceField.get(sourceBean));
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return targetBean;
    }

    public DataClassBaseInfoDto getDiffInfo(DataClassBaseInfoDto sourceBean,DataClassBaseInfoDto targetBean){
        Class sourceBeanClass = sourceBean.getClass();
        Class targetBeanClass = targetBean.getClass();
        DataClassBaseInfoDto dto = new DataClassBaseInfoDto();
        Class outBeanClass = dto.getClass();
        Field[] sourceFields = sourceBeanClass.getDeclaredFields();
        Field[] targetFields = targetBeanClass.getDeclaredFields();
        Field[] declaredFields = outBeanClass.getDeclaredFields();
        for(int i=0; i<sourceFields.length; i++){
            Field sourceField = sourceFields[i];
            if(Modifier.isStatic(sourceField.getModifiers())){
                continue;
            }
            Field targetField = targetFields[i];
            if(Modifier.isStatic(targetField.getModifiers())){
                continue;
            }
            Field declaredField = declaredFields[i];
            if(Modifier.isStatic(declaredField.getModifiers())){
                continue;
            }
            sourceField.setAccessible(true);
            targetField.setAccessible(true);
            declaredField.setAccessible(true);
            try {
                if(sourceField.get(sourceBean) != null && !sourceField.get(sourceBean).equals(targetField.get(targetBean)) &&  !"serialVersionUID".equals(sourceField.getName().toString())){
                    declaredField.set(dto,sourceField.get(sourceBean));
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return dto;
    }

    public static void main(String[] args) {
        DataClassBaseInfoDto d1 = new DataClassBaseInfoDto();
        d1.setCBegin("2121");
        d1.setCChecker("dddd");
        d1.setCCoremetaId("eeeee");
        DataClassBaseInfoDto d2 = new DataClassBaseInfoDto();
        d2.setCBegin("1111");
        d2.setCChecker("2222");
        d2.setCCoremetaId("eeeee");

        System.out.println(d1.getDiffInfo(d1,d2).getCCoremetaId());
    }
}
