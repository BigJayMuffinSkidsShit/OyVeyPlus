package me.muffin.oyveyplus.impl.mixin.mixins.accessors;

import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(KeyBinding.class)
public interface AccessorKeybinding {
    @Accessor
    int getKeyCode();

    @Invoker("isKeyDown")
    boolean isKeyDown();
}
