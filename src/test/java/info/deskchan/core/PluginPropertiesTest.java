package info.deskchan.core;

import org.junit.Test;
//import org.

import java.util.Properties;

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
        //PluginProxyInterface proxyInterface = mock(PluginProxyInterface.class);
        Properties properties = mock(Properties.class);
        when(properties.size() == 1);


    }
}