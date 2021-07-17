package dansplugins.factionsystem;

import dansplugins.factionsystem.bstats.Metrics;
import dansplugins.factionsystem.data.PersistentData;
import dansplugins.factionsystem.managers.ConfigManager;
import dansplugins.factionsystem.managers.LocaleManager;
import dansplugins.factionsystem.managers.StorageManager;
import dansplugins.factionsystem.placeholders.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class MedievalFactions extends JavaPlugin {

    private static MedievalFactions instance;
    private static MedievalFactionsAPI API;

    private final String version = "v4.2.1";

    public static MedievalFactions getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        API = new MedievalFactionsAPI();

        // create/load config
        if (!(new File("./plugins/MedievalFactions/config.yml").exists())) {
            ConfigManager.getInstance().saveConfigDefaults();
        }
        else {
            // pre load compatibility checks
            if (isVersionMismatched()) {
                ConfigManager.getInstance().handleVersionMismatch();
            }
            reloadConfig();
        }

        // load strings and save files
        LocaleManager.getInstance().loadStrings();
        StorageManager.getInstance().load();

        // schedule recurring tasks
        Scheduler.getInstance().schedulePowerIncrease();
        Scheduler.getInstance().schedulePowerDecrease();
        Scheduler.getInstance().scheduleAutosave();

        // register events
        EventRegistry.getInstance().registerEvents();

        // make sure every player experiences power decay in case we updated from pre-v3.5
        PersistentData.getInstance().createActivityRecordForEveryOfflinePlayer();

        // bStats
        int pluginId = 8929;
        Metrics metrics = new Metrics(this, pluginId);

        // Dynmap
        if (DynmapIntegrator.hasDynmap()) {
            DynmapIntegrator.getInstance().scheduleClaimsUpdate(600); // Check once every 30 seconds for updates.
            DynmapIntegrator.getInstance().updateClaims();
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPI().register();
        }

    }

    @Override
    public void onDisable() {
        StorageManager.getInstance().save();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return CommandInterpreter.getInstance().interpretCommand(sender, label, args);
    }

    public String getVersion() {
        return version;
    }

    public boolean isVersionMismatched() {
        return !getConfig().getString("version").equalsIgnoreCase(getVersion());
    }

    public MedievalFactionsAPI getAPI() {
        return API;
    }

}
