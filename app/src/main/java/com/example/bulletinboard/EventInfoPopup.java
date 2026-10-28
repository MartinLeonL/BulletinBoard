package com.example.bulletinboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class EventInfoPopup extends DialogFragment {

    public EventInfoPopup() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.popup_event_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnClose = view.findViewById(R.id.btn_close);
        if (btnClose != null) {
            btnClose.setOnClickListener(v -> dismiss());
        }

        Button btnTempOrgInfo = view.findViewById(R.id.btn_tempOrgInfo);
        if (btnTempOrgInfo != null) {
            btnTempOrgInfo.setOnClickListener(v -> new OrgInfoPopup().show(getParentFragmentManager(), "OrgInfoPopup"));
        }
    }
}