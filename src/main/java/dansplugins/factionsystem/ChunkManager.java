package dansplugins.factionsystem;

import dansplugins.factionsystem.data.EphemeralData;
import dansplugins.factionsystem.data.PersistentData;
import dansplugins.factionsystem.objects.ClaimedChunk;
import dansplugins.factionsystem.objects.*;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.time.ZonedDateTime;
import java.util.*;

import static java.awt.SystemColor.info;
import static org.bukkit.Bukkit.getServer;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class ChunkManager {

    private static ChunkManager instance;

    private ChunkManager() {

    }

    public static ChunkManager getInstance() {
        if (instance == null) {
            instance = new ChunkManager();
        }
        return instance;
    }

    public void claimChunkAtPlayerLocation(Player player, Faction playersFaction) {

        double[] playerCoords = getPlayerChunkCoords(player);

        // check if land is already claimed
        ClaimedChunk chunk = isChunkClaimed(playerCoords[0], playerCoords[1], player.getLocation().getWorld().getName());
        if (chunk != null) {
            // chunk already claimed
            Faction targetFaction = PersistentData.getInstance().getFaction(chunk.getHolder());

            // if holder is player's faction
            if (targetFaction.getName().equalsIgnoreCase(playersFaction.getName()) && !playersFaction.getAutoClaimStatus()) {
                player.sendMessage(ChatColor.RED + "This land is already claimed by your faction!");
                return;
            }

            // if not at war with target faction and inactive claiming isn't possible
            if (!playersFaction.isEnemy(targetFaction.getName()) && !everyPlayerInFactionExperiencingPowerDecay(targetFaction)) {
                player.sendMessage(ChatColor.RED + "Must be at war with target faction or target faction must be inactive!");
                return;
            }

            // surrounded chunk protection check
            if (MedievalFactions.getInstance().getConfig().getBoolean("surroundedChunksProtected")) {
                if (isClaimedChunkSurroundedByChunksClaimedBySameFaction(chunk)) {
                    player.sendMessage(ChatColor.RED + "Target faction has claimed the chunks to the north, east, south and west of this chunk! It cannot be conquered!");
                    return;
                }
            }

            int targetFactionsCumulativePowerLevel = targetFaction.getCumulativePowerLevel();
            int chunksClaimedByTargetFaction = getChunksClaimedByFaction(targetFaction.getName(), PersistentData.getInstance().getClaimedChunks());

            // if target faction does not have more land than their demesne limit
            if (!(targetFactionsCumulativePowerLevel < chunksClaimedByTargetFaction)) {
                player.sendMessage(ChatColor.RED + "Target faction does not have more land than their demsene limit!");
                return;
            }

            // CONQUERABLE

            // remove locks on this chunk
            Iterator<LockedBlock> itr = PersistentData.getInstance().getLockedBlocks().iterator();
            while (itr.hasNext()) {
                LockedBlock block = itr.next();
                if (chunk.getChunk().getWorld().getBlockAt(block.getX(), block.getY(), block.getZ()).getChunk().getX() == chunk.getChunk().getX() &&
                        chunk.getChunk().getWorld().getBlockAt(block.getX(), block.getY(), block.getZ()).getChunk().getZ() == chunk.getChunk().getZ()) {
                    itr.remove();
                }
            }

            PersistentData.getInstance().getClaimedChunks().remove(chunk);

            addClaimedChunk(player.getLocation(), playersFaction);
            player.sendMessage(ChatColor.GREEN + "Land conquered from " + targetFaction.getName() + "! Demesne Size: " + getChunksClaimedByFaction(playersFaction.getName(), PersistentData.getInstance().getClaimedChunks()) + "/" + playersFaction.getCumulativePowerLevel());

            Messenger.getInstance().sendAllPlayersInFactionMessage(targetFaction, ChatColor.RED + playersFaction.getName() + " has conquered land from your faction!");
        }
        else {
            // chunk not already claimed
            addClaimedChunk(player.getLocation(), playersFaction);
            player.sendMessage(ChatColor.GREEN + "Land claimed! Demesne Size: " + getChunksClaimedByFaction(playersFaction.getName(), PersistentData.getInstance().getClaimedChunks()) + "/" + playersFaction.getCumulativePowerLevel());
        }
    }

    private void addClaimedChunk(Location location, Faction faction) {
        ClaimedChunk newChunk = new ClaimedChunk(location.getChunk());
        newChunk.setHolder(faction.getName());
        newChunk.setWorld(location.getWorld().getName());
        PersistentData.getInstance().getClaimedChunks().add(newChunk);
    }

    private double[] getPlayerChunkCoords(Player player) {
        double[] playerCoords = new double[2];
        playerCoords[0] = player.getLocation().getChunk().getX();
        playerCoords[1] = player.getLocation().getChunk().getZ();
        return playerCoords;
    }

    private ClaimedChunk isChunkClaimed(double x, double y, String world)
    {
        for (ClaimedChunk chunk : PersistentData.getInstance().getClaimedChunks())
        {
            if (x == chunk.getCoordinates()[0] && y == chunk.getCoordinates()[1] && world.equalsIgnoreCase(chunk.getWorld()))
            {
                return chunk;
            }
        }

        return null;
    }

    private boolean everyPlayerInFactionExperiencingPowerDecay(Faction faction) {
        int numExperiencingPowerDecay = 0;
        for (UUID uuid : faction.getMemberArrayList()) {
            PlayerActivityRecord record = PersistentData.getInstance().getPlayerActivityRecord(uuid);
            if (record != null) {
                Player player = getServer().getPlayer(record.getPlayerUUID());
                boolean isOnline = false;
                if (player != null)
                {
                    isOnline = player.isOnline();
                }
                if (!isOnline && MedievalFactions.getInstance().getConfig().getBoolean("powerDecreases")
                        && record.getMinutesSinceLastLogout() > MedievalFactions.getInstance().getConfig().getInt("minutesBeforePowerDecrease")) {
                    numExperiencingPowerDecay++;
                }
            }
            else {
                PlayerActivityRecord newRecord = new PlayerActivityRecord(uuid, 1);
                newRecord.setLastLogout(ZonedDateTime.now());
                PersistentData.getInstance().getPlayerActivityRecords().add(newRecord);
            }
        }
        return (numExperiencingPowerDecay == faction.getMemberArrayList().size());
    }

    public void removeChunkAtPlayerLocation(Player player) {
        double[] playerCoords = new double[2];
        playerCoords[0] = player.getLocation().getChunk().getX();
        playerCoords[1] = player.getLocation().getChunk().getZ();

        if (EphemeralData.getInstance().getAdminsBypassingProtections().contains(player.getUniqueId())) {
            ClaimedChunk chunk = isChunkClaimed(playerCoords[0], playerCoords[1], player.getLocation().getWorld().getName());
            if (chunk != null)
            {
                removeChunk(chunk, player, PersistentData.getInstance().getFaction(chunk.getHolder()));
                player.sendMessage(ChatColor.GREEN + "Land unclaimed using admin bypass!");
                return;
            }
            player.sendMessage(ChatColor.RED + "This land is not currently claimed!");
            return;
        }

        for (Faction faction : PersistentData.getInstance().getFactions()) {
            if (faction.isOwner(player.getUniqueId()) || faction.isOfficer(player.getUniqueId())) {
                // check if land is claimed by player's faction
                ClaimedChunk chunk = isChunkClaimed(playerCoords[0], playerCoords[1], player.getLocation().getWorld().getName());
                if (chunk != null)
                {
                    // if holder is player's faction
                    if (chunk.getHolder().equalsIgnoreCase(faction.getName())) {
                        removeChunk(chunk, player, faction);
                        player.sendMessage(ChatColor.GREEN + "Land unclaimed.");

                        return;
                    }
                    else {
                        player.sendMessage(ChatColor.RED + "This land is claimed by " + chunk.getHolder());
                        return;
                    }
                }

            }
        }
    }

    private void removeChunk(ClaimedChunk chunk, Player player, Faction faction) {
        String identifier = (int)chunk.getChunk().getX() + "_" + (int)chunk.getChunk().getZ();

        // if faction home is located on this chunk
        Location factionHome = PersistentData.getInstance().getPlayersFaction(player.getUniqueId()).getFactionHome();
        if (factionHome != null) {
            if (factionHome.getChunk().getX() == chunk.getChunk().getX() && factionHome.getChunk().getZ() == chunk.getChunk().getZ()
                    && chunk.getWorld().equalsIgnoreCase(player.getLocation().getWorld().getName())) {
                // remove faction home
                faction.setFactionHome(null);
                Messenger.getInstance().sendAllPlayersInFactionMessage(faction, ChatColor.RED + "Your faction home has been removed!");

            }
        }

        // remove locks on this chunk
        Iterator<LockedBlock> itr = PersistentData.getInstance().getLockedBlocks().iterator();
        while (itr.hasNext()) {
            LockedBlock block = itr.next();
            if (chunk.getChunk().getWorld().getBlockAt(block.getX(), block.getY(), block.getZ()).getChunk().getX() == chunk.getChunk().getX() &&
                    chunk.getChunk().getWorld().getBlockAt(block.getX(), block.getY(), block.getZ()).getChunk().getZ() == chunk.getChunk().getZ() &&
                    block.getWorld().equalsIgnoreCase(chunk.getWorld())) {
                itr.remove();
            }
        }

        // remove any gates in this chunk
        Iterator<Gate> gtr = faction.getGates().iterator();
        while(gtr.hasNext())
        {
            Gate gate = gtr.next();
            if (isGateInChunk(gate, chunk))
            {
//        		System.out.println("Removing gate " + gate.getName());
                faction.removeGate(gate);
                gtr.remove();
            }
        }

        PersistentData.getInstance().getClaimedChunks().remove(chunk);
    }

    public String checkOwnershipAtPlayerLocation(Player player) {
        double[] playerCoords = new double[2];
        playerCoords[0] = player.getLocation().getChunk().getX();
        playerCoords[1] = player.getLocation().getChunk().getZ();
        ClaimedChunk chunk = isChunkClaimed(playerCoords[0], playerCoords[1], player.getLocation().getWorld().getName());
        if (chunk != null)
        {
            return chunk.getHolder();
        }
        return "unclaimed";
    }

    public boolean isGateInChunk(Gate gate, ClaimedChunk chunk)
    {
        if ((gate.getTopLeftChunkX() == chunk.getCoordinates()[0] || gate.getBottomRightChunkX() == chunk.getCoordinates()[0])
                && (gate.getTopLeftChunkZ() == chunk.getCoordinates()[1] || gate.getBottomRightChunkZ() == chunk.getCoordinates()[1]))
        {
            return true;
        }
        return false;
    }

    public int getChunksClaimedByFaction(String factionName, ArrayList<ClaimedChunk> claimedChunks) {
        int counter = 0;
        for (ClaimedChunk chunk : claimedChunks) {
            if (chunk.getHolder().equalsIgnoreCase(factionName)) {
                counter++;
            }
        }
        return counter;
    }

    public ClaimedChunk getClaimedChunk(int x, int z, String world, ArrayList<ClaimedChunk> claimedChunks) {
        for (ClaimedChunk claimedChunk : claimedChunks) {
            if (claimedChunk.getCoordinates()[0] == x && claimedChunk.getCoordinates()[1] == z && claimedChunk.getWorld().equalsIgnoreCase(world)) {
                return claimedChunk;
            }
        }
        return null;
    }

    private Chunk getChunkByDirection(Chunk origin, String direction) {

        int xpos = -1;
        int zpos = -1;

        if (direction.equalsIgnoreCase("north")) {
            xpos = origin.getX();
            zpos = origin.getZ() + 1;
        }
        if (direction.equalsIgnoreCase("east")) {
            xpos = origin.getX() + 1;
            zpos = origin.getZ();
        }
        if (direction.equalsIgnoreCase("south")) {
            xpos = origin.getX();
            zpos = origin.getZ() - 1;
        }
        if (direction.equalsIgnoreCase("west")) {
            xpos = origin.getX() - 1;
            zpos = origin.getZ();
        }

        return origin.getWorld().getChunkAt(xpos, zpos);
    }

    private ClaimedChunk getClaimedChunk(Chunk chunk) {
        return getClaimedChunk(chunk.getX(), chunk.getZ(), chunk.getWorld().getName(), PersistentData.getInstance().getClaimedChunks());
    }

    // this will return true if the chunks to the North, East, South and West of the target are claimed by the same faction as the target
    private boolean isClaimedChunkSurroundedByChunksClaimedBySameFaction(ClaimedChunk target) {
        ClaimedChunk northernClaimedChunk = getClaimedChunk(getChunkByDirection(target.getChunk(), "north"));
        ClaimedChunk easternClaimedChunk = getClaimedChunk(getChunkByDirection(target.getChunk(), "east"));
        ClaimedChunk southernClaimedChunk = getClaimedChunk(getChunkByDirection(target.getChunk(), "south"));
        ClaimedChunk westernClaimedChunk = getClaimedChunk(getChunkByDirection(target.getChunk(), "west"));

        if (northernClaimedChunk == null ||
                easternClaimedChunk == null ||
                southernClaimedChunk == null ||
                westernClaimedChunk == null) {

            return false;

        }

        boolean northernChunkClaimedBySameFaction = target.getHolder().equalsIgnoreCase(northernClaimedChunk.getHolder());
        boolean easternChunkClaimedBySameFaction = target.getHolder().equalsIgnoreCase(easternClaimedChunk.getHolder());
        boolean southernChunkClaimedBySameFaction = target.getHolder().equalsIgnoreCase(southernClaimedChunk.getHolder());
        boolean westernChunkClaimedBySameFaction = target.getHolder().equalsIgnoreCase(westernClaimedChunk.getHolder());

        return (northernChunkClaimedBySameFaction &&
                easternChunkClaimedBySameFaction &&
                southernChunkClaimedBySameFaction &&
                westernChunkClaimedBySameFaction);
    }

    public boolean isClaimed(Chunk chunk, ArrayList<ClaimedChunk> claimedChunks) {

        for (ClaimedChunk claimedChunk : claimedChunks) {
            if (claimedChunk.getCoordinates()[0] == chunk.getX() && claimedChunk.getCoordinates()[1] == chunk.getZ() && claimedChunk.getWorld().equalsIgnoreCase(chunk.getWorld().getName())) {
                return true;
            }
        }
        return false;
    }

    public void removeAllClaimedChunks(String factionName, ArrayList<ClaimedChunk> claimedChunks) {

        Iterator<ClaimedChunk> itr = claimedChunks.iterator();

        while (itr.hasNext()) {
            ClaimedChunk currentChunk = itr.next();
            if (currentChunk.getHolder().equalsIgnoreCase(factionName)) {
                try {
                    itr.remove();
                }
                catch(Exception e) {
                    System.out.println("An error has occurred during claimed chunk removal.");
                }
            }
        }
    }

    public boolean isFactionExceedingTheirDemesneLimit(Faction faction, ArrayList<ClaimedChunk> claimedChunks) {
        return (getChunksClaimedByFaction(faction.getName(), claimedChunks) > faction.getCumulativePowerLevel());
    }

    public void informPlayerIfTheirLandIsInDanger(Player player, ArrayList<Faction> factions, ArrayList<ClaimedChunk> claimedChunks) {
        Faction faction = PersistentData.getInstance().getPlayersFaction(player.getUniqueId());
        if (faction != null) {
            if (isFactionExceedingTheirDemesneLimit(faction, claimedChunks)) {
                player.sendMessage(ChatColor.RED + "Your faction has more claimed chunks than power! Your land can be conquered!");
            }
        }
    }

}
