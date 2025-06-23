//File: Config.java
package objectrepo;

import com.google.gson.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Config {
    public String node_id;
    public String self_address;
    public String hb_address;
    public String database;
    public int ttl_seconds;
    public boolean bootstrap_primary;
    public List<Peer> peers;

    public static class Peer {
        public String id;
        public String host;
        public int biz_port;
        public int hb_port;
    }

    public static Config load(String path) throws IOException {
        String json = Files.readString(Paths.get(path));
        return new Gson().fromJson(json, Config.class);
    }
}
