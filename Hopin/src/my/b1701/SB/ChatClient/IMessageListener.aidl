package my.b1701.SB.ChatClient;


import my.b1701.SB.ChatService.IChatAdapter;
import my.b1701.SB.ChatService.Message;

interface IMessageListener {

	void processMessage(in IChatAdapter chat,in Message msg);
}
