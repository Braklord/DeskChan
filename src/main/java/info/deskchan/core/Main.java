package info.deskchan.core;

public class Main {

    public static void main(String[] args) {
        PluginManager pluginManager = PluginManager.getInstance();
        pluginManager.initialize(args);

        // don't change this order, it's very important
        pluginManager.tryLoadPluginByPackageName("info.deskchan.core_utils");
        if(!CoreInfo.getCoreProperties().getBoolean("terminal", false)) {
            pluginManager.tryLoadPluginByPackageName("info.deskchan.gui_javafx");
        }
        pluginManager.tryLoadPluginByPackageName("info.deskchan.jar_loader");
        pluginManager.tryLoadPluginByPackageName("info.deskchan.groovy_support");
        pluginManager.tryLoadPluginByPackageName("info.deskchan.external_loader");
        pluginManager.tryLoadPluginByPackageName("info.deskchan.talking_system");
        try {
            LoaderManager.INSTANCE.loadPlugins();

        } catch (Exception e) {
            PluginManager.log(e);
        }
        PluginManager.getInstance().sendMessage("core", "core-events:loading-complete", null);
    }
}