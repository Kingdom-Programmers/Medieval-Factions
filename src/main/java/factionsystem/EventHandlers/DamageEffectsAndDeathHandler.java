package factionsystem.EventHandlers;

import factionsystem.MedievalFactions;
import factionsystem.Objects.ClaimedChunk;
import factionsystem.Objects.Duel;
import factionsystem.Objects.PlayerPowerRecord;
import factionsystem.Subsystems.UtilitySubsystem;

import factionsystem.Util.Pair;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.projectiles.ProjectileSource;

import java.util.ArrayList;
import java.util.List;

import static factionsystem.Subsystems.UtilitySubsystem.*;
import static factionsystem.Subsystems.UtilitySubsystem.getPlayersFaction;

public class DamageEffectsAndDeathHandler implements Listener {

    @EventHandler()
    public void handle(EntityDamageByEntityEvent event) {
    	
   	
        // this method disallows PVP between members of the same faction and between factions who are not at war
        // PVP is allowed between factionless players, players who belong to a faction and the factionless, and players whose factions are at war.
        // System.out.println("EntityDamageByIntity" + event.toString());

        // if this was between two players melee
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            Player victim = (Player) event.getEntity();
        	// if these players are actively duelling then we don't want to handle friendly fire.
            Duel duel = UtilitySubsystem.getDuel(attacker, victim);
            if (duel == null)
            {
            	handleIfFriendlyFire(event, attacker, victim);	
            }
            else if (duel.getStatus().equals(Duel.DuelState.DUELLING))
            {
            	if (victim.getHealth() - event.getFinalDamage() <= 0)
            	{
        			duel.setLoser(victim);
            		duel.finishDuel(false);
            		MedievalFactions.getInstance().duelingPlayers.remove(this);
            		event.setCancelled(true);
            	}
            }
            else
            {
            	handleIfFriendlyFire(event, attacker, victim);
            }
        }
        else if (event.getDamager() instanceof Projectile && event.getEntity() instanceof Player) {
            Projectile arrow = (Projectile) event.getDamager();
            ProjectileSource source = arrow.getShooter();

            if (source instanceof Player){
                Player attacker = (Player) source;
                Player victim = (Player) event.getEntity();

            	// if these players are actively duelling then we don't want to handle friendly fire.
                Duel duel = UtilitySubsystem.getDuel(attacker, victim);
                if (duel == null)
                {
                	handleIfFriendlyFire(event, attacker, victim);	
                }
                else if (duel.getStatus().equals(Duel.DuelState.DUELLING))
                {
                	if (victim.getHealth() - event.getFinalDamage() <= 0)
                	{
            			duel.setLoser(victim);
                		duel.finishDuel(false);
                		MedievalFactions.getInstance().duelingPlayers.remove(this);
                		event.setCancelled(true);
                	}
                }
                else
                {
                	handleIfFriendlyFire(event, attacker, victim);
                }
            }
        }
    }

    private void handleIfFriendlyFire(EntityDamageByEntityEvent event, Player attacker, Player victim) {
        if (!MedievalFactions.getInstance().utilities.arePlayersInAFaction(attacker, victim)){
            // Factionless can fight anyone.
            return;
        }
        else if (MedievalFactions.getInstance().utilities.arePlayersInSameFaction(attacker, victim)) {
            event.setCancelled(true);
            attacker.sendMessage(ChatColor.RED + "You can't attack another player if you are part of the same faction.");
        }

        // if attacker's faction and victim's faction are not at war
        else if (MedievalFactions.getInstance().utilities.arePlayersFactionsNotEnemies(attacker, victim)) {
            if (MedievalFactions.getInstance().getConfig().getBoolean("warsRequiredForPVP")) {
                event.setCancelled(true);
                attacker.sendMessage(ChatColor.RED + "You can't attack another player if your factions aren't at war.");
            }
        }
    }

    @EventHandler()
    public void handle(AreaEffectCloudApplyEvent event) {
        AreaEffectCloud cloud = event.getEntity();

        if (MedievalFactions.getInstance().utilities.potionTypeBad(cloud.getBasePotionData().getType())){
            // Search to see if cloud is in the stored list in MedievalFactions.getInstance()
            for (Pair<Player, AreaEffectCloud> storedCloudPair : MedievalFactions.getInstance().activeAOEClouds){
                if (storedCloudPair.getRight() == cloud){
                    //Check player is not allied with effected entities if any allied remove entity from list.
                    Player attacker = storedCloudPair.getLeft();

                    List<Player> alliedVictims = new ArrayList<>();
                    for (Entity potentialVictimEntity : event.getAffectedEntities()){
                        if (potentialVictimEntity instanceof Player){
                            Player potentialVictim = (Player) potentialVictimEntity;

                            if (attacker == potentialVictim){
                                continue;
                            }

                            // If both are in a faction and not at war.
                            if (MedievalFactions.getInstance().utilities.arePlayersInAFaction(attacker, potentialVictim) &&
                                    (MedievalFactions.getInstance().utilities.arePlayersFactionsNotEnemies(attacker, potentialVictim) ||
                                            MedievalFactions.getInstance().utilities.arePlayersInSameFaction(attacker, potentialVictim))) {
                                alliedVictims.add(potentialVictim);
                            }
                        }
                    }

                    // Remove attacker's allies from the list
                    event.getAffectedEntities().removeAll(alliedVictims);
                }
            }
        }
    }

    @EventHandler()
    public void handle(LingeringPotionSplashEvent event) {
        Player thrower = (Player) event.getEntity().getShooter();
        AreaEffectCloud cloud = event.getAreaEffectCloud();

        Pair<Player, AreaEffectCloud> storedCloud  = new Pair<>(thrower, cloud);
        MedievalFactions.getInstance().activeAOEClouds.add(storedCloud);

        // Add scheduled task to remove the cloud from the activeClouds list
        long delay = cloud.getDuration();
        MedievalFactions.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(MedievalFactions.getInstance(), new Runnable() {
            public void run(){
                MedievalFactions.getInstance().activeAOEClouds.remove(storedCloud);
            }
        }, delay);
    }

    @EventHandler()
    public void handle(PlayerDeathEvent event) {
        event.getEntity();
        Player player = event.getEntity();

        // decrease dying player's power
        for (PlayerPowerRecord record : MedievalFactions.getInstance().playerPowerRecords) {
            if (record.getPlayerUUID().equals(player.getUniqueId())) {
                record.decreasePowerByTenPercent();
                if (getPlayersPowerRecord(player.getUniqueId(), MedievalFactions.getInstance().playerPowerRecords).getPowerLevel() > 0) {
                    player.sendMessage(ChatColor.RED + "Your power level has decreased!");
                }
            }
        }

        // if player's cause of death was another player killing them
        if (player.getKiller() != null) {
            Player killer = player.getKiller();

            PlayerPowerRecord record = UtilitySubsystem.getPlayersPowerRecord(killer.getUniqueId(), MedievalFactions.getInstance().playerPowerRecords);
            if (record != null) {
                if (record.increasePowerByTenPercent()){
                    killer.sendMessage(ChatColor.GREEN + "Your power level has increased!");
                }
            }
        }

        // if player is in faction
        if (isInFaction(player.getUniqueId(), MedievalFactions.getInstance().factions)) {

            // if player is in land claimed by their faction
            double[] playerCoords = new double[2];
            playerCoords[0] = player.getLocation().getChunk().getX();
            playerCoords[1] = player.getLocation().getChunk().getZ();

            // check if land is claimed
            if (UtilitySubsystem.isClaimed(player.getLocation().getChunk(), MedievalFactions.getInstance().claimedChunks))
            {
                ClaimedChunk chunk = UtilitySubsystem.getClaimedChunk(player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ(),
                        player.getLocation().getWorld().getName(), MedievalFactions.getInstance().claimedChunks);
                // if holder is player's faction
                if (chunk.getHolder().equalsIgnoreCase(getPlayersFaction(player.getUniqueId(), MedievalFactions.getInstance().factions).getName()) && getPlayersFaction(player.getUniqueId(), MedievalFactions.getInstance().factions).getAutoClaimStatus() == false) {

                    // if not killed by another player
                    if (!(player.getKiller() instanceof Player)) {

                        // player keeps items
                        // event.setKeepInventory(true); // TODO: fix this duplicating items

                    }

                }
            }

        }

    }

    @EventHandler()
    public void handle(PotionSplashEvent event) {
        ThrownPotion potion = event.getPotion();

        // If shooter was not player ignore.
        if (!(potion.getShooter() instanceof Player)) return;
        Player attacker = (Player) potion.getShooter();

        for(PotionEffect effect : potion.getEffects()) {
            // Is potion effect bad?
            if (MedievalFactions.getInstance().utilities.potionEffectBad(effect.getType())) {

                // If any victim is a allied player remove potion intensity
                for (LivingEntity victimEntity : event.getAffectedEntities()) {
                    if (victimEntity instanceof Player){
                        Player victim = (Player) victimEntity;

                        // People can still hurt themselves, let's encourage skill!
                        if (attacker == victim){
                            continue;
                        }

                        // If players are in faction and not at war
                        if (MedievalFactions.getInstance().utilities.arePlayersInAFaction(attacker, victim) &&
                                (MedievalFactions.getInstance().utilities.arePlayersFactionsNotEnemies(attacker, victim) ||
                                        MedievalFactions.getInstance().utilities.arePlayersInSameFaction(attacker, victim))) {
                            event.setIntensity(victimEntity, 0);
                        }
                    }
                }
            }
        }
    }

}