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

import hu.danielgaldev.semestr.R;
import hu.danielgaldev.semestr.model.pojo.Semester;
import hu.danielgaldev.semestr.model.pojo.Subject;

public class NewSubjectDialogFragment extends DialogFragment {

    public static final String TAG = "NewSubjectDialogFragment";
    private EditText nameEditText;
    private EditText creditsEditText;
    private Long semesterId;

    public interface NewSubjectDialogListener {
        void onSubjectCreated(Subject newItem);
    }

    private NewSubjectDialogListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof NewSubjectDialogListener) {
            listener = (NewSubjectDialogListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the NewSubjectDialogListener interface!");
        }
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            semesterId = bundle.getLong("semesterId", 0);
        }
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_subject, null);
        nameEditText = contentView.findViewById(R.id.SubjectNameEditText);
        creditsEditText = contentView.findViewById(R.id.SubjectCreditsEditText);
        return contentView;
    }

    private boolean isValid() {
        if (nameEditText.getText().equals("")) return false;
        try {
            int i = Integer.parseInt(creditsEditText.getText().toString());
        } catch (NumberFormatException | NullPointerException nfe) {
            // TODO error message
            return false;
        }
        return true;
    }

    private Subject getSubject() {
        Subject sub = new Subject(
                nameEditText.getText().toString(),
                Integer.parseInt(creditsEditText.getText().toString()),
                semesterId);
        return sub;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.new_subject)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isValid()) {
                            listener.onSubjectCreated(getSubject());
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }
}
