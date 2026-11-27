package com.example.bulletinboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.bulletinboard.database.DatabaseHandler;
import com.example.bulletinboard.database.entity.User;

import java.util.List;

public class StudentRegisterScreen extends Fragment {

    DatabaseHandler db;

    public StudentRegisterScreen() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = DatabaseHandler.build(getContext());
        return inflater.inflate(R.layout.fragment_student_register_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnRegister = view.findViewById(R.id.btn_registerStudent);
        Button btnToLogin = view.findViewById(R.id.btn_toLogin);
        Button btnToOrgRegister = view.findViewById(R.id.btn_toOrgRegister);

        if (btnRegister != null) {
            btnRegister.setOnClickListener(v -> attemptRegister(view));
        }

        if (btnToLogin != null) {
            btnToLogin.setOnClickListener(v -> navigateTo(new LoginScreen()));
        }

        if (btnToOrgRegister != null) {
            btnToOrgRegister.setOnClickListener(v -> navigateTo(new OrgRegisterScreen()));
        }
    }

    private void attemptRegister(View view)
    {
        List<User> createdUsers = db.userDAO().getAllUsers();

        EditText etFirstName = view.findViewById(R.id.et_firstName);
        EditText etLastName = view.findViewById(R.id.et_lastName);

        EditText etEmail = view.findViewById(R.id.et_createEmail);
        EditText etUsername = view.findViewById(R.id.et_createUsername);
        EditText etPassword = view.findViewById(R.id.et_createPassword);

        boolean exists = false;

        for(User u : createdUsers)
        {
            if(u.Email.equals(etEmail.getText().toString()) || u.Username.equals(etUsername.getText().toString()));
            {
                exists = true;
                break;
            }
        }

        if(!exists)
        {
            User newUser = new User(
                    etFirstName.getText().toString(),
                    etLastName.getText().toString(),
                    etUsername.getText().toString(),
                    etEmail.getText().toString(),
                    etPassword.getText().toString(),
                    -1);

            db.userDAO().insertUser(newUser);
            db.loginAs(newUser);

            navigateTo(new MapScreen());
        }
    }

    private void navigateTo(Fragment fragment) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).replaceFragment(fragment);
        }
    }
}