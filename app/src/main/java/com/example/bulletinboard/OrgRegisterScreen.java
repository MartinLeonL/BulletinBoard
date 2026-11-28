package com.example.bulletinboard;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bulletinboard.models.User;
import com.google.firebase.auth.FirebaseUser;

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

        Button btnRegister = view.findViewById(R.id.btn_registerOrg);
        Button btnToLogin = view.findViewById(R.id.btn_toLogin);
        Button btnToStudentRegister = view.findViewById(R.id.btn_toStudentRegister);

        if (btnRegister != null) {
            btnRegister.setOnClickListener(v -> attemptRegister(view));
        }

        if (btnToLogin != null) {
            String text = btnToLogin.getText().toString();
            SpannableString spannable = new SpannableString(text);
            spannable.setSpan(new UnderlineSpan(), 0, spannable.length(), 0);
            btnToLogin.setText(spannable);
            btnToLogin.setPaintFlags(btnToLogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            btnToLogin.setOnClickListener(v -> navigateTo(new LoginScreen()));
        }

        if (btnToStudentRegister != null) {
            String text = btnToStudentRegister.getText().toString();
            SpannableString spannable = new SpannableString(text);
            spannable.setSpan(new UnderlineSpan(), 0, spannable.length(), 0);
            btnToStudentRegister.setText(spannable);
            btnToStudentRegister.setPaintFlags(btnToStudentRegister.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            btnToStudentRegister.setOnClickListener(v -> navigateTo(new StudentRegisterScreen()));
        }
    }

    private void attemptRegister(View view) {
        EditText etOrgName = view.findViewById(R.id.et_createUsername);
        EditText etEmail = view.findViewById(R.id.et_createEmail);
        EditText etPassword = view.findViewById(R.id.et_createPassword);

        String orgName = etOrgName != null ? etOrgName.getText().toString().trim() : "";
        String email = etEmail != null ? etEmail.getText().toString().trim().toLowerCase() : "";
        String password = etPassword != null ? etPassword.getText().toString().trim() : "";

        if (orgName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.endsWith("@ucf.edu")) {
            Toast.makeText(getContext(), "Must use a valid @ucf.edu email address.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getContext(), "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
            return;
        }

        String orgNameLower = orgName.toLowerCase();

        // 1. Check if Organization Name is taken
        FirebaseHelper.getDb().collection("users")
                .whereEqualTo("orgNameLower", orgNameLower)
                .get()
                .addOnSuccessListener(orgQuery -> {
                    if (!orgQuery.isEmpty()) {
                        Toast.makeText(getContext(), "Organization name is already registered.", Toast.LENGTH_SHORT).show();
                    } else {
                        // 2. Check if an organization account already exists for this email
                        FirebaseHelper.getDb().collection("users")
                                .whereEqualTo("email", email)
                                .whereEqualTo("accountType", "org")
                                .get()
                                .addOnSuccessListener(emailQuery -> {
                                    if (!emailQuery.isEmpty()) {
                                        Toast.makeText(getContext(), "An organization account is already registered with this email.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Register in Firebase Auth using org alias email
                                        String authEmail = email.replace("@ucf.edu", "+org@ucf.edu");

                                        FirebaseHelper.getAuth().createUserWithEmailAndPassword(authEmail, password)
                                                .addOnSuccessListener(authResult -> {
                                                    FirebaseUser fbUser = authResult.getUser();
                                                    if (fbUser == null) return;
                                                    String uid = fbUser.getUid();
                                                    User newOrg = new User(uid, "org", email, orgName, orgNameLower, "No description provided yet.", 0);
                                                    newOrg.setAuthEmail(authEmail);

                                                    // Store org profile in Firestore
                                                    FirebaseHelper.getDb().collection("users").document(uid)
                                                            .set(newOrg)
                                                            .addOnSuccessListener(aVoid -> {
                                                                Toast.makeText(getContext(), "Organization registered successfully!", Toast.LENGTH_SHORT).show();
                                                                navigateTo(new MapScreen());
                                                            })
                                                            .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to save profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                                })
                                                .addOnFailureListener(e -> Toast.makeText(getContext(), "Auth failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                    }
                                })
                                .addOnFailureListener(e -> Toast.makeText(getContext(), "Database error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Database error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void navigateTo(Fragment fragment) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).replaceFragment(fragment);
        }
    }
}