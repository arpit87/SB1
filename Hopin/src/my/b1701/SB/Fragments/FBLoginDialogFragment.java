package my.b1701.SB.Fragments;

import my.b1701.SB.R;
import my.b1701.SB.ActivityHandlers.MapListActivityHandler;
import my.b1701.SB.HelperClasses.ThisUserConfig;
import my.b1701.SB.Util.StringUtils;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


public class FBLoginDialogFragment extends DialogFragment{
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View dialogView = inflater.inflate(R.layout.fblogin_newdialog, container);
        
        ImageView dialogCloseButton = (ImageView)dialogView.findViewById(R.id.button_close_fb_login_dialog);
		// if button is clicked, close the custom dialog
		dialogCloseButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		
		Button fbLoginButton = (Button) dialogView.findViewById(R.id.signInViaFacebook);
		// if button is clicked, close the custom dialog
		fbLoginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				MapListActivityHandler.getInstance().getUnderlyingActivity().getFbConnector().loginToFB();
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
