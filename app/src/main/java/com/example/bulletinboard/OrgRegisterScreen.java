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
import com.example.bulletinboard.database.entity.Organization;
import com.example.bulletinboard.database.entity.User;
import com.example.bulletinboard.database.relations.OrganizationWithDetails;

import java.util.List;

public class OrgRegisterScreen extends Fragment {

    DatabaseHandler db;

    public OrgRegisterScreen() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = DatabaseHandler.build(getContext());
        return inflater.inflate(R.layout.fragment_org_register_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnRegister = view.findViewById(R.id.btn_registerOrg);
        Button btnToLogin = view.findViewById(R.id.btn_toLogin);
        Button btnToStudentRegister = view.findViewById(R.id.btn_toStudentRegister);

        if (btnRegister != null) {
            btnRegister.setOnClickListener(v -> attemptRegister(view));
        }

        if (btnToLogin != null) {
            btnToLogin.setOnClickListener(v -> navigateTo(new LoginScreen()));
        }

        if (btnToStudentRegister != null) {
            btnToStudentRegister.setOnClickListener(v -> navigateTo(new StudentRegisterScreen()));
        }
    }

    private void attemptRegister(View view)
    {
        // TODO:
        // Org name, Org Email, and Org Password are the same ids as users..
        // Has potential to cause overlap issues but I'm not sure yet.
        // Remember this if there's issues with not saving data correctly
        EditText etEmail = view.findViewById(R.id.et_createEmail);
        EditText etUsername = view.findViewById(R.id.et_createUsername);
        EditText etPassword = view.findViewById(R.id.et_createPassword);

        OrganizationWithDetails existingOrg = db.organizationDAO().getOrganization(etUsername.getText().toString());

        if(existingOrg == null)
        {
            User newUser = new User(
                    "", // Org has no first name
                    "", // Org has no last name
                    etUsername.getText().toString(),
                    etEmail.getText().toString(),
                    etPassword.getText().toString(),
                    -1);

            Organization newOrg = new Organization(
                    etUsername.getText().toString(),
                    "Placeholder", // No functionality for type yet...
                    "No description yet...");

            db.userDAO().insertUser(newUser);
            db.organizationDAO().insert(newOrg);
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