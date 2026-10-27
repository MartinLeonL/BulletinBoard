package com.example.bulletinboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class StudentRegisterScreen extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public StudentRegisterScreen() {
        // Required empty public constructor
    }

    public static StudentRegisterScreen newInstance(String param1, String param2) {
        StudentRegisterScreen fragment = new StudentRegisterScreen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_register_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnRegister = view.findViewById(R.id.btn_register);
        Button btnToLogin = view.findViewById(R.id.btn_toLogin);
        Button btnToOrgRegister = view.findViewById(R.id.btn_toOrgRegister);

        if (btnRegister != null) {
            btnRegister.setOnClickListener(v -> navigateTo(new MapScreen()));
        }

        if (btnToLogin != null) {
            btnToLogin.setOnClickListener(v -> navigateTo(new LoginScreen()));
        }

        if (btnToOrgRegister != null) {
            btnToOrgRegister.setOnClickListener(v -> navigateTo(new OrgRegisterScreen()));
        }
    }

    private void navigateTo(Fragment fragment) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).replaceFragment(fragment);
        }
    }
}