package my.b1701.SB.ChatClient;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import my.b1701.SB.R;
import my.b1701.SB.Platform.Platform;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



public class SBChatListViewAdapter extends BaseAdapter {

	List<SBChatMessage> mListMessages = new ArrayList<SBChatMessage>();
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
	    if (convertView == null) {
		LayoutInflater inflater = (LayoutInflater) Platform.getInstance().getContext().getSystemService(Platform.getInstance().getContext().LAYOUT_INFLATER_SERVICE);
		chatRowView = inflater.inflate(R.layout.chat_msg_row, null);
	    } else {
	    chatRowView = convertView;
	    }
	    DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);
	    SBChatMessage msg = mListMessages.get(position);
	    TextView msgName = (TextView) chatRowView.findViewById(R.id.chatmessagename);
	    msgName.setText(msg.getName());
	    msgName.setTextColor(Color.WHITE);
	    msgName.setError(null);
	    TextView msgText = (TextView) chatRowView.findViewById(R.id.chatmessagetext);
	    msgText.setText(msg.getMessage());
	    //registerForContextMenu(msgText);
	    TextView msgDate = (TextView) chatRowView.findViewById(R.id.chatmessagedate);
	    if (msg.getTimestamp() != null) {
		String date = msg.getTimestamp();
		msgDate.setText(date);
	    }
	    if (msg.isError()) {
		String err = "#some error!";
		msgName.setText(err);
		msgName.setTextColor(Color.RED);
		msgName.setError(err);
	    }
	    return chatRowView;
	}
    }

