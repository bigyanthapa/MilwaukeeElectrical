package com.milwaukeetool.mymilwaukee.util;

/**
 * Created by cent146 on 12/17/14.
 */
public class NamedObject<T> {
    public final String name;
    public final T object;

    public NamedObject(String name, T object) {
        this.name = name;
        this.object = object;
    }
}
