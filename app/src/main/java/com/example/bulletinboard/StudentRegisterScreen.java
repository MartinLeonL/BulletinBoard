package com.example.bulletinboard;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StudentRegisterScreen extends Fragment {

    public StudentRegisterScreen() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_register_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnRegister = view.findViewById(R.id.btn_registerStudent);
        Button btnToLogin = view.findViewById(R.id.btn_toLogin);
        Button btnToOrgRegister = view.findViewById(R.id.btn_toOrgRegister);

        if (btnRegister != null) {
            btnRegister.setOnClickListener(v -> navigateTo(new MapScreen()));
        }

        if (btnToLogin != null) {
            String text = btnToLogin.getText().toString();
            SpannableString spannable = new SpannableString(text);
            spannable.setSpan(new UnderlineSpan(), 0, spannable.length(), 0);
            btnToLogin.setText(spannable);
            btnToLogin.setPaintFlags(btnToLogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            btnToLogin.setOnClickListener(v -> navigateTo(new LoginScreen()));
        }

        if (btnToOrgRegister != null) {
            String text = btnToOrgRegister.getText().toString();
            SpannableString spannable = new SpannableString(text);
            spannable.setSpan(new UnderlineSpan(), 0, spannable.length(), 0);
            btnToOrgRegister.setText(spannable);
            btnToOrgRegister.setPaintFlags(btnToOrgRegister.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            btnToOrgRegister.setOnClickListener(v -> navigateTo(new OrgRegisterScreen()));
        }
    }

    private void navigateTo(Fragment fragment) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).replaceFragment(fragment);
        }
    }
}