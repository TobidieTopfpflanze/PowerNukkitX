package cn.nukkit.command.defaults;

import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.lang.TranslationContainer;

/**
 * @author xtypr
 * @since 2015/11/12
 */
public class DefaultGamemodeCommand extends VanillaCommand {

    public DefaultGamemodeCommand(String name) {
        super(name, "commands.defaultgamemode.description", "commands.defaultgamemode.usage");
        this.setPermission("nukkit.command.defaultgamemode");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                CommandParameter.newType("gameMode", CommandParamType.INT)
        });
        this.commandParameters.put("byString", new CommandParameter[]{
                CommandParameter.newEnum("gameMode", CommandEnum.ENUM_GAMEMODE)
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (args.length == 0) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", new String[]{this.usageMessage}));
            return false;
        }
        int gameMode = Server.getGamemodeFromString(args[0]);
        if (gameMode != -1) {
            sender.getServer().setPropertyInt("gamemode", gameMode);
            sender.sendMessage(new TranslationContainer("commands.defaultgamemode.success", new String[]{Server.getGamemodeString(gameMode)}));
        } else {
            sender.sendMessage("Unknown game mode");
        }
        return true;
    }
}
