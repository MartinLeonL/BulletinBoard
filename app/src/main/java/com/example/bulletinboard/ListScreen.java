package com.example.bulletinboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bulletinboard.models.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ListScreen extends Fragment {

    private EventAdaptor adaptor;
    private List<Event> eventList;

    public ListScreen() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton btnAddEvent = view.findViewById(R.id.btn_addEvent);
        FloatingActionButton btnFilter = view.findViewById(R.id.btn_filter);

        if (btnAddEvent != null) {
            btnAddEvent.setOnClickListener(v -> new AddEventPopup().show(getParentFragmentManager(), "AddEventPopup"));
        }

        if (btnFilter != null) {
            btnFilter.setOnClickListener(v -> new FilterPopup().show(getParentFragmentManager(), "FilterPopup"));
        }

        eventList = new ArrayList<>();
        adaptor = new EventAdaptor(eventList);

        RecyclerView rv = view.findViewById(R.id.rcv_eventList);
        if (rv != null) {
            rv.setLayoutManager(new LinearLayoutManager(getContext()));
            rv.setAdapter(adaptor);
        }
    }
}