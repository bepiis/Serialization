package net.il0c4l.testserialization;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SubEntry extends Entry {

    private String command;
    private int progress;
    private boolean done;

    public SubEntry(final UUID uuid, String command, int progress, boolean done){
        super(uuid);
        this.command = command;
        this.progress = progress;
        this.done = done;
    }

    public SubEntry(Map<String, Object> serialized){
        super((UUID) serialized.get("uuid"));
        this.command = (String) serialized.get("command");
        this.progress = (int) serialized.get("progress");
        this.done = (boolean) serialized.get("done");
    }

    @Override
    public Map<String, Object> serialize(){
        HashMap<String, Object> serializer = new HashMap<>();

        serializer.put("command", command);
        serializer.put("progress", progress);
        serializer.put("done", done);

        return serializer;
    }

    public String getCommand(){
        return command;
    }

    public int getProgress(){
        return progress;
    }

    public boolean isDone(){
        return done;
    }

    public void setDone(boolean done){
        this.done = done;
    }
}
