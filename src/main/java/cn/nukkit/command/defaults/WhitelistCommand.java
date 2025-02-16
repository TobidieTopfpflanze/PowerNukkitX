package cn.nukkit.command.defaults;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;

/**
 * @author xtypr
 * @since 2015/11/12
 */
public class WhitelistCommand extends VanillaCommand {

    public WhitelistCommand(String name) {
        super(name, "commands.whitelist.description", "commands.whitelist.usage", new String[]{"allowlist"}); // In Minecraft Bedrock v1.18.10 the whitelist was renamed to allowlist
        this.setPermission(
                "nukkit.command.whitelist.reload;" +
                        "nukkit.command.whitelist.enable;" +
                        "nukkit.command.whitelist.disable;" +
                        "nukkit.command.whitelist.list;" +
                        "nukkit.command.whitelist.add;" +
                        "nukkit.command.whitelist.remove;"+
                        //v1.18.10+
                        "nukkit.command.allowlist.reload;" +
                        "nukkit.command.allowlist.enable;" +
                        "nukkit.command.allowlist.disable;" +
                        "nukkit.command.allowlist.list;" +
                        "nukkit.command.allowlist.add;" +
                        "nukkit.command.allowlist.remove"
        );
        this.commandParameters.clear();
        this.commandParameters.put("1arg", new CommandParameter[]{
                CommandParameter.newEnum("action", new CommandEnum("AllowlistAction", "on", "off", "list", "reload"))
        });
        this.commandParameters.put("2args", new CommandParameter[]{
                CommandParameter.newEnum("action", new CommandEnum("AllowlistPlayerAction", "add", "remove")),
                CommandParameter.newType("player", CommandParamType.TARGET)
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        if (args.length == 0 || args.length > 2) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return true;
        }

        if (args.length == 1) {
            if (this.badPerm(sender, args[0].toLowerCase())) {
                return false;
            }
            switch (args[0].toLowerCase()) {
                case "reload":
                    sender.getServer().reloadWhitelist();
                    Command.broadcastCommandMessage(sender, new TranslationContainer("commands.allowlist.reloaded"));

                    return true;
                case "on":
                    sender.getServer().setPropertyBoolean("white-list", true);
                    Command.broadcastCommandMessage(sender, new TranslationContainer("commands.allowlist.enabled"));

                    return true;
                case "off":
                    sender.getServer().setPropertyBoolean("white-list", false);
                    Command.broadcastCommandMessage(sender, new TranslationContainer("commands.allowlist.disabled"));

                    return true;
                case "list":
                    StringBuilder result = new StringBuilder();
                    int count = 0;
                    for (String player : sender.getServer().getWhitelist().getAll().keySet()) {
                        result.append(player).append(", ");
                        ++count;
                    }
                    sender.sendMessage(new TranslationContainer("commands.allowlist.list", String.valueOf(count), String.valueOf(count)));
                    sender.sendMessage(result.length() > 0 ? result.substring(0, result.length() - 2) : "");

                    return true;

                case "add":
                    sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                    return true;

                case "remove":
                    sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                    return true;
            }
        } else if (args.length == 2) {
            if (this.badPerm(sender, args[0].toLowerCase())) {
                return false;
            }
            switch (args[0].toLowerCase()) {
                case "add":
                    sender.getServer().getOfflinePlayer(args[1]).setWhitelisted(true);
                    Command.broadcastCommandMessage(sender, new TranslationContainer("commands.allowlist.add.success", args[1]));

                    return true;
                case "remove":
                    sender.getServer().getOfflinePlayer(args[1]).setWhitelisted(false);
                    Command.broadcastCommandMessage(sender, new TranslationContainer("commands.allowlist.remove.success", args[1]));

                    return true;
            }
        }

        return true;
    }

    private boolean badPerm(CommandSender sender, String perm) {
        if (!sender.hasPermission("nukkit.command.whitelist." + perm) && !sender.hasPermission("nukkit.command.allowlist." + perm)) {
            sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));

            return true;
        }

        return false;
    }
}
