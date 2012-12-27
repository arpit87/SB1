package my.b1701.SB.ChatService;

import my.b1701.SB.ChatClient.IMessageListener;
import my.b1701.SB.ChatClient.IChatManagerListener;
import my.b1701.SB.ChatService.IChatAdapter;

interface IChatManager {

	IChatAdapter createChat(in String participant, in IMessageListener listener);
	void deleteChatNotification(IChatAdapter chat);
	void addChatCreationListener(IChatManagerListener listener);
    void removeChatCreationListener(IChatManagerListener listener);
    IChatAdapter getChat(String participant);
}