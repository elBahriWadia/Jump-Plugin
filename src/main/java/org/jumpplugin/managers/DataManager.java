package org.jumpplugin.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DataManager {

    private final File dataFile;
    private final Gson gson;
    private Map<String, Object> data = new HashMap<>();

    public DataManager(File dataFolder) {
        this.dataFile = new File(dataFolder, "jump_data.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.data = new HashMap<>();

        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
                saveData(); // write empty JSON object
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        loadData(); // always load whatever exists
    }

    public void loadData() {
        try (FileReader reader = new FileReader(dataFile)) {
            Map<String, Object> loaded = gson.fromJson(reader, Map.class);
            if (loaded != null) {
                data = loaded;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveData() {
        try (FileWriter writer = new FileWriter(dataFile)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void setNested(String path, Object value) {
        loadData();

        String[] parts = path.split("\\.");
        Map<String, Object> current = data;

        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            Object next = current.get(part);

            if (!(next instanceof Map)) {
                next = new HashMap<String, Object>();
                current.put(part, next);
            }

            current = (Map<String, Object>) next;
        }

        current.put(parts[parts.length - 1], value);
        saveData();
    }

    public Object getNested(String path) {
        loadData();

        String[] parts = path.split("\\.");
        Map<String, Object> current = data;

        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            Object next = current.get(part);

            if (!(next instanceof Map)) {
                return null;
            }

            current = (Map<String, Object>) next;
        }

        return current.get(parts[parts.length - 1]);
    }

    public void set(String key, Object value) {
        // make sure we always have up-to-date data before writing
        loadData();
        data.put(key, value);
        saveData();
    }

    public Object get(String key) {
        loadData();
        return data.get(key);
    }
}

