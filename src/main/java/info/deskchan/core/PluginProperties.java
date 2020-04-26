package info.deskchan.core;

import java.io.*;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import static java.lang.Double.parseDouble;

public class PluginProperties extends MessageDataMap {
    private PluginProxyInterface proxyInterface;

    PluginProperties(PluginProxyInterface proxyInterface) {
        super();
        this.proxyInterface  = proxyInterface;
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

        Path configPath = this.proxyInterface.getDataDirPath().resolve("config.properties");
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
            Set<String> keys = this.keySet();

            for (String key: keys) {
                if(!properties.keySet().contains(key)) {
                    remove(key);
                }
            }
        }

        properties.forEach((key, value) -> {
            Object obj = get(key);
            try {
                if (obj instanceof Number) {
                    put(key.toString(), parseDouble(value.toString()));
                } else if (obj instanceof Boolean) {
                    put(key.toString(), value.toString().toLowerCase().equals("true"));
                } else {
                    put(key.toString(), value.toString());
                }
            } catch (Exception e) {
                put(key.toString(), value.toString());
            }
        });
        this.proxyInterface.log("Properties loaded");
    }

    /** Saves properties to default location. **/
    public void save() {
        if (size() == 0) return;
        Path configPath = this.proxyInterface.getDataDirPath().resolve("config.properties");
        System.err.println(configPath.toString());
        try {
            Properties properties = new Properties();

            forEach((key, value) -> {
                properties.put(key, value.toString());
            });

            BufferedOutputStream ip = new BufferedOutputStream(new FileOutputStream(configPath));
            //OutputStream ip = new FileOutputStream(configPath);
            properties.store(ip, this.proxyInterface.getId() + " config");
            ip.close();

        } catch (Exception e) {
            this.proxyInterface.log(new IOException("Cannot save file: " + configPath, e));
        }
    }

}
