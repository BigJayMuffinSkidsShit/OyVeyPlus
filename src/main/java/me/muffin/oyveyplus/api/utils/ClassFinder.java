package me.muffin.oyveyplus.api.utils;

import java.util.Set;

import org.reflections.Reflections;

public class ClassFinder {

    public static Set<Class> findClasses(String pack, Class subType) {
        Reflections reflections = new Reflections(pack);
        return reflections.getSubTypesOf(subType);
    }
}
