package com.example.bulletinboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bulletinboard.models.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.CopyrightOverlay;
import org.osmdroid.views.overlay.Marker;

import java.util.HashMap;
import java.util.Map;

public class MapScreen extends Fragment {

    private MapView map;
    private ListenerRegistration eventsRegistration;
    private final Map<Marker, String> markerEventMap = new HashMap<>();

    public MapScreen() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Configuration.getInstance().setUserAgentValue("UCFBulletinBoardApp/1.0 (Mobile App; contact: ucfbulletinboard@ucf.edu)");

        FloatingActionButton btnAddEvent = view.findViewById(R.id.btn_addEvent);
        FloatingActionButton btnFilter = view.findViewById(R.id.btn_filter);

        if (btnAddEvent != null) {
            btnAddEvent.setOnClickListener(v -> new AddEventPopup().show(getParentFragmentManager(), "AddEventPopup"));
        }

        if (btnFilter != null) {
            btnFilter.setOnClickListener(v -> new FilterPopup().show(getParentFragmentManager(), "FilterPopup"));
        }

        map = view.findViewById(R.id.mapView);
        if (map != null) {
            // OpenStreetMap France (OSMFR): 100% free public HTTPS tile source (No API key required)
            OnlineTileSourceBase osmFrSource = new XYTileSource(
                    "OSMFR",
                    0, 19, 256, ".png",
                    new String[]{
                            "https://a.tile.openstreetmap.fr/osmfr/",
                            "https://b.tile.openstreetmap.fr/osmfr/",
                            "https://c.tile.openstreetmap.fr/osmfr/"
                    }
            );

            map.setTileSource(osmFrSource);
            map.setUseDataConnection(true);
            map.setMultiTouchControls(true);

            map.getOverlays().add(new CopyrightOverlay(requireContext()));
            map.getController().setZoom(16.0);
            map.getController().setCenter(new GeoPoint(28.6024, -81.2001));

            loadEvents();
        }
    }

    private void addPin(double latitude, double longitude, String title, String snippet, String eventId) {
        if (map == null) return;

        Marker marker = new Marker(map);
        marker.setPosition(new GeoPoint(latitude, longitude));
        marker.setTitle(title != null ? title : "Event");
        marker.setSnippet(snippet != null ? snippet : "");
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

        if (eventId != null) {
            markerEventMap.put(marker, eventId);
        }

        marker.setOnMarkerClickListener((m, mapView) -> {
            String id = markerEventMap.get(m);
            if (id != null && !id.isEmpty()) {
                EventInfoPopup.newInstance(id).show(getParentFragmentManager(), "EventInfoPopup");
            } else {
                m.showInfoWindow();
            }
            return true;
        });

        map.getOverlays().add(marker);
    }

    private void loadEvents() {
        eventsRegistration = FirebaseHelper.getDb().collection("events")
                .addSnapshotListener((result, error) -> {
                    if (error != null || result == null || map == null || getContext() == null) return;

                    map.getOverlays().clear();
                    markerEventMap.clear();
                    map.getOverlays().add(new CopyrightOverlay(requireContext()));

                    long now = System.currentTimeMillis();

                    for (DocumentSnapshot doc : result.getDocuments()) {
                        Event event = doc.toObject(Event.class);
                        if (event != null) {
                            Boolean activeObj = doc.getBoolean("active");
                            if (activeObj == null) activeObj = doc.getBoolean("isActive");
                            boolean isActive = Boolean.TRUE.equals(activeObj) || (activeObj == null && event.isActive());

                            Timestamp endTime = event.getEndTime();
                            boolean notExpired = (endTime == null) || (endTime.toDate().getTime() >= (now - 300000));

                            if (isActive && notExpired) {
                                String label = (event.getCategory() != null ? "[" + event.getCategory() + "] " : "")
                                        + (event.getLocationLabel() != null ? event.getLocationLabel() : "");

                                addPin(
                                        event.getLatitude(),
                                        event.getLongitude(),
                                        event.getName(),
                                        label,
                                        doc.getId()
                                );
                            }
                        }
                    }

                    map.invalidate();
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (map != null) {
            map.onResume();
        }
    }

    @Override
    public void onPause() {
        if (map != null) {
            map.onPause();
        }
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        if (eventsRegistration != null) {
            eventsRegistration.remove();
            eventsRegistration = null;
        }
        markerEventMap.clear();
        if (map != null) {
            map.onDetach();
            map = null;
        }
        super.onDestroyView();
    }
}