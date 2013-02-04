package my.b1701.SB.Fragments;

import my.b1701.SB.R;
import my.b1701.SB.HelperClasses.SmsHelper;
import my.b1701.SB.HelperClasses.ToastTracker;
import my.b1701.SB.Users.CurrentNearbyUsers;
import my.b1701.SB.Users.NearbyUser;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class SmsDialogFragment extends DialogFragment{
	
	Button btn_cancel;
	Button btn_sendsms;
	EditText smsTextView;
	String toUserFBID;
	TextView smsHeader;
	NearbyUser toNearbyUser;
	boolean foundNearby = true; //we are expecting to fund user nearby as smsicon available only for nearbyusers
	
	public SmsDialogFragment(String toFbID)
	{
		toUserFBID = toFbID;
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View dialogView = inflater.inflate(R.layout.dialog_sendsms, container);
        smsTextView = (EditText)dialogView.findViewById(R.id.smsText);
        smsHeader = (TextView)dialogView.findViewById(R.id.sms_header_text);
        toNearbyUser = CurrentNearbyUsers.getInstance().getNearbyUserWithFBID(toUserFBID);
        if(toNearbyUser == null)
        {
        	//this shouldnt happen but just in case
        	ToastTracker.showToast("Unable to send sms to this user");
        	foundNearby = false;
        }  
        else
        {
        	String first_name = toNearbyUser.getUserFBInfo().getFirstName();
        	smsHeader.setText("Send SMS to "+first_name);
        }
        
        
        Button dialogCloseButton = (Button)dialogView.findViewById(R.id.btn_cancelsms);
		// if button is clicked, close the custom dialog
		dialogCloseButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		
		Button sendSmsButton = (Button) dialogView.findViewById(R.id.btn_sendsms);
		// if button is clicked, close the custom dialog
		sendSmsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				if(foundNearby == false)
				{
					ToastTracker.showToast("Unable to send sms to this user");
				}
				else
				{
					String user_id = toNearbyUser.getUserLocInfo().getUserID();
					String smstext = smsTextView.getText().toString();
					if(smstext == "")
						ToastTracker.showToast("Cant send empty message");
					else
						SmsHelper.getInstance().sendSms(smstext,user_id);
				}
			}
		});
        
		return dialogView;
	}
	
	/*@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.setView(inflater.inflate(R.layout.fblogin_newdialog, null)).setTitle("Facebook Login")
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    	MapListActivityHandler.getInstance().getUnderlyingActivity().getFbConnector().loginToFB();
                    }
                })
                .setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });
        return builder.create();
    }*/
	
}
