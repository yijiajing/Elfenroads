package commands;

// will help us implement the chat feature
public class ChatMessageCommand implements GameCommand {

    private String message;
    private String senderName;

    public ChatMessageCommand (String pMessage, String pSenderName)
    {
        message = pMessage;
        senderName = pSenderName;
    }

    @Override
    public void execute()
    {
        // TODO: set up a GUI element to hold the chat
        // below is sample code for what it will look like once we pick one
        // GUITextPaneOrSomething.add(senderName + ": " + message + "\n");
        // GUITextPaneOrSomething.refresh();
    }




}
