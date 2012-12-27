package my.b1701.SB.ChatService;

import my.b1701.SB.ChatService.Message;
import my.b1701.SB.ChatClient.IMessageListener;

interface IChatAdapter {

void sendMessage(in Message message);
void setOpen(in boolean value);
void addMessageListener(in IMessageListener listener);
void removeMessageListener(IMessageListener listen) ;
boolean isOpen();
String getParticipant();
List<Message> getMessages();
}