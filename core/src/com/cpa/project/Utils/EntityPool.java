package com.cpa.project.Utils;

import com.badlogic.gdx.utils.Pool;
import com.cpa.project.Entities.Entity;

import java.lang.reflect.InvocationTargetException;

public class EntityPool<T extends Entity> extends Pool<T> {
    private final Class<T> type;

    public EntityPool(Class<T> type) {
        this.type = type;
    }

    @Override
    protected T newObject() {
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Cannot create instance of " + type, e);
        }
    }
}

