package my.b1701.SB.ChatClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.b1701.SB.R;
import my.b1701.SB.HelperClasses.SBImageLoader;
import my.b1701.SB.HelperClasses.ThisUserConfig;
import my.b1701.SB.Platform.Platform;
import my.b1701.SB.Users.CurrentNearbyUsers;
import my.b1701.SB.Users.NearbyUser;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class SBChatListViewAdapter extends BaseAdapter {

	List<SBChatMessage> mListMessages = new ArrayList<SBChatMessage>();
	HashMap<Long,SBChatMessage> mHashMapSentNotDeliveredMsgs = new HashMap<Long,SBChatMessage>();
	String selfFBId = ThisUserConfig.getInstance().getString(ThisUserConfig.FBUID);
	String selfFirstName = ThisUserConfig.getInstance().getString(ThisUserConfig.FB_FIRSTNAME);
	String selfImageURL = ThisUserConfig.getInstance().getString(ThisUserConfig.FBPICURL);
	int chatMsgStatus = SBChatMessage.UNKNOWN ; 
	/**
	 * Returns the number of messages contained in the messages list.
	 * @return The number of messages contained in the messages list.
	 */
	@Override
	public int getCount() {
	    return mListMessages.size();	    
	}
	
	public void setMessage(int i,SBChatMessage msg) {
	     mListMessages.set(i, msg);
	}
	
	public void addMessage(SBChatMessage msg) {
	     mListMessages.add(msg);
	     //save all msgs that user is sending in a hash map to update their status
	     if(msg.getStatus() == SBChatMessage.SENDING)
	    	 mHashMapSentNotDeliveredMsgs.put(msg.getUniqueIdentifier(), msg);
	}
	
	public void updateMessageStatusWithUniqueID(long unique_ID,int status) {
		
	     SBChatMessage msg = mHashMapSentNotDeliveredMsgs.get(unique_ID);
	     if(msg!=null)
	     {
	    	 msg.setStatus(status);
	    	 if(status == SBChatMessage.DELIVERED)
	    		 mHashMapSentNotDeliveredMsgs.remove(msg);
	     }		
	}
	
	public void clearList()
	{
		mListMessages.clear();
	}
	
	public void addAllToList(List<SBChatMessage> listMessages)
	{
		mListMessages.addAll(listMessages);
	}

	/**
	 * Return an item from the messages list that is positioned at the position passed by parameter.
	 * @param position The position of the requested item.
	 * @return The item from the messages list at the requested position.
	 */
	@Override
	public Object getItem(int position) {
	    return mListMessages.get(position);
	}
	
	

	/**
	 * Return the id of an item from the messages list that is positioned at the position passed by parameter.
	 * @param position The position of the requested item.
	 * @return The id of an item from the messages list at the requested position.
	 */
	@Override
	public long getItemId(int position) {
	    return position;
	}

	/**
	 * Return the view of an item from the messages list.
	 * @param position The position of the requested item.
	 * @param convertView The old view to reuse if possible.
	 * @param parent The parent that this view will eventually be attached to.
	 * @return A View corresponding to the data at the specified position.
	 */

	public View getView(int position, View convertView, ViewGroup parent) {
	    View chatRowView;
	    TextView msgText ;
	    TextView msgStatus ;
	    ImageView imgView ;
	    String imageURL = "";
	    String statusText = "";
	    String time = "";
		LayoutInflater inflater = (LayoutInflater) Platform.getInstance().getContext().getSystemService(Platform.getInstance().getContext().LAYOUT_INFLATER_SERVICE);
		
	    SBChatMessage msg = mListMessages.get(position);	
	    if(msg.getInitiator().equalsIgnoreCase(selfFBId))
	    {
	    	chatRowView = inflater.inflate(R.layout.chat_msg_row_my, null);	    	
	    	imageURL = selfImageURL;	   
	    
	    }
	    else
	    {
	    	 chatRowView = inflater.inflate(R.layout.chat_msg_row_other, null);
	    	 NearbyUser n = CurrentNearbyUsers.getInstance().getNearbyUserWithFBID(msg.getInitiator());	    	  
	 	    if(n!=null)
	 	    {	 	
	 	    	imageURL = n.getUserFBInfo().getImageURL();		    	
	 	    }
	 	  /* else
		    {
		    	String err = "#User not in current nearby user list!";
		    	msgText.setText(err);
		    	msgText.setTextColor(Color.RED);
		    	msgText.setError(err);
		    }*/
	    }  
	    	msgText = (TextView) chatRowView.findViewById(R.id.chatmessagetext);
	    	msgStatus = (TextView) chatRowView.findViewById(R.id.chatmessagestatusandtime);
	    	imgView = (ImageView) chatRowView.findViewById(R.id.chat_msg_pic);
	    	SBImageLoader.getInstance().displayImageElseStub(imageURL, imgView, R.drawable.userpicicon);
		    msgText.setText(msg.getMessage());
		    
		    if (msg.getTimestamp() != null) {
				time = msg.getTime();				
			    }
		    
		    chatMsgStatus = msg.getStatus();		    
		    switch(chatMsgStatus)
		    {
		        case SBChatMessage.SENDING_FAILED:
		    	statusText = "Sending failed";
		    	break;
		    	case SBChatMessage.SENDING: //sending
		    	statusText = "Sending..";
		    	break;
		    	case SBChatMessage.SENT:
		    	statusText = "Sent@"+time;
		    	break;
		    	case SBChatMessage.DELIVERED:
		    	statusText = "Delivered@"+time;
			    break;	
		    	case SBChatMessage.RECEIVED:
			    statusText = "@"+time;
				break;
		    	case SBChatMessage.OLD:
				statusText = "@"+time;
				break;
				default:
				statusText = "";
		    }
		    //registerForContextMenu(msgText);
		    msgStatus.setText(statusText);
		    
	    if (msg.isError()) {
		String err = "#some error occured!";
		msgText.setText(err);
		msgText.setTextColor(Color.RED);
		msgStatus.setError("");
	    }
	    return chatRowView;
	}
    }

