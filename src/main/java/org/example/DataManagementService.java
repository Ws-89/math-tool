package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DataManagementService {
    private final Gson gson = new Gson();

    public void save(List<BigDecimal> data, String fileName) throws IOException {
        List<List<BigDecimal>> history = load(fileName);
        history.add(data);

        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(history, writer);
        }
    }

    public List<List<BigDecimal>> load(String fileName) throws IOException {
        try (FileReader reader = new FileReader(fileName)) {
            Type listType = new TypeToken<List<List<BigDecimal>>>() {}.getType();
            List<List<BigDecimal>> history = gson.fromJson(reader, listType);
            return history != null ? history : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>(); // Jeśli plik nie istnieje, zwracamy pustą listę
        }
    }
}