package com.chotix.mydiary1.main;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.chotix.mydiary1.R;
import com.chotix.mydiary1.entries.DiaryActivity;
import com.chotix.mydiary1.shared.ThemeManager;

public class NoEntriesDialogFragment extends DialogFragment implements View.OnClickListener{
    /**
     * UI
     */

    private TextView TV_no_entries_create;

    public static NoEntriesDialogFragment newInstance(long topic, String diaryTitle) {
        Bundle args = new Bundle();
        NoEntriesDialogFragment fragment = new NoEntriesDialogFragment();
        args.putLong("topicId", topic);
        args.putString("diaryTitle", diaryTitle);
        args.putBoolean("has_entries", false);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog= super.onCreateDialog(savedInstanceState);
        return dialog;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View rootView = inflater.inflate(R.layout.dialog_fragment_no_entries, container);

        TV_no_entries_create = rootView.findViewById(R.id.TV_no_entries_create);
        TV_no_entries_create.setOnClickListener(this);
        SpannableString content = new SpannableString(TV_no_entries_create.getText());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        TV_no_entries_create.setText(content);
        TV_no_entries_create.setTextColor(ThemeManager.getInstance().getThemeMainColor(getActivity()));

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.TV_no_entries_create:
                Intent goEntriesPageIntent = new Intent(getActivity(), DiaryActivity.class);
                goEntriesPageIntent.putExtras(getArguments());
                getActivity().startActivity(goEntriesPageIntent);
                dismiss();
                break;
        }
    }
}
