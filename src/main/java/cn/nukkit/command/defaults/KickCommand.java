package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.event.player.PlayerKickEvent;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;

/**
 * @author xtypr
 * @since 2015/11/11
 */
public class KickCommand extends VanillaCommand {

    public KickCommand(String name) {
        super(name, "commands.kick.description", "commands.kick.usage");
        this.setPermission("nukkit.command.kick");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                CommandParameter.newType("player", CommandParamType.TARGET),
                CommandParameter.newType("reason", true, CommandParamType.MESSAGE)
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (args.length == 0) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }

        String name = args[0];

        StringBuilder reason = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            reason.append(args[i]).append(" ");
        }

        if (reason.length() > 0) {
            reason = new StringBuilder(reason.substring(0, reason.length() - 1));
        }

        Player player = sender.getServer().getPlayer(name);
        if (player != null) {
            player.kick(PlayerKickEvent.Reason.KICKED_BY_ADMIN, reason.toString());
            if (reason.length() >= 1) {
                Command.broadcastCommandMessage(sender, new TranslationContainer("commands.kick.success.reason", player.getName(), reason.toString())
                );
            } else {
                Command.broadcastCommandMessage(sender, new TranslationContainer("commands.kick.success", player.getName()));
            }
        } else {
            sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.player.notFound"));
        }

        return true;
    }
}
