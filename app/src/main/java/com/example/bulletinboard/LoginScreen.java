package com.example.bulletinboard;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
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

import com.google.firebase.firestore.DocumentSnapshot;

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
        EditText etPassword = view.findViewById(R.id.et_createPassword);
        EditText etUsername = view.findViewById(R.id.et_username);

        if (btnLogin != null && etUsername != null && etPassword != null) {
            btnLogin.setOnClickListener(v -> attemptLogin(
                    etUsername.getText().toString().trim(),
                    etPassword.getText().toString().trim()
            ));
        }

        if (btnToRegister != null) {
            String text = btnToRegister.getText().toString();
            SpannableString spannable = new SpannableString(text);
            spannable.setSpan(new UnderlineSpan(), 0, spannable.length(), 0);
            btnToRegister.setText(spannable);
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

    private void attemptLogin(String identifier, String password) {
        if (identifier.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        String inputLower = identifier.toLowerCase();

        // If user typed an email directly (e.g. john@ucf.edu)
        if (inputLower.contains("@")) {
            // Try signing in as student email first
            FirebaseHelper.getAuth().signInWithEmailAndPassword(inputLower, password)
                    .addOnSuccessListener(authResult -> {
                        Toast.makeText(getContext(), "Logged in successfully!", Toast.LENGTH_SHORT).show();
                        navigateTo(new MapScreen());
                    })
                    .addOnFailureListener(studentAuthErr -> {
                        // If student auth failed, try signing in as org auth email
                        String orgAuthEmail = inputLower.replace("@ucf.edu", "+org@ucf.edu");
                        FirebaseHelper.getAuth().signInWithEmailAndPassword(orgAuthEmail, password)
                                .addOnSuccessListener(authResult -> {
                                    Toast.makeText(getContext(), "Logged in successfully as Organization!", Toast.LENGTH_SHORT).show();
                                    navigateTo(new MapScreen());
                                })
                                .addOnFailureListener(orgAuthErr ->
                                        Toast.makeText(getContext(), "Login failed: Incorrect email or password.", Toast.LENGTH_SHORT).show());
                    });
            return;
        }

        // Search Firestore for matching student username or organization name
        FirebaseHelper.getDb().collection("users")
                .whereEqualTo("usernameLower", inputLower)
                .get()
                .addOnSuccessListener(usernameQuery -> {
                    if (!usernameQuery.isEmpty()) {
                        DocumentSnapshot doc = usernameQuery.getDocuments().get(0);
                        String docAuthEmail = doc.getString("authEmail");
                        if (docAuthEmail == null) docAuthEmail = doc.getString("email");

                        if (docAuthEmail != null) {
                            signInWithEmail(docAuthEmail, password);
                        } else {
                            Toast.makeText(getContext(), "User email not found.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Check if it matches an organization name
                        FirebaseHelper.getDb().collection("users")
                                .whereEqualTo("orgNameLower", inputLower)
                                .get()
                                .addOnSuccessListener(orgQuery -> {
                                    if (!orgQuery.isEmpty()) {
                                        DocumentSnapshot doc = orgQuery.getDocuments().get(0);
                                        String docAuthEmail = doc.getString("authEmail");
                                        if (docAuthEmail == null) docAuthEmail = doc.getString("email");

                                        if (docAuthEmail != null) {
                                            signInWithEmail(docAuthEmail, password);
                                        } else {
                                            Toast.makeText(getContext(), "Org email not found.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "Username or Organization not found.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(e -> Toast.makeText(getContext(), "Database error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Database error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void signInWithEmail(String authEmail, String password) {
        FirebaseHelper.getAuth().signInWithEmailAndPassword(authEmail, password)
                .addOnSuccessListener(authResult -> {
                    Toast.makeText(getContext(), "Logged in successfully!", Toast.LENGTH_SHORT).show();
                    navigateTo(new MapScreen());
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void navigateTo(Fragment fragment) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).replaceFragment(fragment);
        }
    }
}