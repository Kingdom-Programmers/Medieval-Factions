package dansplugins.factionsystem.utils;

import dansplugins.factionsystem.MedievalFactions;
import dansplugins.factionsystem.managers.ActionBarManager;
import dansplugins.factionsystem.managers.LocaleManager;
import dansplugins.factionsystem.objects.Faction;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TerritoryOwnerNotifier {
    private static TerritoryOwnerNotifier instance;

    private TerritoryOwnerNotifier() {

    }

    public static TerritoryOwnerNotifier getInstance() {
        if (instance == null) instance = new TerritoryOwnerNotifier();
        return instance;
    }

    public void sendPlayerTerritoryAlert(Player player, Faction holder) {
        // get color
        ChatColor territoryAlertColor;
        if (holder != null) {
            String territoryAlertColorString = (String) holder.getFlags().getFlag("territoryAlertColor");
            territoryAlertColor = ColorChecker.getInstance().getColorByName(territoryAlertColorString);
        } else {
            String territoryAlertColorString = MedievalFactions.getInstance().getConfig().getString("territoryAlertColor");
            territoryAlertColor = ColorChecker.getInstance().getColorByName(territoryAlertColorString);
        }

        String title;
        if(holder != null) {
            title = holder.getName();
        } else {
            title = LocaleManager.getInstance().getText("Wilderness");
        }


        // set actionbar
        if(MedievalFactions.getInstance().getConfig().getBoolean("territoryIndicatorActionbar")) {
            ActionBarManager actionBar = ActionBarManager.getInstance(MedievalFactions.getInstance());

            if(holder == null) {
                actionBar.clearPlayerActionBar(player);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(territoryAlertColor + title));
            } else {
                actionBar.showPersistentActionBarMessage(player, new TextComponent(territoryAlertColor + title));
            }
        }

        // send alert
        if (MedievalFactions.getInstance().getConfig().getBoolean("territoryAlertPopUp")) {
            int fadeIn = 10;
            int stay = 70;
            int fadeOut = 20;
            player.sendTitle(territoryAlertColor + title, null, fadeIn, stay, fadeOut);
        }
        else {
            player.sendMessage(territoryAlertColor + title);
        }
    }

}
