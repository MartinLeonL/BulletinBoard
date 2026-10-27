package com.example.bulletinboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class StudentAccountScreen extends Fragment {

    public StudentAccountScreen() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_account_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnSignOut = view.findViewById(R.id.btn_signOut);
        if (btnSignOut != null) {
            btnSignOut.setOnClickListener(v -> navigateTo(new LoginScreen()));
        }

        Button btnTEMPorgInfo = view.findViewById(R.id.btn_TEMPorgInfo);
        if (btnTEMPorgInfo != null) {
            btnTEMPorgInfo.setOnClickListener(v -> new OrgInfoPopup().show(getParentFragmentManager(), "OrgInfoPopup"));
        }
    }

    private void navigateTo(Fragment fragment) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).replaceFragment(fragment);
        }
    }
}