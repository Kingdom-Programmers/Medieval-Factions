package dansplugins.factionsystem.managers;

import dansplugins.factionsystem.MedievalFactions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ConfigManager {

    private static ConfigManager instance;
    private boolean altered = false;

    private ConfigManager() {

    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    public void handleVersionMismatch() {
        // set version
        if (!MedievalFactions.getInstance().getConfig().isString("version")) {
            MedievalFactions.getInstance().getConfig().addDefault("version", MedievalFactions.getInstance().getVersion());
        }
        else {
            MedievalFactions.getInstance().getConfig().set("version", MedievalFactions.getInstance().getVersion());
        }

        // add defaults if they don't exist
        if (!MedievalFactions.getInstance().getConfig().isInt("initialMaxPowerLevel")) {
            MedievalFactions.getInstance().getConfig().addDefault("initialMaxPowerLevel", 20);
        }
        if (!MedievalFactions.getInstance().getConfig().isInt("initialPowerLevel")) {
            MedievalFactions.getInstance().getConfig().addDefault("initialPowerLevel", 5);
        }
        if (!MedievalFactions.getInstance().getConfig().isBoolean("mobsSpawnInFactionTerritory")) {
            MedievalFactions.getInstance().getConfig().addDefault("mobsSpawnInFactionTerritory", false);
        }
        if (!MedievalFactions.getInstance().getConfig().isInt("powerIncreaseAmount")) {
            MedievalFactions.getInstance().getConfig().addDefault("powerIncreaseAmount", 2);
        }
        if (!MedievalFactions.getInstance().getConfig().isBoolean("laddersPlaceableInEnemyFactionTerritory")) {
            MedievalFactions.getInstance().getConfig().addDefault("laddersPlaceableInEnemyFactionTerritory", true);
        }
        if (!MedievalFactions.getInstance().getConfig().isInt("minutesBeforeInitialPowerIncrease")) {
            MedievalFactions.getInstance().getConfig().addDefault("minutesBeforeInitialPowerIncrease", 30);
        }
        if (!MedievalFactions.getInstance().getConfig().isInt("minutesBetweenPowerIncreases")) {
            MedievalFactions.getInstance().getConfig().addDefault("minutesBetweenPowerIncreases", 60);
        }
        if (!MedievalFactions.getInstance().getConfig().isBoolean("warsRequiredForPVP")) {
            MedievalFactions.getInstance().getConfig().addDefault("warsRequiredForPVP", true);
        }
        if (!MedievalFactions.getInstance().getConfig().isDouble("factionOwnerMultiplier")) {
            MedievalFactions.getInstance().getConfig().addDefault("factionOwnerMultiplier", 2.0);
        }
        if (!MedievalFactions.getInstance().getConfig().isDouble("officerPerMemberCount")){
            MedievalFactions.getInstance().getConfig().addDefault("officerPerMemberCount", 5);
        }
        if (!MedievalFactions.getInstance().getConfig().isDouble("factionOfficerMultiplier")){
            MedievalFactions.getInstance().getConfig().addDefault("factionOfficerMultiplier", 1.5);
        }
        if (!MedievalFactions.getInstance().getConfig().isBoolean("powerDecreases")) {
        	MedievalFactions.getInstance().getConfig().addDefault("powerDecreases", true);
        }
        if (!MedievalFactions.getInstance().getConfig().isInt("minutesBetweenPowerDecreases")) {
        	MedievalFactions.getInstance().getConfig().addDefault("minutesBetweenPowerDecreases", 1440);
        }
        if (!MedievalFactions.getInstance().getConfig().isInt("minutesBeforePowerDecrease")) {
        	MedievalFactions.getInstance().getConfig().addDefault("minutesBeforePowerDecrease", 20160);
        }
        if (!MedievalFactions.getInstance().getConfig().isInt("powerDecreaseAmount")) {
            MedievalFactions.getInstance().getConfig().addDefault("powerDecreaseAmount", 1);
        }
        if (!MedievalFactions.getInstance().getConfig().isInt("factionMaxNameLength")) {
            MedievalFactions.getInstance().getConfig().addDefault("factionMaxNameLength", 20);
        }
        if (!MedievalFactions.getInstance().getConfig().isInt("factionMaxNumberGates")) {
            MedievalFactions.getInstance().getConfig().addDefault("factionMaxNumberGates", 5);
        }
        if (!MedievalFactions.getInstance().getConfig().isInt("factionMaxGateArea")) {
            MedievalFactions.getInstance().getConfig().addDefault("factionMaxGateArea", 64);
        }
        if (!MedievalFactions.getInstance().getConfig().isBoolean("surroundedChunksProtected")) {
            MedievalFactions.getInstance().getConfig().addDefault("surroundedChunksProtected", true);
        }
        if (!MedievalFactions.getInstance().getConfig().isBoolean("zeroPowerFactionsGetDisbanded")) {
            MedievalFactions.getInstance().getConfig().addDefault("zeroPowerFactionsGetDisbanded", false);
        }
        if (!MedievalFactions.getInstance().getConfig().isDouble("vassalContributionPercentageMultiplier")) {
            MedievalFactions.getInstance().getConfig().addDefault("vassalContributionPercentageMultiplier", 0.75);
        }
        if (!MedievalFactions.getInstance().getConfig().isBoolean("nonMembersCanInteractWithDoors")) {
            MedievalFactions.getInstance().getConfig().addDefault("nonMembersCanInteractWithDoors", false);
        }
        if (!MedievalFactions.getInstance().getConfig().isBoolean("playersChatWithPrefixes")) {
            MedievalFactions.getInstance().getConfig().addDefault("playersChatWithPrefixes", true);
        }
        if (!MedievalFactions.getInstance().getConfig().isInt("maxClaimRadius")) {
            MedievalFactions.getInstance().getConfig().addDefault("maxClaimRadius", 3);
        }
        if (!MedievalFactions.getInstance().getConfig().isString("languageid")) {
            MedievalFactions.getInstance().getConfig().addDefault("languageid", "en-us");
        }
        if (!MedievalFactions.getInstance().getConfig().isBoolean("chatSharedInVassalageTrees")) {
            MedievalFactions.getInstance().getConfig().addDefault("chatSharedInVassalageTrees", true);
        }
        if (!MedievalFactions.getInstance().getConfig().isBoolean("allowAllyInteraction")) {
            MedievalFactions.getInstance().getConfig().addDefault("allowAllyInteraction", false);
        }
        if (!MedievalFactions.getInstance().getConfig().isBoolean("allowVassalageTreeInteraction")) {
            MedievalFactions.getInstance().getConfig().addDefault("allowVassalageTreeInteraction", false);
        }
        if (!MedievalFactions.getInstance().getConfig().isString("factionChatColor")) {
            MedievalFactions.getInstance().getConfig().addDefault("factionChatColor", "gold");
        }
        if (!MedievalFactions.getInstance().getConfig().isBoolean("territoryAlertPopUp")) {
            MedievalFactions.getInstance().getConfig().addDefault("territoryAlertPopUp", true);
        }
        if (!MedievalFactions.getInstance().getConfig().isBoolean("territoryIndicatorActionbar")) {
            MedievalFactions.getInstance().getConfig().addDefault("territoryIndicatorActionbar", true);
        }
        if (!MedievalFactions.getInstance().getConfig().isString("territoryAlertColor")) {
            MedievalFactions.getInstance().getConfig().addDefault("territoryAlertColor", "white");
        }
        if (!MedievalFactions.getInstance().getConfig().isBoolean("randomFactionAssignment")) {
            MedievalFactions.getInstance().getConfig().addDefault("randomFactionAssignment", false);
        }
        if (!MedievalFactions.getInstance().getConfig().isBoolean("allowNeutrality")) {
            MedievalFactions.getInstance().getConfig().addDefault("allowNeutrality", false);
        }
        if (!MedievalFactions.getInstance().getConfig().isBoolean("showPrefixesInFactionChat")) {
            MedievalFactions.getInstance().getConfig().addDefault("showPrefixesInFactionChat", false);
        }

        deleteOldConfigOptionsIfPresent();

        MedievalFactions.getInstance().getConfig().options().copyDefaults(true);
        MedievalFactions.getInstance().saveConfig();
    }

    private void deleteOldConfigOptionsIfPresent() {

        if (MedievalFactions.getInstance().getConfig().isInt("officerLimit")) {
            MedievalFactions.getInstance().getConfig().set("officerLimit", null);
        }

        if (MedievalFactions.getInstance().getConfig().isInt("hourlyPowerIncreaseAmount")) {
            MedievalFactions.getInstance().getConfig().set("hourlyPowerIncreaseAmount", null);
        }

        if (MedievalFactions.getInstance().getConfig().isInt("maxPowerLevel")) {
            MedievalFactions.getInstance().getConfig().set("maxPowerLevel", null);
        }

    }

    public void setConfigOption(String option, String value, CommandSender sender) {

        if (MedievalFactions.getInstance().getConfig().isSet(option)) {

            if (option.equalsIgnoreCase("version")) {
                sender.sendMessage(ChatColor.RED + LocaleManager.getInstance().getText("CannotSetVersion"));
                return;
            }
            else if (option.equalsIgnoreCase("initialMaxPowerLevel") || option.equalsIgnoreCase("initialPowerLevel")
                    || option.equalsIgnoreCase("powerIncreaseAmount")
                    || option.equalsIgnoreCase("minutesBeforeInitialPowerIncrease")
                    || option.equalsIgnoreCase("minutesBetweenPowerIncreases")
                    || option.equalsIgnoreCase("officerLimit")
                    || option.equalsIgnoreCase("officerPerMemberCount")
                    || option.equalsIgnoreCase("minutesBetweenPowerDecreases")
                    || option.equalsIgnoreCase("minutesBeforePowerDecrease")
                    || option.equalsIgnoreCase("powerDecreaseAmount")
                    || option.equalsIgnoreCase("factionMaxNameLength")
	                || option.equalsIgnoreCase("factionMaxNumberGates")
	                || option.equalsIgnoreCase("factionMaxGateArea")
                    || option.equalsIgnoreCase("maxClaimRadius"))
            {
                MedievalFactions.getInstance().getConfig().set(option, Integer.parseInt(value));
                sender.sendMessage(ChatColor.GREEN + LocaleManager.getInstance().getText("IntegerSet"));
            }
            else if (option.equalsIgnoreCase("mobsSpawnInFactionTerritory")
                    || option.equalsIgnoreCase("laddersPlaceableInEnemyFactionTerritory")
                    || option.equalsIgnoreCase("warsRequiredForPVP")
                    || option.equalsIgnoreCase("powerDecreases")
                    || option.equalsIgnoreCase("surroundedChunksProtected")
                    || option.equalsIgnoreCase("zeroPowerFactionsGetDisbanded")
                    || option.equalsIgnoreCase("nonMembersCanInteractWithDoors")
                    || option.equalsIgnoreCase("playersChatWithPrefixes")
                    || option.equalsIgnoreCase("chatSharedInVassalageTrees")
                    || option.equalsIgnoreCase("allowAllyInteraction")
                    || option.equalsIgnoreCase("allowVassalageTreeInteraction")
                    || option.equalsIgnoreCase("territoryAlertPopUp")
                    || option.equalsIgnoreCase("randomFactionAssignment")
                    || option.equalsIgnoreCase("allowNeutrality")
                    || option.equalsIgnoreCase("showPrefixesInFactionChat")) {
                MedievalFactions.getInstance().getConfig().set(option, Boolean.parseBoolean(value));
                sender.sendMessage(ChatColor.GREEN + LocaleManager.getInstance().getText("BooleanSet"));
            }
            else if (option.equalsIgnoreCase("factionOwnerMultiplier")
                    || option.equalsIgnoreCase("factionOfficerMultiplier")
                    || option.equalsIgnoreCase("vassalContributionPercentageMultiplier")){
                MedievalFactions.getInstance().getConfig().set(option, Double.parseDouble(value));
                sender.sendMessage(ChatColor.GREEN + LocaleManager.getInstance().getText("DoubleSet"));
            }
            else {
                MedievalFactions.getInstance().getConfig().set(option, value);
                sender.sendMessage(ChatColor.GREEN + LocaleManager.getInstance().getText("StringSet"));

                if (option.equalsIgnoreCase("languageid")) {
                    LocaleManager.getInstance().reloadStrings();
                }
            }

            // save
            MedievalFactions.getInstance().saveConfig();
            altered = true;
        }
        else {
            sender.sendMessage(ChatColor.RED + String.format(LocaleManager.getInstance().getText("WasntFound"), option));
        }

    }

    public void saveConfigDefaults() {
        MedievalFactions.getInstance().getConfig().addDefault("version", MedievalFactions.getInstance().getVersion());
        MedievalFactions.getInstance().getConfig().addDefault("initialMaxPowerLevel", 20);
        MedievalFactions.getInstance().getConfig().addDefault("initialPowerLevel", 5);
        MedievalFactions.getInstance().getConfig().addDefault("powerIncreaseAmount", 2);
        MedievalFactions.getInstance().getConfig().addDefault("mobsSpawnInFactionTerritory", false);
        MedievalFactions.getInstance().getConfig().addDefault("laddersPlaceableInEnemyFactionTerritory", true);
        MedievalFactions.getInstance().getConfig().addDefault("minutesBeforeInitialPowerIncrease", 30);
        MedievalFactions.getInstance().getConfig().addDefault("minutesBetweenPowerIncreases", 60);
        MedievalFactions.getInstance().getConfig().addDefault("warsRequiredForPVP", true);
        MedievalFactions.getInstance().getConfig().addDefault("factionOwnerMultiplier", 2.0);
        MedievalFactions.getInstance().getConfig().addDefault("officerPerMemberCount", 5);
        MedievalFactions.getInstance().getConfig().addDefault("factionOfficerMultiplier", 1.5);
        MedievalFactions.getInstance().getConfig().addDefault("powerDecreases", true);
        MedievalFactions.getInstance().getConfig().addDefault("minutesBetweenPowerDecreases", 1440);
        MedievalFactions.getInstance().getConfig().addDefault("minutesBeforePowerDecrease", 20160);
        MedievalFactions.getInstance().getConfig().addDefault("powerDecreaseAmount", 1);
        MedievalFactions.getInstance().getConfig().addDefault("factionMaxNameLength", 20);
        MedievalFactions.getInstance().getConfig().addDefault("factionMaxNumberGates", 5);
        MedievalFactions.getInstance().getConfig().addDefault("factionMaxGateArea", 64);
        MedievalFactions.getInstance().getConfig().addDefault("surroundedChunksProtected", true);
        MedievalFactions.getInstance().getConfig().addDefault("zeroPowerFactionsGetDisbanded", false);
        MedievalFactions.getInstance().getConfig().addDefault("vassalContributionPercentageMultiplier", 0.75);
        MedievalFactions.getInstance().getConfig().addDefault("nonMembersCanInteractWithDoors", false);
        MedievalFactions.getInstance().getConfig().addDefault("playersChatWithPrefixes", true);
        MedievalFactions.getInstance().getConfig().addDefault("maxClaimRadius", 3);
        MedievalFactions.getInstance().getConfig().addDefault("languageid", "en-us");
        MedievalFactions.getInstance().getConfig().addDefault("chatSharedInVassalageTrees", true);
        MedievalFactions.getInstance().getConfig().addDefault("allowAllyInteraction", false);
        MedievalFactions.getInstance().getConfig().addDefault("allowVassalageTreeInteraction", false);
        MedievalFactions.getInstance().getConfig().addDefault("factionChatColor", "gold");
        MedievalFactions.getInstance().getConfig().addDefault("territoryAlertPopUp", true);
        MedievalFactions.getInstance().getConfig().addDefault("territoryAlertColor", "white");
        MedievalFactions.getInstance().getConfig().addDefault("randomFactionAssignment", false);
        MedievalFactions.getInstance().getConfig().addDefault("allowNeutrality", false);
        MedievalFactions.getInstance().getConfig().addDefault("showPrefixesInFactionChat", false);
        MedievalFactions.getInstance().getConfig().options().copyDefaults(true);
        MedievalFactions.getInstance().saveConfig();
    }

    public void sendPageOneOfConfigList(CommandSender sender) {
        sender.sendMessage(ChatColor.AQUA + LocaleManager.getInstance().getText("ConfigListPageOne"));
        sender.sendMessage(ChatColor.AQUA + "version: " + MedievalFactions.getInstance().getConfig().getString("version")
                + ", languageid: " + MedievalFactions.getInstance().getConfig().getString("languageid")
                + ", initialMaxPowerLevel: " + MedievalFactions.getInstance().getConfig().getInt("initialMaxPowerLevel")
                + ", initialPowerLevel: " +  MedievalFactions.getInstance().getConfig().getInt("initialPowerLevel")
                + ", powerIncreaseAmount: " + MedievalFactions.getInstance().getConfig().getInt("powerIncreaseAmount")
                + ", mobsSpawnInFactionTerritory: " + MedievalFactions.getInstance().getConfig().getBoolean("mobsSpawnInFactionTerritory")
                + ", laddersPlaceableInEnemyFactionTerritory: " + MedievalFactions.getInstance().getConfig().getBoolean("laddersPlaceableInEnemyFactionTerritory")
                + ", minutesBeforeInitialPowerIncrease: " + MedievalFactions.getInstance().getConfig().getInt("minutesBeforeInitialPowerIncrease")
                + ", minutesBetweenPowerIncreases: " + MedievalFactions.getInstance().getConfig().getInt("minutesBetweenPowerIncreases")
                + ", warsRequiredForPVP: " + MedievalFactions.getInstance().getConfig().getBoolean("warsRequiredForPVP")
                + ", factionOwnerMultiplier: " + MedievalFactions.getInstance().getConfig().getDouble("factionOwnerMultiplier")
                + ", officerPerMemberCount: " + MedievalFactions.getInstance().getConfig().getInt("officerPerMemberCount")
                + ", factionOfficerMultiplier: " + MedievalFactions.getInstance().getConfig().getDouble("factionOfficerMultiplier")
                + ", powerDecreases: " + MedievalFactions.getInstance().getConfig().getBoolean("powerDecreases")
                + ", minutesBetweenPowerDecreases: " + MedievalFactions.getInstance().getConfig().getInt("minutesBetweenPowerDecreases")
                + ", minutesBeforePowerDecrease: " + MedievalFactions.getInstance().getConfig().getInt("minutesBeforePowerDecrease")
                + ", powerDecreaseAmount: " + MedievalFactions.getInstance().getConfig().getInt("powerDecreaseAmount")
                + ", factionMaxNameLength: " + MedievalFactions.getInstance().getConfig().getInt("factionMaxNameLength")
		        + ", factionMaxNumberGates: " + MedievalFactions.getInstance().getConfig().getInt("factionMaxNumberGates")
		        + ", factionMaxGateArea: " + MedievalFactions.getInstance().getConfig().getInt("factionMaxGateArea"));
    }

    public void sendPageTwoOfConfigList(CommandSender sender) {
        sender.sendMessage(ChatColor.AQUA + LocaleManager.getInstance().getText("ConfigListPageTwo"));
        sender.sendMessage(ChatColor.AQUA + "surroundedChunksProtected: " + MedievalFactions.getInstance().getConfig().getBoolean("surroundedChunksProtected")
                + ", zeroPowerFactionsGetDisbanded: " + MedievalFactions.getInstance().getConfig().getBoolean("zeroPowerFactionsGetDisbanded")
                + ", vassalContributionPercentageMultiplier: " + MedievalFactions.getInstance().getConfig().getDouble("vassalContributionPercentageMultiplier")
                + ", nonMembersCanInteractWithDoors: " + MedievalFactions.getInstance().getConfig().getBoolean("nonMembersCanInteractWithDoors")
                + ", playersChatWithPrefixes: " + MedievalFactions.getInstance().getConfig().getBoolean("playersChatWithPrefixes")
                + ", maxClaimRadius: " + MedievalFactions.getInstance().getConfig().getInt("maxClaimRadius")
                + ", chatSharedInVassalageTrees: " + MedievalFactions.getInstance().getConfig().getBoolean("chatSharedInVassalageTrees")
                + ", allowAllyInteraction: " + MedievalFactions.getInstance().getConfig().getBoolean("allowAllyInteraction")
                + ", allowVassalageTreeInteraction: " + MedievalFactions.getInstance().getConfig().getBoolean("allowVassalageTreeInteraction")
                + ", factionChatColor: " + MedievalFactions.getInstance().getConfig().getString("factionChatColor")
                + ", territoryAlertPopUp: " + MedievalFactions.getInstance().getConfig().getBoolean("territoryAlertPopUp")
                + ", territoryAlertColor: " + MedievalFactions.getInstance().getConfig().getString("territoryAlertColor")
                + ", randomFactionAssignment: " + MedievalFactions.getInstance().getConfig().getBoolean("randomFactionAssignment")
                + ", allowNeutrality: " + MedievalFactions.getInstance().getConfig().getBoolean("allowNeutrality")
                + ", showPrefixesInFactionChat: " + MedievalFactions.getInstance().getConfig().getBoolean("showPrefixesInFactionChat"));
    }

    public boolean hasBeenAltered() {
        return altered;
    }

}
