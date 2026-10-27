package com.example.bulletinboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class OrgRegisterScreen extends Fragment {

    public OrgRegisterScreen() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_org_register_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnRegister = view.findViewById(R.id.btn_register);
        Button btnToLogin = view.findViewById(R.id.btn_toLogin);
        Button btnToStudentRegister = view.findViewById(R.id.btn_toStudentRegister);

        if (btnRegister != null) {
            btnRegister.setOnClickListener(v -> navigateTo(new MapScreen()));
        }

        if (btnToLogin != null) {
            btnToLogin.setOnClickListener(v -> navigateTo(new LoginScreen()));
        }

        if (btnToStudentRegister != null) {
            btnToStudentRegister.setOnClickListener(v -> navigateTo(new StudentRegisterScreen()));
        }
    }

    private void navigateTo(Fragment fragment) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).replaceFragment(fragment);
        }
    }
}