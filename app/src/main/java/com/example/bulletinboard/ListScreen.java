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
import androidx.room.Room;

import com.example.bulletinboard.database.entity.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ListScreen extends Fragment {

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

        DatabaseHandler db = Room.databaseBuilder(view.getContext(),
                        DatabaseHandler.class, "DatabaseHandler")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration(true)
                .build();

        List<Event> events = db.eventDAO().getAllEvents();

        RecyclerView rv = view.findViewById(R.id.rcv_eventList);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(new EventAdaptor(events));
    }
}