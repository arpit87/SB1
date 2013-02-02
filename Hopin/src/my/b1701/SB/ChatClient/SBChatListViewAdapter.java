package my.b1701.SB.ChatClient;

import java.util.ArrayList;
import java.util.List;

import my.b1701.SB.R;
import my.b1701.SB.HelperClasses.ThisUserConfig;
import my.b1701.SB.Platform.Platform;
import my.b1701.SB.Users.CurrentNearbyUsers;
import my.b1701.SB.Users.NearbyUser;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class SBChatListViewAdapter extends BaseAdapter {

	List<SBChatMessage> mListMessages = new ArrayList<SBChatMessage>();
	String selfFBId = ThisUserConfig.getInstance().getString(ThisUserConfig.FBUID);
	String selfFirstName = ThisUserConfig.getInstance().getString(ThisUserConfig.FB_FIRSTNAME);
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
	    //here we are inflating everytime..inefficient?
	    if (convertView == null) {
		LayoutInflater inflater = (LayoutInflater) Platform.getInstance().getContext().getSystemService(Platform.getInstance().getContext().LAYOUT_INFLATER_SERVICE);
		chatRowView = inflater.inflate(R.layout.chat_msg_row, null);
	    } else {
	    chatRowView = convertView;
	    }	       
	    	    
	    TextView msgName = (TextView) chatRowView.findViewById(R.id.chatmessagename);
	    TextView msgText = (TextView) chatRowView.findViewById(R.id.chatmessagetext);
	    TextView msgDate = (TextView) chatRowView.findViewById(R.id.chatmessagedate);
	    
	    SBChatMessage msg = mListMessages.get(position);	
	    if(msg.getInitiator().equalsIgnoreCase(selfFBId))
	    {
	    	chatRowView.setBackgroundResource(R.drawable.chat_msg_frame_transred);
	    	msgName.setText(selfFirstName);
	    }
	    else
	    {
	    	 NearbyUser n = CurrentNearbyUsers.getInstance().getNearbyUserWithFBID(msg.getInitiator());
	 	    if(n!=null)
	 	    {	 	    
		    	if(n.getUserOtherInfo().isOfferingRide())
		    		chatRowView.setBackgroundResource(R.drawable.chat_msg_frame_transgreen);
		    	else
		    		chatRowView.setBackgroundResource(R.drawable.chat_msg_frame_transblue);	
		    	
		    	msgName.setText(n.getUserFBInfo().getFirstName());
	 	    }
	 	   else
		    {
		    	String err = "#User not in current nearby user list!";
		    	msgText.setText(err);
		    	msgText.setTextColor(Color.RED);
		    	msgText.setError(err);
		    }
	    }  
	    
		    msgText.setText(msg.getMessage());
		   
		    //registerForContextMenu(msgText);
		   	    
		    if (msg.getTimestamp() != null) {
			String time = msg.getTime();
			msgDate.setText("@"+time);
		    }
	     
	    
	    if (msg.isError()) {
		String err = "#some error occured!";
		msgName.setText(err);
		msgName.setTextColor(Color.RED);
		msgDate.setError("");
	    }
	    return chatRowView;
	}
    }

