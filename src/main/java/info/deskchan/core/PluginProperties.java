package info.deskchan.core;

import java.io.*;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import static java.lang.Double.parseDouble;

public class PluginProperties extends MessageDataMap {
    private final PluginProxyInterface proxyInterface;

    PluginProperties(PluginProxyInterface proxy) {
        proxyInterface  = proxy;
    }

    /** Loads properties from default location and overwrites current properties map. **/
    public void load() {
        load_impl(true);
    }

    /** Loads properties from default location and merges current properties map. **/
    void merge() {
        load_impl(false);
    }

    private void load_impl(boolean clear) {
        //Set<String, Object> keys = new HashSet<String, Object>();

        Path configPath = proxyInterface.dataDirPath().resolve("config.properties");
        // To synchronize HashMap: Map m = Collections.synchronizedMap(new HashMap(...));
        Properties properties = new Properties();
        try {
            //InputStream ip = new FileInputStream(configPath);
            // BufferedInputStream is faster them FileInputStream, because of making
            // less native calls to the OS, https://bit.ly/2VNjQ4q
            BufferedInputStream ip = new BufferedInputStream(new FileInputStream(configPath));
            properties.load(ip);
            ip.close();
        } catch (IOException e) {
            return;
        }

        if(clear) {
            // keysSet() returns a set containing keys of the specified map.
            Set<Object> keys = properties.keySet();

            for (Object key: keys) {
                if(!keys.contains(key)) {
                    properties.remove(key);
                }
            }
        }

        Set<Map.Entry<Object, Object>> property = properties.entrySet();

        for(Map.Entry m: property) {
            try {
                if (m.getValue() instanceof Number) {
                    properties.put(m.getKey().toString(), parseDouble(m.getValue().toString()));
                } else if (m.getValue() instanceof Boolean) {
                    properties.put(m.getKey().toString(), Boolean.valueOf(m.getValue().toString()));
                } else {
                    properties.put(m.getKey().toString(), m.getValue().toString());
                }
            } catch (Exception e) {
                proxyInterface.log("Properties loaded");
            }
        }
    }

    /** Saves properties to default location. **/
    public void save() {
        if (size() == 0) return;
        Path configPath = proxyInterface.dataDirPath().resolve("config.properties");
        try {
            Properties properties = new Properties();
            Set<Map.Entry<Object, Object>> property = properties.entrySet();
            for (Map.Entry m: property) {
                properties.put(m.getKey(), m.getValue().toString());
            }
            BufferedOutputStream ip = new BufferedOutputStream(new FileOutputStream(configPath));
            properties.store(ip, proxyInterface.getId() + " config");
            ip.close();

        } catch (Exception e) {
            proxyInterface.log(new IOException("Cannot save file: " + configPath, e));
        }
    }
}
