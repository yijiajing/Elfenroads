public abstract class APIObject {

    /**
     * this class represents an object that is part of the API. Examples are:
     *
     * GameSession
     * GameService
     * User
     *
     *
     * it won't really have much functionality on its own. We're just going to use it to save some fields that we always need in API calls, such as the LobbyService host IP.
     */

    private String hostIP; // does not include port
    private String port;
    private String IPWithPort;

    protected APIObject (String pHostIP)
    {
        hostIP = pHostIP;
        port = "4242"; // pretty sure the port will always be this
        IPWithPort = hostIP + ":" + port;
    }

    public String getHostIP() {
        return hostIP;
    }

    public String getPort() {
        return port;
    }

    public String getHostIPWithPort() {
        return IPWithPort;
    }
}
