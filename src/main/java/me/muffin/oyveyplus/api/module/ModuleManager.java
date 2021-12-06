package me.muffin.oyveyplus.api.module;

import me.muffin.oyveyplus.api.utils.ClassFinder;
import me.muffin.oyveyplus.api.wrapper.Wrapper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author fuckyouthinkimboogieman
 */

public class ModuleManager implements Wrapper {

    private final List<Module> modules;

    public ModuleManager() {
        modules = new ArrayList<>();

        Set<Class> classList = ClassFinder.findClasses("me.muffin.oyveyplus.impl.modules", Module.class);
        classList.forEach(aClass -> {
            try {
                Module module = (Module) aClass.getConstructor().newInstance();
                modules.add(module);
            } catch (InvocationTargetException e) {
                e.getCause().printStackTrace();
                System.err.println("Couldn't initiate module " + aClass.getSimpleName() + "! Err: " + e.getClass().getSimpleName() + ", message: " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Couldn't initiate module " + aClass.getSimpleName() + "! Err: " + e.getClass().getSimpleName() + ", message: " + e.getMessage());
            }
        });
        System.out.println("Modules Initialized ");
        modules.sort(Comparator.comparing(Module::getName));
    }

    public List<Module> getModules() { return modules; }

    public List<Module> getModules(Module.Category category) { return modules.stream().filter(module -> module.getCategory() == category).collect(Collectors.toList()); }

    public Module getModule(String name) { return modules.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null); }

}
