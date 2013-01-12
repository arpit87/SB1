package my.b1701.SB.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;
import my.b1701.SB.R;
import my.b1701.SB.Util.StringUtils;

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
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.username_dialog, null))
                .setPositiveButton(R.string.set_username, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText userName = (EditText) UserNameDialogFragment.this.getDialog().findViewById(R.id.username);
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
    }
}
