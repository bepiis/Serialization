package net.il0c4l.testserialization;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;
import java.util.stream.Collectors;

public class Entry implements ConfigurationSerializable {

    private UUID uuid;
    private double points;
    private boolean complete;
    private List<SubEntry> subEntries;

    public Entry(final UUID uuid){
        this.uuid = uuid;
        this.complete = false;
        subEntries = new ArrayList<>();
    }

    public Entry(UUID uuid, List<SubEntry> subEntries, double points){
        this.uuid = uuid;
        this.subEntries = subEntries;
        this.points = points;
        this.complete = false;
    }

    public Entry(Map<String, Object> serialized){
        this.points = (double) serialized.get("points");
        this.complete = (boolean) serialized.get("complete");

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> mappedSubEntries = (List<Map<String, Object>>) serialized.get("subentries");

        subEntries = mappedSubEntries.stream().map(SubEntry::new).collect(Collectors.toList());

    }

    public Entry(UUID uuid, boolean complete){
        this.uuid = uuid;
        this.complete = complete;
    }

    @Override
    public Map<String, Object> serialize(){
        HashMap<String, Object> serializer = new HashMap<>();

        serializer.put("points", points);
        serializer.put("complete", complete);

        List<Map<String, Object>> tempSerSubEntries = subEntries.stream().map(SubEntry::serialize).collect(Collectors.toList());

        serializer.put("subentries", tempSerSubEntries);

        return serializer;
    }

    public UUID getUUID(){
        return uuid;
    }

    public void setUUID(UUID uuid){
        this.uuid = uuid;
    }

    public double getPoints(){
        return points;
    }

    public List<SubEntry> getSubEntries(){
        return subEntries;
    }



}
