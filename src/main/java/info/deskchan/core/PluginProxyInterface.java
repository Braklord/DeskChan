package info.deskchan.core;

public interface PluginProxyInterface extends MessageListener {

    /** Get name of plugin. **/
    String getId();

    /** Unload plugin from program immediately. This method is usually called from core. **/
    void unload();

    /** Send message through core.
     * @param message
     */
    void sendMessage(MessageData message);

    /** Send message at tag through core.
     * @param tag Tag
     * @param data Any data that will be sent with message, can be null
     */
    void sendMessage(String tag, Object data);

    /** Send message at tag through core. First listener will be called if receiver wants to response to your message.
     * Second listener will be called after all of receivers get your message and complete their work with it.
     * @param tag Tag
     * @param data Any data that will be sent with message, can be null
     * @param responseListener Response to your message
     * @param returnListener Listener tat will be called after all of receivers get your message
     */
    Object sendMessage(String tag, Object data, ResponseListener responseListener, ResponseListener returnListener);

    /** Send message at tag through core. Listener will be called if receiver wants to response to your message.
     * @param tag Tag
     * @param data Any data that will be sent with message, can be null
     * @param responseListener Response to your message
     */
    Object sendMessage(String tag, Object data, ResponseListener responseListener);

    /** Add listener to tag. All messages from everywhere in program will be received by this listener. */
    void addMessageListener(String tag, MessageListener listener);

    ///** Add typed listener to tag. All messages from everywhere in program will be received by this listener. */
    //fun <T> addTypedMessageListener(tag: String, listener: TypedMessageListener<T>)

    /** Remove listener to tag. */
    void removeMessageListener(String tag, MessageListener listener);

    ///** Remove typed listener to tag. */
    //fun <T> removeTypedMessageListener(tag: String, listener: TypedMessageListener<T>)

    /** Set alternative */
    void setAlternative(String srcTag, String dstTag, int priority);

    /** Delete alternative */
    void deleteAlternative(String srcTag, String drcTag);

    /** Call next alternative in the chain. */
    void callNextAlternative(String sender, String tag, String currentAlternative, Object data);

    /** Check if message sender is asking for advice. */
    boolean isAskingAnswer(String sender);

    /** Set timer.
     * @param delay Delay, ms
     * @param responseListener Function that will be called after delay
     * @return Id of timer  **/
    int setTimer(long delay, ResponseListener responseListener);

    /** Set cycled timer.
     * @param delay Delay, ms
     * @param count Count of cycles, -1 to infinite
     * @param responseListener Function that will be called after delay
     * @return Id of timer  **/
    int setTimer(long delay, int count, ResponseListener responseListener);

    /** Cancel timer by id. **/
    void cancelTimer(int id);

    /** Properties of plugin **/
    PluginProperties getProperties();

    /** Set path to resource bundle that you want to be used by your plugin.
     * @param path Path to resources folder
     */
    void setResourceBundle(String path);

    /** Set config field of your plugin. */
    void setConfigField(String key, Object value);

    /** Get config field of your plugin. */
    Object getConfigField(String key);

    /** Get resource string from resource bundles. Resources will be searched not only in bundle that you specified
     * but also in main bundle. */
    String getString(String key);

    /** Use this to get path to 'DeskChan' directory. **/
    Path rootDirPath();

    /** Use this to get path where plugin should store temporary user data. **/
    Path dataDirPath();

    /** Use this to get path to 'assets' folder, where stored program assets. **/
    Path assetsDirPath();

    /** Use this to get path to 'plugins/pluginId' folder, where stored plugin assets. **/
    Path pluginDirPath();

    /** Log text to file and console. **/
    void log(String text);

    /** Log stack and text of error thrown to file and console. **/
    void log(Throwable e);

    /** Log text to file and console.
     * @param text
     * @param level
     */
    void log(String text, LoggerLevel level);
}
