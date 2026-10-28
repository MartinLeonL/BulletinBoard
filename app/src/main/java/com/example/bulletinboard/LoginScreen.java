package com.example.bulletinboard;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LoginScreen extends Fragment {

    private boolean isPasswordVisible = false;

    public LoginScreen() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View btnLogin = view.findViewById(R.id.btn_login);
        Button btnToRegister = view.findViewById(R.id.btn_toRegister);
        ImageButton btnTogglePassword = view.findViewById(R.id.btn_togglePassword);
        EditText etPassword = view.findViewById(R.id.et_password);

        if (btnLogin != null) {
            btnLogin.setOnClickListener(v -> navigateTo(new MapScreen()));
        }

        if (btnToRegister != null) {
            btnToRegister.setPaintFlags(btnToRegister.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            btnToRegister.setOnClickListener(v -> navigateTo(new StudentRegisterScreen()));
        }

        if (btnTogglePassword != null && etPassword != null) {
            btnTogglePassword.setOnClickListener(v -> {
                isPasswordVisible = !isPasswordVisible;
                if (isPasswordVisible) {
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                etPassword.setSelection(etPassword.getText().length());
            });
        }
    }

    private void navigateTo(Fragment fragment) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).replaceFragment(fragment);
        }
    }
}