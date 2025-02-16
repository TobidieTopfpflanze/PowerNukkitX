package cn.powernukkitx.bootstrap;

import cn.powernukkitx.bootstrap.cli.*;
import cn.powernukkitx.bootstrap.info.locator.JavaLocator;
import cn.powernukkitx.bootstrap.info.locator.Location;
import cn.powernukkitx.bootstrap.util.ConfigUtils;
import cn.powernukkitx.bootstrap.util.LanguageUtils;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

public final class CLI implements Program {
    public final Timer timer = new Timer();
    private boolean startPNX = true;

    private final Map<String, Component> components = new HashMap<>();

    public CLI() {
        components.put("GraalVMInstall", new GraalVMInstall());
        components.put("AdoptOpenJDKInstall", new AdoptOpenJDKInstall());
        components.put("PrintHelp", new PrintHelp());
        components.put("CheckVersion", new CheckVersion());
        components.put("JavaInstall", new JavaInstall());
        components.put("PNXStart", new PNXStart());
        components.put("UpdatePNX", new UpdatePNX());
        components.put("UpdateLibs", new UpdateLibs());
        components.put("UpdateBootstrap", new UpdateBootstrap());
        components.put("UpdateComponents", new UpdateComponents());
    }

    @Override
    public void exec(String[] args) {
        OptionParser parser = new OptionParser();
        parser.allowsUnrecognizedOptions();
        OptionSpec<Void> helpSpec = parser.accepts("help", LanguageUtils.tr("command.help")).forHelp();
        OptionSpec<String> versionSpec = parser.accepts("version", LanguageUtils.tr("command.version")).withOptionalArg().ofType(String.class);
        OptionSpec<Void> autoRestartSpec = parser.accepts("autoRestart", LanguageUtils.tr("command.autoRestart")).availableUnless(helpSpec, versionSpec);
        OptionSpec<Void> updatePNXSpec = parser.accepts("updatePNX", LanguageUtils.tr("command.updatePNX")).availableUnless(autoRestartSpec);
        OptionSpec<Void> updateLibsSpec = parser.accepts("updateLibs", LanguageUtils.tr("command.updateLibs")).availableUnless(autoRestartSpec);
        OptionSpec<Void> updateBootstrapSpec = parser.accepts("updateBootstrap", LanguageUtils.tr("command.updateBootstrap")).availableUnless(autoRestartSpec);
        OptionSpec<Void> updateComponents = parser.accepts("updateComponents", LanguageUtils.tr("command.updateComponents")).availableUnless(autoRestartSpec);

        // 解析参数
        OptionSet options = parser.parse(args);

        if (options.has(helpSpec)) {
            exec("PrintHelp", parser);
        }
        if (options.has(versionSpec)) {
            exec("CheckVersion", options.valueOf(versionSpec));
        }
        if (options.has(updatePNXSpec)) {
            exec("UpdatePNX");
        }
        if (options.has(updateLibsSpec)) {
            exec("UpdateLibs");
        }
        if (options.has(updateBootstrapSpec)) {
            exec("UpdateBootstrap");
        }
        if (options.has(updateComponents)) {
            exec("UpdateComponents");
        }

        if (startPNX) {
            JavaLocator javaLocator = new JavaLocator("17", true);
            List<Location<JavaLocator.JavaInfo>> result = javaLocator.locate();
            if (result.size() == 0) {
                exec("JavaInstall");
            } else {
                exec("PNXStart", result, options.has(autoRestartSpec) || ConfigUtils.autoRestart());
            }
        }

        // 最终停止timer，退出程序
        timer.cancel();
    }

    public void exec(String componentName, Object... args) {
        if (components.containsKey(componentName)) {
            components.get(componentName).execute(this, args);
        }
    }

    public void setStartPNX(boolean start) {
        this.startPNX = start;
    }
}
