package cn.nukkit.command.defaults;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.lang.TranslationContainer;

/**
 * @author xtypr
 * @since 2015/11/13
 */
public class SaveOffCommand extends VanillaCommand {

    public SaveOffCommand(String name) {
        super(name, "commands.saveoff.description", "commands.save-off.usage");
        this.setPermission("nukkit.command.save.disable");
        this.commandParameters.clear();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        sender.getServer().setAutoSave(false);
        Command.broadcastCommandMessage(sender, new TranslationContainer("commands.save.disabled"));
        return true;
    }
}
