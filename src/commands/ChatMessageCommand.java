package commands;

import loginwindow.ChatBoxGUI;

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
        String msgPlusName = senderName + ": " + message; // the GUI chatbox will automatically add a newline

    }




}
