package me.muffin.oyveyplus.impl.modules.movement;

import me.muffin.oyveyplus.api.module.Module;

/**
 * @author fuckyouthinkimboogieman
 */

public class Sprint extends Module {

    public Sprint() {
        super("Sprint", "You can't press control? Whats next...", Category.Movement);
    }

    public void onUpdate() { mc.player.setSprinting(true); }
}
