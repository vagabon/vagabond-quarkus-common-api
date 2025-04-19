package org.vagabond.engine.crud.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.StringUtils;
import org.vagabond.engine.crud.entity.BaseEntity;

import io.quarkus.logging.Log;

public class EntityUtils {

    private EntityUtils() {
    }

    public static void setEntity(BaseEntity entity1, BaseEntity entity2) {
        setEntity(entity1, entity2, entity1.getClass());
    }

    public static void setEntity(BaseEntity entity1, BaseEntity entity2, Class<?> entityClass) {
        setFields(entity1, entity2, entityClass.getDeclaredFields());
        if (entityClass.getSuperclass() != null) {
            setEntity(entity1, entity2, entityClass.getSuperclass());
        }
    }

    private static void setFields(BaseEntity entity1, BaseEntity entity2, Field[] fields) {
        for (var field : fields) {
            if (!field.getName().contains("_") && !field.getName().contains("$") && !"id".equals(field.getName())
                    && !"serialVersionUID".equals(field.getName()) && !"quarkusSyntheticLogger".equals(field.getName())) {
                Object value = null;
                var getterName = String.format("%s%s", "get", StringUtils.capitalize(field.getName()));
                try {
                    value = entity2.getClass().getMethod(getterName).invoke(entity2);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    Log.error("expcetion EntityUtils.setFields : " + getterName, e);
                }
                doSetValue(entity1, value, field);
            }
        }
    }

    private static void doSetValue(BaseEntity entity, Object value, Field field) {
        var setterName = String.format("%s%s", "set", StringUtils.capitalize(field.getName()));
        try {
            var newValue = value;
            Log.debug("setValue: " + setterName + " " + newValue);
            if (newValue != null) {
                if (value instanceof BaseEntity baseEntity && baseEntity.id.equals(-1L)) {
                    newValue = null;
                }
                entity.getClass().getMethod(setterName, (field.getType())).invoke(entity, newValue);
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Log.error("expcetion EntityUtils.doSetValue : " + setterName);
        }
    }

    public static boolean isObjectDiff(Object first, Object second) {
        if (first == null && second == null) {
            return false;
        } else if (first == null) {
            return true;
        }
        return !first.equals(second);
    }
}