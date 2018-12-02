package hu.danielgaldev.semestr.fragments.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import hu.danielgaldev.semestr.R;
import hu.danielgaldev.semestr.model.pojo.RequirementType;

public class NewReqTypeDialogFragment extends DialogFragment {

    public static final String TAG = "NewReqTypeDialogFragment";
    private EditText nameEditText;
    private NumberPicker weightNumberPicker;

    public interface NewReqTypeDialogListener {
        void onReqTypeCreated(RequirementType reqType);
    }

    private NewReqTypeDialogListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof NewReqTypeDialogFragment.NewReqTypeDialogListener) {
            listener = (NewReqTypeDialogFragment.NewReqTypeDialogListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the NewReqTypeDialogListener interface!");
        }
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_reqtype, null);
        weightNumberPicker = contentView.findViewById(R.id.WeightNumberPicker);
        weightNumberPicker.setMinValue(1);
        weightNumberPicker.setMaxValue(14);
        nameEditText = contentView.findViewById(R.id.ReqTypeNameEditText);
        return contentView;
    }

    private RequirementType getReqType() {
        RequirementType reqType = new RequirementType();
        reqType.name = nameEditText.getText().toString();
        reqType.weight = weightNumberPicker.getValue();
        return reqType;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.add_new_requirement_type)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onReqTypeCreated(getReqType());
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }
}
