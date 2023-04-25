package com.zfsoft.platform.utils.validate;


import com.google.common.collect.Sets;
import com.zfsoft.platform.utils.util.ExceptHandle;
import com.zfsoft.platform.utils.util.OptionalUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

/**
 * @Auther: lijun@sstcsoft.com
 * @Date: 2019/9/11 15:06
 *
 * @modify: kkfan   <br>
 *      @1.1 支持手动校验 <br>
 *      @1.2 修改规则 若传入分组默认分组包含默认分组 {@link javax.validation.groups.Default}  <br>
 *      @1.3 根据id是否存在自动判断是更新还是插入校验  <br>
 *          不可自定义分组名称，需配合 {@link ValidGroups.INSERT} {@link ValidGroups.UPDATA} 使用  <br>
 * @Date: 2021/10/22 14:06
 */
public class ValidationUtils {
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> String validateEntity(T obj, Class ...groups) {
        groups = Optional.ofNullable((groups.length > 0) ? groups : null).orElseGet(
                () -> OptionalUtils.resolve(
                        ExceptHandle.handlingSupplierWrapper(
                                () -> {
                                    Field field = obj.getClass().getDeclaredField("id");
                                    field.setAccessible(true);
                                    return field.get(obj);
                                }
                        )).isPresent() ? new Class[]{ValidGroups.UPDATA.class} : new Class[]{ValidGroups.INSERT.class}
        );
        return validateEntity0(obj, groups);
    }

    public static <T> String validateEntity0(T obj,Class ...groups){
        StringBuilder sb = new StringBuilder();
        HashSet<Class> classes = Sets.newHashSet(groups);
        classes.add(Default.class);
        Set<ConstraintViolation<T>> constraintViolationSet = validator.validate(obj, classes.toArray(new Class<?>[classes.size()]));
        Iterator<ConstraintViolation<T>> constraintViolationIterator = constraintViolationSet.iterator();
        while (constraintViolationIterator.hasNext()){
            ConstraintViolation<T> constraintViolation = constraintViolationIterator.next();
            //sb.append(constraintViolation.getPropertyPath());
            //sb.append(":");
            sb.append(constraintViolation.getMessage());
            sb.append("\r\n");
        }
        return sb.toString();
    }

    public static <T> String validateProperty(T obj, String propertyName,Class ...groups){
        StringBuilder sb = new StringBuilder();
        HashSet<Class> classes = Sets.newHashSet(groups);
        classes.add(Default.class);
        Set<ConstraintViolation<T>> set = validator.validateProperty(obj, propertyName, classes.toArray(new Class<?>[classes.size()]));
        Iterator<ConstraintViolation<T>> constraintViolationIterator = set.iterator();
        while (constraintViolationIterator.hasNext()){
            ConstraintViolation<T> constraintViolation = constraintViolationIterator.next();
            sb.append(constraintViolation.getPropertyPath());
            sb.append(":");
            sb.append(constraintViolation.getMessage());
            sb.append("\r\n");
        }
        return sb.toString();
    }

    public static <T> void validateEntityPattern(T obj,Class ...groups){
        String msg = validateEntity(obj, groups);
        if(msg.length()>0){
            throw new ParamValidException(msg);
        }
    }

    public static <T> void validatePropertyPattern(T obj, String propertyName,Class ...groups) {
        String msg = validateProperty(obj, propertyName, groups);
        if(msg.length()>0){
            throw new ParamValidException(msg);
        }
    }

}
