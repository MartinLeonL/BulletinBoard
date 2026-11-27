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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bulletinboard.database.DatabaseHandler;
import com.example.bulletinboard.database.entity.User;

import java.util.List;

public class LoginScreen extends Fragment {

    private boolean isPasswordVisible = false;
    DatabaseHandler db;

    public LoginScreen() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = DatabaseHandler.build(getContext());
        return inflater.inflate(R.layout.fragment_login_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View btnLogin = view.findViewById(R.id.btn_login);
        Button btnToRegister = view.findViewById(R.id.btn_toRegister);
        ImageButton btnTogglePassword = view.findViewById(R.id.btn_togglePassword);
        EditText etPassword = view.findViewById(R.id.et_createPassword);
        EditText etUsername = view.findViewById(R.id.et_username);

        if (btnLogin != null)
        {
            btnLogin.setOnClickListener(v -> attemptLogin(etPassword.getText().toString(), etUsername.getText().toString()));
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

    private void attemptLogin(String password, String username)
    {
        User user = db.userDAO().getUser(username);

        if(user == null)
        {
            Toast.makeText(getContext(), "Could not find an account with these details...", Toast.LENGTH_SHORT).show();
            return;
        }

        if(user.Password.equals(password))
        {
            DatabaseHandler.loginAs(user);
            navigateTo(new MapScreen());
            return;
        }
    }

    private void navigateTo(Fragment fragment) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).replaceFragment(fragment);
        }
    }
}