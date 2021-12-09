package me.muffin.oyveyplus.api.utils;

import me.muffin.oyveyplus.api.wrapper.Wrapper;
import org.reflections.Reflections;

import java.util.Set;

public class ReflectionUtil implements Wrapper {

	public static Set<Class<?>> findClasses(String pkg, Class subType) {
		Reflections reflections = new Reflections(pkg);
		return reflections.getSubTypesOf(subType);
	}

}
