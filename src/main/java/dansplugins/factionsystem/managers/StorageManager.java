package dansplugins.factionsystem.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import dansplugins.factionsystem.MedievalFactions;
import dansplugins.factionsystem.data.PersistentData;
import dansplugins.factionsystem.objects.*;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorageManager {

    private static StorageManager instance;

    private final static String FILE_PATH = "./plugins/MedievalFactions/";
    private final static String FACTIONS_FILE_NAME = "factions.json";
    private final static String CHUNKS_FILE_NAME = "claimedchunks.json";
    private final static String PLAYERPOWER_FILE_NAME = "playerpowerrecords.json";
    private final static String PLAYERACTIVITY_FILE_NAME = "playeractivityrecords.json";
    private final static String LOCKED_BLOCKS_FILE_NAME = "lockedblocks.json";

    private final static Type LIST_MAP_TYPE = new TypeToken<ArrayList<HashMap<String, String>>>(){}.getType();

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();;

    private StorageManager() {

    }

    public static StorageManager getInstance() {
        if (instance == null) {
            instance = new StorageManager();
        }
        return instance;
    }

    public void save() {
        saveFactions();
        saveClaimedChunks();
        savePlayerPowerRecords();
        savePlayerActivityRecords();
        saveLockedBlocks();
        if (ConfigManager.getInstance().hasBeenAltered()) {
            MedievalFactions.getInstance().saveConfig();
        }
    }

    private void saveFactions() {
        List<Map<String, String>> factions = new ArrayList<>();
        for (Faction faction : PersistentData.getInstance().getFactions()){
            factions.add(faction.save());
        }

        File file = new File(FILE_PATH + FACTIONS_FILE_NAME);
        writeOutFiles(file, factions);
    }

    private void saveClaimedChunks() {
        List<Map<String, String>> chunks = new ArrayList<>();
        for (ClaimedChunk chunk : PersistentData.getInstance().getClaimedChunks()){
            chunks.add(chunk.save());
        }

        File file = new File(FILE_PATH + CHUNKS_FILE_NAME);
        writeOutFiles(file, chunks);
    }

    private void savePlayerPowerRecords() {
        List<Map<String, String>> playerPowerRecords = new ArrayList<>();
        for (PlayerPowerRecord record : PersistentData.getInstance().getPlayerPowerRecords()){
            playerPowerRecords.add(record.save());
        }

        File file = new File(FILE_PATH + PLAYERPOWER_FILE_NAME);
        writeOutFiles(file, playerPowerRecords);
    }
    
    private void savePlayerActivityRecords()
    {
    	List<Map<String, String>> playerActivityRecords = new ArrayList<>();
    	for (PlayerActivityRecord record : PersistentData.getInstance().getPlayerActivityRecords())
    	{
    		playerActivityRecords.add(record.save());
    		
    		File file = new File(FILE_PATH + PLAYERACTIVITY_FILE_NAME);
    		writeOutFiles(file, playerActivityRecords);
    	}
    }

    private void saveLockedBlocks() {
        List<Map<String, String>> lockedBlocks = new ArrayList<>();
        for (LockedBlock block : PersistentData.getInstance().getLockedBlocks()){
            lockedBlocks.add(block.save());
        }

        File file = new File(FILE_PATH + LOCKED_BLOCKS_FILE_NAME);
        writeOutFiles(file, lockedBlocks);
    }

    private void writeOutFiles(File file, List<Map<String, String>> saveData) {
        try {
            file.createNewFile();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            outputStreamWriter.write(gson.toJson(saveData));
            outputStreamWriter.close();
        } catch(IOException e) {
            System.out.println("ERROR: " + e.toString());
        }
    }

    public void load() {
        loadFactions();
        loadClaimedChunks();
        loadPlayerPowerRecords();
        loadPlayerActivityRecords();
        loadLockedBlocks();;
    }

    private void loadFactions() {
        PersistentData.getInstance().getFactions().clear();

        ArrayList<HashMap<String, String>> data = loadDataFromFilename(FILE_PATH + FACTIONS_FILE_NAME);

        for (Map<String, String> factionData : data){
            Faction newFaction = new Faction(factionData);
            PersistentData.getInstance().getFactions().add(newFaction);
        }
    }

    private void loadClaimedChunks() {
        PersistentData.getInstance().getClaimedChunks().clear();

        ArrayList<HashMap<String, String>> data = loadDataFromFilename(FILE_PATH + CHUNKS_FILE_NAME);

        for (Map<String, String> chunkData : data){
            ClaimedChunk chunk = new ClaimedChunk(chunkData);
            PersistentData.getInstance().getClaimedChunks().add(chunk);
        }
    }

    private void loadPlayerPowerRecords() {
        PersistentData.getInstance().getPlayerPowerRecords().clear();

        ArrayList<HashMap<String, String>> data = loadDataFromFilename(FILE_PATH + PLAYERPOWER_FILE_NAME);

        for (Map<String, String> powerRecord : data){
            PlayerPowerRecord player = new PlayerPowerRecord(powerRecord);
            PersistentData.getInstance().getPlayerPowerRecords().add(player);
        }
    }
    
    private void loadPlayerActivityRecords() {
        PersistentData.getInstance().getPlayerActivityRecords().clear();

        ArrayList<HashMap<String, String>> data = loadDataFromFilename(FILE_PATH + PLAYERACTIVITY_FILE_NAME);

        for (Map<String, String> powerRecord : data){
        	PlayerActivityRecord player = new PlayerActivityRecord(powerRecord);
            PersistentData.getInstance().getPlayerActivityRecords().add(player);
        }
    }

    private void loadLockedBlocks() {
        PersistentData.getInstance().getLockedBlocks().clear();

        ArrayList<HashMap<String, String>> data = loadDataFromFilename(FILE_PATH + LOCKED_BLOCKS_FILE_NAME);

        for (Map<String, String> lockedBlockData : data){
            LockedBlock lockedBlock = new LockedBlock(lockedBlockData);
            PersistentData.getInstance().getLockedBlocks().add(lockedBlock);
        }
    }

    private ArrayList<HashMap<String, String>> loadDataFromFilename(String filename) {
        try{
            Gson gson = new GsonBuilder().setPrettyPrinting().create();;
            JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8));
            return gson.fromJson(reader, LIST_MAP_TYPE);
        } catch (FileNotFoundException e) {
            // Fail silently because this can actually happen in normal use
        }
        return new ArrayList<>();
    }

}
