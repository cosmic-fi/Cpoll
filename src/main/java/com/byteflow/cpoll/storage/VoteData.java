package com.byteflow.cpoll.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class VoteData {
    private List<Boolean> booleanList;

    public VoteData() {
        booleanList = new ArrayList<>();
    }

    public void addStatus(int index, boolean value) {
        if (index >= 0 && index <= booleanList.size()) {
            booleanList.add(index, value);
        } else {
            System.out.println("Invalid index: " + index);
        }
    }

    public List<Boolean> getStatus() {
        return booleanList;
    }

    public void saveStatusToFile(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(booleanList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadVoteSatus(String filename) {
        try (FileReader reader = new FileReader(filename)) {
            Gson gson = new Gson();
            Boolean[] array = gson.fromJson(reader, Boolean[].class);
            booleanList = new ArrayList<>(List.of(array));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
