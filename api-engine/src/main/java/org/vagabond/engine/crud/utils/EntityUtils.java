package org.vagabond.engine.crud.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import io.quarkus.logging.Log;
import org.apache.commons.lang3.StringUtils;
import org.vagabond.engine.crud.entity.BaseEntity;

public class EntityUtils {

    private EntityUtils() {
    }

    public static void setEntity(BaseEntity entity1, BaseEntity entity2) {
        setFields(entity1, entity2, entity1.getClass().getDeclaredFields());
        if (entity1.getClass().getSuperclass() != null) {
            setFields(entity1, entity2, entity1.getClass().getSuperclass().getDeclaredFields());
            if (entity1.getClass().getSuperclass().getSuperclass() != null) {
                setFields(entity1, entity2, entity1.getClass().getSuperclass().getSuperclass().getDeclaredFields());
                if (entity1.getClass().getSuperclass().getSuperclass().getSuperclass() != null) {
                    setFields(entity1, entity2, entity1.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredFields());
                }
            }
        }
    }

    private static void setFields(BaseEntity entity1, BaseEntity entity2, Field[] fields) {
        for (var field : fields) {
            if (!field.getName().contains("_") && !field.getName().contains("$") && !"id".equals(field.getName())
                    && !"serialVersionUID".equals(field.getName()) && !"quarkusSyntheticLogger".equals(field.getName())) {
                Object value = null;
                var prefixe = "get";
                var getterName = prefixe + StringUtils.capitalize(field.getName());
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
        var setterName = "set" + StringUtils.capitalize(field.getName());
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
}