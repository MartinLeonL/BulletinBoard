package com.example.bulletinboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bulletinboard.models.User;

public class StudentAccountScreen extends Fragment {

    public StudentAccountScreen() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_account_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnSignOut = view.findViewById(R.id.btn_signOut);
        if (btnSignOut != null) {
            btnSignOut.setOnClickListener(v -> logout());
        }

        Button btnTempOrgInfo = view.findViewById(R.id.btn_tempOrgInfo);
        if (btnTempOrgInfo != null) {
            btnTempOrgInfo.setOnClickListener(v -> new OrgInfoPopup().show(getParentFragmentManager(), "OrgInfoPopup"));
        }

        loadUserProfile(view);
    }

    private void loadUserProfile(View view) {
        String uid = FirebaseHelper.getCurrentUid();
        if (uid == null) {
            navigateTo(new LoginScreen());
            return;
        }

        TextView tvUsernameTitle = view.findViewById(R.id.username_title);
        TextView tvAccountType = view.findViewById(R.id.username);

        FirebaseHelper.getDb().collection("users").document(uid)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        User user = doc.toObject(User.class);
                        if (user != null && tvUsernameTitle != null) {
                            String displayName = user.getUsername() != null ? user.getUsername() : user.getEmail();
                            tvUsernameTitle.setText(displayName);
                        }
                        if (tvAccountType != null) {
                            tvAccountType.setText("(Student)");
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to load profile", Toast.LENGTH_SHORT).show());
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