package my.b1701.SB.ChatService;

import java.util.List;

import my.b1701.SB.Users.NearbyUser;

public interface ChatBase {
	
	void sendMessage(Message message);

	/**
	 * Get the participant of the chat
	 * @return the participant
	 */
	NearbyUser getParticipant();

	/**
	 * Add a message listener.
	 * @param listener the listener to add.
	 */
	
	String getState();

	void setOpen(boolean isOpen);

	boolean isOpen();

	void setState(String state);

	List<Message> getMessages();

}

