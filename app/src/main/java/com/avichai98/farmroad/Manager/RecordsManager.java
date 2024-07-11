package com.avichai98.farmroad.Manager;

import androidx.annotation.NonNull;
import java.util.ArrayList;

public class RecordsManager {
    private static final int ARRAY_SIZE = 10;
    private final ArrayList<Record> records;
    private static volatile RecordsManager instance;


    public RecordsManager() {
        records = new ArrayList<>();
    }

    public static RecordsManager init(){
        if (instance == null){
            synchronized (RecordsManager.class){
                if (instance == null){
                    instance = new RecordsManager();
                }
            }
        }
        return getInstance();
    }

    public static RecordsManager getInstance() {
        return instance;
    }

    public  ArrayList<Record> getRecordsArrayList() {
        return records;
    }



    public void addRecord(Record record){

        if (records.size() < ARRAY_SIZE){
            records.add(record);
        }

        else{
            if(record.getScore() <= records.get(records.size() - 1).getScore())
                return;

            records.remove(records.size() - 1); // Remove the last record
            records.add(record);                      // Add the new record
        }

        // Sort records based on the score of each record
        records.sort((r1, r2) -> {
            return Integer.compare(r2.getScore(), r1.getScore()); // Descending order
        });
    }


    @NonNull
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i=0;i<records.size();i++) {
            s.append(records.get(i).toString());
        }
        return s.toString();
    }

    public void setRecordsManager(RecordsManager recordsListFromJson) {
        instance = recordsListFromJson;
    }
}
