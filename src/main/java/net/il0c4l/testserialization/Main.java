package net.il0c4l.testserialization;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class Main extends JavaPlugin {

    private File file;
    private FileConfiguration data;
    private List<Entry> entries;
    private Executor executor;

    @Override
    public void onEnable(){
        saveDefaultConfig();

        registerConfigSerializable();

        data = returnFlatFile("data.yml");
        if(!data.contains("UUID")){
          data.createSection("UUID");
        }
        entries = getEntriesFromStorage();
        testCases();

        executor = Executors.newSingleThreadExecutor();
        aEntrySync();

        getLogger().log(Level.INFO, "Enabled plugin!");
    }

    private void testCases(){
        String command = "EntityDeath.EntityType:PIG.Material:DIAMOND_SWORD";
        String command2 = "EntityDeath.EntityType:COW.Material:DIAMOND_SWORD";

        UUID uuid1 = UUID.fromString("d30d52a8-eeb1-45b4-8758-f30dca914283");
        UUID uuid2 = UUID.fromString("ad80e069-363d-44cf-83f7-e3db464a68d4");

        List<SubEntry> subEntries = new ArrayList<>();

        subEntries.add(new SubEntry(uuid1, command, 2, false));
        subEntries.add(new SubEntry(uuid1, command2, 1, false));

        entries.add(new Entry(uuid1, subEntries, 1000.0));
        entries.add(new Entry(uuid2, subEntries, 2000.0));
    }

    private void registerConfigSerializable(){
        final Class[] serializable = {Entry.class, SubEntry.class};
        Arrays.stream(serializable).forEach(ConfigurationSerialization::registerClass);
    }

    private FileConfiguration returnFlatFile(String filename){
        file =  new File(getDataFolder().getPath(), filename);
        if(!file.exists()){
            try{
                file.createNewFile();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    private void aEntrySync(){
        Runnable run = () -> {
            try{
                Thread.sleep(15000L);
            } catch(InterruptedException e){
                e.printStackTrace();
            } finally{
                syncEntries();
                getLogger().log(Level.INFO, "SYNCED");
            }
        };
        executor.execute(() -> {
           while(isEnabled()) { run.run(); }
        });

    }

    public List<Entry> getEntriesFromStorage(){
        List<Entry> entries = new ArrayList<>();
        for(String sec : data.getConfigurationSection("UUID").getKeys(false)){
            Entry temp = (Entry) data.get("UUID." + sec);
            temp.setUUID(UUID.fromString(sec));
            entries.add(temp);
        }
        return entries;
    }

    public void syncEntries(){
        entries.forEach(it -> data.set("UUID." + it.getUUID().toString(), it));
        try{
            data.save(file);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public boolean entryExists(UUID uuid){
        return entries.stream().anyMatch(it -> it.getUUID().equals(uuid));
    }

    public List<Entry> getEntries(){
        return entries;
    }
}
