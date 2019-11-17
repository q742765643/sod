package com.piesat.common.jpa.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-11-17 18:18
 **/
@MappedSuperclass
@Data
public class UUIDEntity implements Serializable
{
    @Id
    @Column(length = 32)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "com.piesat.common.jpa.generator")
    private String id;

}

