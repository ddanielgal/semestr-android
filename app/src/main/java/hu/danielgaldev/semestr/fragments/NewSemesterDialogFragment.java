package hu.danielgaldev.semestr.fragments;

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
import android.widget.ArrayAdapter;
import android.widget.EditText;

import hu.danielgaldev.semestr.R;
import hu.danielgaldev.semestr.data.Semester;

public class NewSemesterDialogFragment extends DialogFragment {

    public static final String TAG = "NewSemesterDialogFragment";
    private EditText numberEditText;
    private EditText universityEditText;
    private EditText degreeEditText;

    public interface NewSemesterDialogListener {
        void onSemesterCreated(Semester newItem);
    }

    private NewSemesterDialogListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof NewSemesterDialogListener) {
            listener = (NewSemesterDialogListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the NewShoppingItemDialogListener interface!");
        }
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_semester, null);
        numberEditText = contentView.findViewById(R.id.SemesterNumberEditText);
        universityEditText = contentView.findViewById(R.id.UniversityEditText);
        degreeEditText = contentView.findViewById(R.id.DegreeEditText);
        return contentView;
    }

    private boolean isValid() {
        try {
            int i = Integer.parseInt(numberEditText.getText().toString());
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    private Semester getSemester() {
        Semester semester = new Semester();
        semester.number = Integer.parseInt(numberEditText.getText().toString());
        semester.university = universityEditText.getText().toString();
        semester.degree = degreeEditText.getText().toString();
        return semester;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.new_semester)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isValid()) {
                            listener.onSemesterCreated(getSemester());
                    }
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }
}
