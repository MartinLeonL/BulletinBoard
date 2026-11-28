package com.example.bulletinboard;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bulletinboard.models.Event;
import com.example.bulletinboard.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

public class ListScreen extends Fragment {

    private EventAdaptor adaptor;
    private List<Event> allEvents = new ArrayList<>();
    private List<Event> visibleEvents = new ArrayList<>();
    private List<String> followedOrgUids = new ArrayList<>();

    private ListenerRegistration eventsRegistration;
    private ListenerRegistration userRegistration;

    private TextView tvEmptyState;
    private RecyclerView rcvEventList;
    private EditText etSearch;

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
        tvEmptyState = view.findViewById(R.id.tv_emptyState);
        rcvEventList = view.findViewById(R.id.rcv_eventList);
        etSearch = view.findViewById(R.id.et_search);

        if (btnAddEvent != null) {
            btnAddEvent.setOnClickListener(v -> new AddEventPopup().show(getParentFragmentManager(), "AddEventPopup"));
        }

        if (btnFilter != null) {
            btnFilter.setOnClickListener(v -> new FilterPopup().show(getParentFragmentManager(), "FilterPopup"));
        }

        adaptor = new EventAdaptor(visibleEvents);
        adaptor.setOnItemClickListener(event -> new EventInfoPopup().show(getParentFragmentManager(), "EventInfoPopup"));

        if (rcvEventList != null) {
            rcvEventList.setLayoutManager(new LinearLayoutManager(getContext()));
            rcvEventList.setAdapter(adaptor);
        }

        if (etSearch != null) {
            etSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    applyFilterAndSort();
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

        loadUserAndEvents();
    }

    private void loadUserAndEvents() {
        String currentUid = FirebaseHelper.getCurrentUid();
        if (currentUid != null) {
            userRegistration = FirebaseHelper.getDb().collection("users").document(currentUid)
                    .addSnapshotListener((snapshot, e) -> {
                        if (e != null || snapshot == null || !snapshot.exists()) return;
                        User user = snapshot.toObject(User.class);
                        if (user != null && user.getFollowedOrgs() != null) {
                            followedOrgUids = user.getFollowedOrgs();
                        } else {
                            followedOrgUids.clear();
                        }
                        applyFilterAndSort();
                    });
        }

        long currentTime = System.currentTimeMillis();

        eventsRegistration = FirebaseHelper.getDb().collection("events")
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null || snapshots == null) return;

                    List<Event> fetched = new ArrayList<>();
                    for (DocumentSnapshot doc : snapshots.getDocuments()) {
                        Event event = doc.toObject(Event.class);
                        if (event != null) {
                            event.setEventId(doc.getId());

                            // Event active rule: isActive == true AND (endTime == null OR endTime > currentTime)
                            boolean notExpired = event.getEndTime() == null
                                    || event.getEndTime().toDate().getTime() > currentTime;

                            if (event.isActive() && notExpired) {
                                fetched.add(event);
                            }
                        }
                    }

                    allEvents = fetched;
                    applyFilterAndSort();
                });
    }

    private void applyFilterAndSort() {
        String searchQuery = etSearch != null ? etSearch.getText().toString().trim().toLowerCase() : "";

        List<Event> filtered = new ArrayList<>();
        for (Event event : allEvents) {
            boolean matchesSearch = searchQuery.isEmpty()
                    || (event.getName() != null && event.getName().toLowerCase().contains(searchQuery))
            || (event.getPostedByName() != null && event.getPostedByName().toLowerCase().contains(searchQuery));

            if (matchesSearch) {
                filtered.add(event);
            }
        }

        // Pin events created by followed organizations to the top
        filtered.sort((e1, e2) -> {
            boolean e1Followed = e1.getPostedByUid() != null && followedOrgUids.contains(e1.getPostedByUid());
            boolean e2Followed = e2.getPostedByUid() != null && followedOrgUids.contains(e2.getPostedByUid());

            if (e1Followed && !e2Followed) return -1;
            if (!e1Followed && e2Followed) return 1;
            return 0;
        });

        visibleEvents = filtered;
        if (adaptor != null) {
            adaptor.setEvents(visibleEvents);
        }

        if (visibleEvents.isEmpty()) {
            if (tvEmptyState != null) tvEmptyState.setVisibility(View.VISIBLE);
            if (rcvEventList != null) rcvEventList.setVisibility(View.GONE);
        } else {
            if (tvEmptyState != null) tvEmptyState.setVisibility(View.GONE);
            if (rcvEventList != null) rcvEventList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (eventsRegistration != null) {
            eventsRegistration.remove();
            eventsRegistration = null;
        }
        if (userRegistration != null) {
            userRegistration.remove();
            userRegistration = null;
        }
    }
}