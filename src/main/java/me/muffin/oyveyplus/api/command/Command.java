package me.muffin.oyveyplus.api.command;

/**
 * @author fuckyouthinkimboogieman
 */

public abstract class Command {
    private final String[] aliases;
    private final String description;
    private final String usage;

    public Command(String[] aliases, String description, String usage) {
        super();
        this.aliases = aliases;
        this.description = description;
        this.usage = usage;
    }

    public abstract void execute(String[] args);

    public String[] getAliases() { return aliases; }

    public String getDescription() { return description; }

    public String getUsage() { return usage; }
}
