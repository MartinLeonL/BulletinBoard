package com.example.bulletinboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bulletinboard.models.User;

public class OrgAccountScreen extends Fragment {

    public OrgAccountScreen() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_org_account_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnSignOut = view.findViewById(R.id.btn_signOut);
        if (btnSignOut != null) {
            btnSignOut.setOnClickListener(v -> logout());
        }

        Button btnUpdate = view.findViewById(R.id.btn_update);
        EditText etDescription = view.findViewById(R.id.editTextText);

        if (btnUpdate != null && etDescription != null) {
            btnUpdate.setOnClickListener(v -> updateDescription(etDescription.getText().toString().trim()));
        }

        loadProfile(view);
    }

    private void loadProfile(View view) {
        String uid = FirebaseHelper.getCurrentUid();
        if (uid == null) {
            navigateTo(new LoginScreen());
            return;
        }

        TextView tvOrgName = view.findViewById(R.id.org_name_title);
        TextView tvAccountType = view.findViewById(R.id.username);
        EditText etDescription = view.findViewById(R.id.editTextText);

        FirebaseHelper.getDb().collection("users").document(uid)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        User user = doc.toObject(User.class);
                        if (user != null) {
                            if (tvOrgName != null) {
                                String name = user.getOrgName() != null ? user.getOrgName() : user.getEmail();
                                tvOrgName.setText(name);
                            }
                            if (tvAccountType != null) {
                                tvAccountType.setText("(org)");
                            }
                            if (etDescription != null && user.getDescription() != null) {
                                etDescription.setText(user.getDescription());
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to load profile", Toast.LENGTH_SHORT).show());
    }

    private void updateDescription(String newDescription) {
        String uid = FirebaseHelper.getCurrentUid();
        if (uid == null) return;

        FirebaseHelper.getDb().collection("users").document(uid)
                .update("description", newDescription)
                .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Description updated!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void logout() {
        FirebaseHelper.logout();
        navigateTo(new LoginScreen());
    }

    private void navigateTo(Fragment fragment) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).replaceFragment(fragment);
        }
    }
}