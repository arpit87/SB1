package my.b1701.SB.Fragments;

import my.b1701.SB.R;
import my.b1701.SB.ActivityHandlers.MapListActivityHandler;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class FBLoginDialogFragment extends DialogFragment{
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.fblogin_dialog, container);
        dialogView.setVisibility(STYLE_NO_TITLE);    
        ImageView dialogCloseButton = (ImageView)dialogView.findViewById(R.id.button_close_fb_login_dialog);
		// if button is clicked, close the custom dialog
		dialogCloseButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getDialog().cancel();
			}
		});
		
		Button fbLoginButton = (Button) dialogView.findViewById(R.id.signInViaFacebook);
		// if button is clicked, close the custom dialog
		fbLoginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getDialog().cancel();
				MapListActivityHandler.getInstance().getUnderlyingActivity().buttonOnMapClick(v);
			}
		});
        
		return dialogView;
	}
	
}
