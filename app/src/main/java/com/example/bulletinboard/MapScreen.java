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

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.CopyrightOverlay;

public class MapScreen extends Fragment {

    private MapView map;

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

        FloatingActionButton btnAddEvent = view.findViewById(R.id.btn_addEvent);
        FloatingActionButton btnFilter = view.findViewById(R.id.btn_filter);

        if (btnAddEvent != null) {
            btnAddEvent.setOnClickListener(v -> new AddEventPopup().show(getParentFragmentManager(), "AddEventPopup"));
        }

        if (btnFilter != null) {
            btnFilter.setOnClickListener(v -> new FilterPopup().show(getParentFragmentManager(), "FilterPopup"));
        }

        map = view.findViewById(R.id.mapView);

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setUseDataConnection(true);
        map.setMultiTouchControls(true);

        map.getOverlays().add(
                new CopyrightOverlay(requireContext())
        );

        map.getController().setZoom(16.0);

        map.getController().setCenter(
                new GeoPoint(28.6024, -81.2001)
        );

        addPin(
                28.6024,
                -81.2001,
                "Gaming Tournament",
                "Come play video games!"
        );
    }

    private void addPin(double latitude, double longitude,
                        String title, String description)
    {
        Marker marker = new Marker(map);

        marker.setPosition(
                new GeoPoint(latitude, longitude)
        );

        marker.setTitle(title);
        marker.setSnippet(description);

        marker.setAnchor(
                Marker.ANCHOR_CENTER,
                Marker.ANCHOR_BOTTOM
        );

        map.getOverlays().add(marker);
        map.invalidate();
    }

    private void loadEvents()
    {
        FirebaseHelper.getDb()
                .collection("events")
                .get()
                .addOnSuccessListener(result ->
                {
                    if(map == null)
                        return;

                    for(Event event : result.toObjects(Event.class))
                    {
                        addPin(
                                event.getLatitude(),
                                event.getLongitude(),
                                event.getName(),
                                event.getDescription()
                        );
                    }
                });
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if(map != null)
            map.onResume();
    }

    @Override
    public void onPause()
    {
        if(map != null)
            map.onPause();

        super.onPause();
    }

    @Override
    public void onDestroyView()
    {
        if(map != null)
        {
            map.onDetach();
            map = null;
        }

        super.onDestroyView();
    }
}