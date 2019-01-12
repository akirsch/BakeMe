package com.example.android.bakeme.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakeme.Constants;
import com.example.android.bakeme.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StepInstructionsFragment extends Fragment {

    private Unbinder mUnbinder;
    private String mStepInstructions;

    @BindView(R.id.preparation_step_instructions)
    TextView stepInstructionsView;

    public StepInstructionsFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null){
            mStepInstructions = savedInstanceState.getString(Constants.INSTRUCTIONS_KEY);
        } else {
            mStepInstructions = getArguments().getString(Constants.STEP_LONG_DESC_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragement_step_instructions, container, false);

        mUnbinder = ButterKnife.bind(this, rootView);



        stepInstructionsView.setText(mStepInstructions);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.INSTRUCTIONS_KEY, mStepInstructions);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
