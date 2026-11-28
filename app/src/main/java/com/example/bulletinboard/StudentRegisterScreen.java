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

        if (btnToOrgRegister != null) {
            String text = btnToOrgRegister.getText().toString();
            SpannableString spannable = new SpannableString(text);
            spannable.setSpan(new UnderlineSpan(), 0, spannable.length(), 0);
            btnToOrgRegister.setText(spannable);
            btnToOrgRegister.setPaintFlags(btnToOrgRegister.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            btnToOrgRegister.setOnClickListener(v -> navigateTo(new OrgRegisterScreen()));
        }
    }

    private void attemptRegister(View view) {
        EditText etFirstName = view.findViewById(R.id.et_firstName);
        EditText etLastName = view.findViewById(R.id.et_lastName);
        EditText etEmail = view.findViewById(R.id.et_createEmail);
        EditText etUsername = view.findViewById(R.id.et_createUsername);
        EditText etPassword = view.findViewById(R.id.et_createPassword);

        String firstName = etFirstName != null ? etFirstName.getText().toString().trim() : "";
        String lastName = etLastName != null ? etLastName.getText().toString().trim() : "";
        String email = etEmail != null ? etEmail.getText().toString().trim() : "";
        String username = etUsername != null ? etUsername.getText().toString().trim() : "";
        String password = etPassword != null ? etPassword.getText().toString().trim() : "";

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all mandatory fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.toLowerCase().endsWith("@ucf.edu")) {
            Toast.makeText(getContext(), "Must use a valid @ucf.edu email address.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getContext(), "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
            return;
        }

        String usernameLower = username.toLowerCase();

        // Check if username is taken
        FirebaseHelper.getDb().collection("users")
                .whereEqualTo("usernameLower", usernameLower)
                .get()
                .addOnSuccessListener(query -> {
                    if (!query.isEmpty()) {
                        Toast.makeText(getContext(), "Username is already taken.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Register in Firebase Auth
                        FirebaseHelper.getAuth().createUserWithEmailAndPassword(email, password)
                                .addOnSuccessListener(authResult -> {
                                    FirebaseUser fbUser = authResult.getUser();
                                    if (fbUser == null) return;
                                    String uid = fbUser.getUid();
                                    User newUser = new User(uid, "student", email.toLowerCase(), firstName, lastName, username, usernameLower);

                                    // Store user profile in Firestore
                                    FirebaseHelper.getDb().collection("users").document(uid)
                                            .set(newUser)
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(getContext(), "Registration successful!", Toast.LENGTH_SHORT).show();
                                                navigateTo(new MapScreen());
                                            })
                                            .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to save profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                })
                                .addOnFailureListener(e -> Toast.makeText(getContext(), "Auth failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
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