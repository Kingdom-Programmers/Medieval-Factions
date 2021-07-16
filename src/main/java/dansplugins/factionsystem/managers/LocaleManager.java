package dansplugins.factionsystem.managers;

import dansplugins.factionsystem.MedievalFactions;
import dansplugins.factionsystem.utils.Pair;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class LocaleManager {

    private static LocaleManager instance;

    private ArrayList<String> supportedLanguageIDs = new ArrayList<>(Arrays.asList("en-us", "es", "ru"));

    private ArrayList<String> keys = new ArrayList<>();
    private HashMap<String, String> strings = new HashMap<>();

    private String languageFolderPath;
    private String localizationFileName;
    private String localizationFilePath;

    private LocaleManager() {
        initializePaths();
    }

    private void initializePaths() {
        String pluginFolderPath = "./plugins/MedievalFactions/";
        languageFolderPath = pluginFolderPath + "languages/";
        localizationFileName = MedievalFactions.getInstance().getConfig().getString("languageid") + ".tsv";
        localizationFilePath = languageFolderPath + localizationFileName;
    }

    public static LocaleManager getInstance() {
        if (instance == null) {
            instance = new LocaleManager();
        }
        return instance;
    }

    public String getText(String key) {
        if (!keys.contains(key)) return String.format("[key '%s' not found]", key);
        return strings.get(key);
    }

    public void loadStrings() {

        if (isFilePresent(localizationFilePath)) {
            loadFromPluginFolder();
            System.out.println("DEBUG: Loaded from plugin folder!");
        }
        else {
            loadFromResource();
            System.out.println("DEBUG: Loaded from resource!");
        }
        System.out.println(String.format(getText("KeysLoaded"), keys.size()));
    }

    public void reloadStrings() {
        initializePaths();
        keys.clear();
        strings.clear();
        loadStrings();
    }

    public boolean isLanguageIDSupported(String ID) {
        return supportedLanguageIDs.contains(ID);
    }

    public String getSupportedLanguageIDsSeparatedByCommas() {

        String IDs = "";
        for (int i = 0; i < supportedLanguageIDs.size(); i++) {
            IDs = IDs + supportedLanguageIDs.get(i);
            if (i != supportedLanguageIDs.size() - 1) {
                IDs = IDs + ", ";
            }
        }
        return IDs;

    }

    private boolean isFilePresent(String path) {
        File file = new File(path);
        return file.exists();
    }

    private void loadFromPluginFolder() {
        File file = new File(localizationFilePath);
        try {

            // load from local language file
            loadFromFile(file);

            // update local language files
            updateCurrentLocalLanguageFile();

            // save
            saveToPluginFolder();

        } catch (Exception e) {
            System.out.println("DEBUG: Something went wrong loading from the plugin folder.");
            e.printStackTrace();
        }

    }

    private void loadFromFile(File file) {
//        System.out.println("DEBUG: Loading from " + file.toString());
        try {
            List<String> lines = Files.readAllLines(file.toPath());

            for (String line : lines) {
                Pair<String, String> pair = getPairFromLine(line);
                if (pair != null && !strings.containsKey(pair.getLeft())) {
                    strings.put(pair.getLeft(), pair.getRight());
                    keys.add(pair.getLeft());
                }
            }

        } catch (Exception e) {
            System.out.println("DEBUG: Something went wrong loading from file!");
            e.printStackTrace();
        }

    }

    // this should be called after loading from plugin folder
    private void updateCurrentLocalLanguageFile() {
        System.out.println("DEBUG: LocaleManager is updating supported local language files.");
        String ID = MedievalFactions.getInstance().getConfig().getString("languageid");
        if (isLanguageIDSupported(ID)) {
            InputStream inputStream;
            inputStream = getResourceAsInputStream(ID + ".tsv");
            loadMissingKeysFromInputStream(inputStream);
        }
        else {
            InputStream inputStream;
            inputStream = getResourceAsInputStream("en-us.tsv");
            loadMissingKeysFromInputStream(inputStream);
        }
    }

    private InputStream getResourceAsInputStream(String fileName) {
        return MedievalFactions.getInstance().getResource(fileName);
    }

    private void loadFromResource() {
        try {
            // get resource as input stream
            InputStream inputStream = getResourceAsInputStream(localizationFileName);
            loadMissingKeysFromInputStream(inputStream);
            saveToPluginFolder();
        } catch (Exception e) {
            System.out.println("DEBUG: Error loading from resource!");
            e.printStackTrace();
        }
    }

    private void loadMissingKeysFromInputStream(InputStream inputStream) {
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(reader);
        br.lines().forEach(line -> {
            Pair<String, String> pair = getPairFromLine(line);
            if (pair != null && !strings.containsKey(pair.getLeft())) { // if pair found and if key not already loaded
                strings.put(pair.getLeft(), pair.getRight());
                keys.add(pair.getLeft());
//                System.out.println(String.format("DEBUG: Loaded missing key %s from resources!", pair.getLeft()));
            }
        });
    }

    private Pair<String, String> getPairFromLine(String line) {
        String key = "";
        String value = "";

        int tabIndex = getIndexOfTab(line);

        if (tabIndex != -1) {
            key = line.substring(0, tabIndex);
            value = line.substring(tabIndex + 1);
            return new Pair<>(key, value);
        }
        else {
            return null;
        }

    }

    private int getIndexOfTab(String line) {
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '\t') {
                return i;
            }
        }
        return -1;
    }

    private void saveToPluginFolder() {

        sortKeys();

        try {
            File folder = new File(languageFolderPath);
            if (!folder.exists()) {
                if (!folder.mkdir()) {
                    System.out.println("DEBUG: Failed to create directory.");
                    return;
                }
            }
            File file = new File(localizationFilePath);
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    System.out.println("DEBUG: Failed to create file.");
                    return;
                }
            }
            try (BufferedWriter output = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), StandardCharsets.UTF_8))) {
                for (String key : keys) {
                    output.write(key + "\t" + strings.get(key) + "\n");
                }
            } catch (Exception ex) {
                System.out.println("DEBUG: Failed to write to file.");
            }
        } catch (Exception e) {
            System.out.println("DEBUG: There was a problem saving the strings.");
            e.printStackTrace();
        }
    }

    private void sortKeys() {
        Collections.sort(keys);
    }

}
