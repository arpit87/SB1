package my.b1701.SB.Fragments;

import my.b1701.SB.R;
import my.b1701.SB.HelperClasses.ThisUserConfig;
import my.b1701.SB.Users.ThisUser;
import my.b1701.SB.Util.StringUtils;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class UserNameDialogFragment extends DialogFragment {

    public interface UserNameDialogListener {
        public void onSetUserNameClick(String userName);
    }

    UserNameDialogListener mListener;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try {
            mListener = (UserNameDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement UserNameDialogListener");
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View dialogView = inflater.inflate(R.layout.username_dialog, container);
        final EditText userNameView = (EditText) dialogView.findViewById(R.id.listviewusername);
        userNameView.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        Button notnowButton = (Button)dialogView.findViewById(R.id.cancelenterusernamebutton);
		// if button is clicked, close the custom dialog
       /* notnowButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});*/
		
		Button enterButton = (Button) dialogView.findViewById(R.id.enterusernamebutton);
		// if button is clicked, close the custom dialog
		enterButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
                String userNameText = userNameView.getText().toString();
                if (StringUtils.isBlank(userNameText)) {
                    return;
                }

               ThisUserConfig.getInstance().putString(ThisUserConfig.USERNAME, userNameText);
				dismiss();
				
			}
		});
        
		return dialogView;
	}

  /*  @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.username_dialog, null))
                .setPositiveButton(R.string.set_username, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText userName = (EditText) UserNameDialogFragment.this.getDialog().findViewById(R.id.listviewusername);
                        String userNameText = userName.getText().toString();
                        if (StringUtils.isBlank(userNameText)) {
                            return;
                        }

                        mListener.onSetUserNameClick(userNameText);
                        UserNameDialogFragment.this.getDialog().cancel();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });
        return builder.create();
    }*/
}
