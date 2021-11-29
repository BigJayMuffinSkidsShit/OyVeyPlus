package me.muffin.oyveyplus.api.command;

import net.minecraft.util.text.TextFormatting;
import me.muffin.oyveyplus.api.utils.MessageUtil;
import me.muffin.oyveyplus.impl.commands.PrefixCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author fuckyouthinkimboogieman
 */

public class CommandManager {
    private final List<Command> commands;

    private String prefix = ".";

    public CommandManager() {
        commands = new ArrayList<>();
        commands.addAll(Arrays.asList(
                new PrefixCommand()
        ));

        commands.sort(Comparator.comparing(command -> command.getAliases()[0]));
    }

    public List<Command> getCommands() { return commands; }

    public void execute(String message) {
        String noPrefix = message.substring(this.prefix.length());
        Command command = getCommand(noPrefix.split(" ")[0]);
        if (command != null) {
            command.execute(Arrays.copyOfRange(noPrefix.split(" "), 1, noPrefix.split(" ").length));
            return;
        }

        // This will not be executed if command was found, since getCommand() returns null if it cannot find any command.
        MessageUtil.instance.addMessage("Command \"" + noPrefix.split(" ")[0] + TextFormatting.RED + "\" not found!", true);
    }

    public Command getCommand(String name) { return commands.stream().filter(command -> Arrays.stream(command.getAliases()).anyMatch(name::equalsIgnoreCase)).findFirst().orElse(null); }

    public String getPrefix() { return this.prefix; }

    public void setPrefix(String prefix) { this.prefix = prefix; }
}
