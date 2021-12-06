package me.muffin.oyveyplus.impl.commands;

import me.muffin.oyveyplus.OyVeyPlus;
import me.muffin.oyveyplus.api.command.Command;
import me.muffin.oyveyplus.api.utils.MessageUtil;

/**
 * @author fuckyouthinkimboogieman
 */

public class PrefixCommand extends Command {
    public PrefixCommand() {
        super(new String[] { "prefix", "p" }, "Set the prefix to whatever you want.", "prefix <char>");
    }

    @Override public void execute(String[] args) {
        if (args.length == 0) {
            MessageUtil.instance.addMessage("Not enough arguments! Usage: " + getUsage(), true);
            return;
        }

        OyVeyPlus.commandManager.setPrefix(args[0]);
        MessageUtil.instance.addMessage("Prefix was set to " + OyVeyPlus.commandManager.getPrefix() + "!", true);
    }
}
