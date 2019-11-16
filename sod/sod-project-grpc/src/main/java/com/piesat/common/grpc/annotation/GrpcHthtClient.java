package com.piesat.common.grpc.annotation;

import java.lang.annotation.*;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/13 15:51
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GrpcHthtClient {
}
