package info.deskchan.core;

import org.junit.Test;
//import org.

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PluginPropertiesTest {

    @Test
    public void load() {
    }

    @Test
    public void merge() {
    }

    @Test
    public void saveReturnNull() {
        final PluginProxyInterface proxyInterface = null;
        PluginProperties properties = new PluginProperties(proxyInterface);

        //assertEquals(1, properties.save());

    }
}