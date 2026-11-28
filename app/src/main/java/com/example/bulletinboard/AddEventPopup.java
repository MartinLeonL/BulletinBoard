package com.example.bulletinboard;

import android.Manifest;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.bulletinboard.models.Event;
import com.example.bulletinboard.models.User;
import com.google.firebase.Timestamp;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddEventPopup extends DialogFragment {

    private String selectedCategory = "CLUB";
    private String photoBase64 = null;
    private Timestamp selectedEndTime = null;

    private EditText etEventName;
    private EditText etEventLocation;
    private EditText etEndTime;
    private EditText etEventDescription;
    private ImageView ivPhotoPreview;

    private final Map<String, TextView> categoryViews = new HashMap<>();
    private ActivityResultLauncher<String> selectGalleryLauncher;
    private ActivityResultLauncher<Void> takeCameraLauncher;

    public AddEventPopup() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        selectGalleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        processGalleryImage(uri);
                    }
                }
        );

        takeCameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicturePreview(),
                bitmap -> {
                    if (bitmap != null) {
                        processCameraImage(bitmap);
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.popup_add_event, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null && getContext() != null) {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int maxHeight = (int) (metrics.heightPixels * 0.78); // 78% of screen height

            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    maxHeight
            );
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView btnClose = view.findViewById(R.id.btn_close);
        if (btnClose != null) {
            btnClose.setOnClickListener(v -> dismiss());
        }

        etEventName = view.findViewById(R.id.et_eventName);
        etEventLocation = view.findViewById(R.id.et_eventLocation);
        etEndTime = view.findViewById(R.id.et_endTime);
        etEventDescription = view.findViewById(R.id.et_eventDescription);
        ivPhotoPreview = view.findViewById(R.id.iv_photoPreview);

        ImageButton btnUploadPhoto = view.findViewById(R.id.btn_uploadPhoto);
        if (btnUploadPhoto != null) {
            btnUploadPhoto.setOnClickListener(v -> showPhotoSourceDialog());
        }

        if (etEndTime != null) {
            etEndTime.setOnClickListener(v -> showTimePicker());
        }

        setupCategoryChips(view);

        Button btnPost = view.findViewById(R.id.btn_postEvent);
        if (btnPost != null) {
            btnPost.setOnClickListener(v -> attemptPostEvent());
        }
    }

    private void showPhotoSourceDialog() {
        if (getContext() == null) return;

        CharSequence[] options = new CharSequence[]{
                getString(R.string.take_photo),
                getString(R.string.choose_gallery)
        };

        new AlertDialog.Builder(getContext())
                .setTitle(R.string.select_photo_source)
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        takeCameraLauncher.launch(null);
                    } else {
                        selectGalleryLauncher.launch("image/*");
                    }
                })
                .show();
    }

    private void setupCategoryChips(View view) {
        categoryViews.put("CLUB", view.findViewById(R.id.btn_catClub));
        categoryViews.put("FOOD", view.findViewById(R.id.btn_catFood));
        categoryViews.put("SPORT", view.findViewById(R.id.btn_catSport));
        categoryViews.put("SOCIAL", view.findViewById(R.id.btn_catSocial));
        categoryViews.put("STUDENT GOV", view.findViewById(R.id.btn_catStudentGov));
        categoryViews.put("OTHER", view.findViewById(R.id.btn_catOther));

        for (Map.Entry<String, TextView> entry : categoryViews.entrySet()) {
            TextView tv = entry.getValue();
            if (tv != null) {
                tv.setOnClickListener(v -> {
                    selectedCategory = entry.getKey();
                    updateCategorySelection();
                });
            }
        }

        updateCategorySelection();
    }

    private void updateCategorySelection() {
        for (Map.Entry<String, TextView> entry : categoryViews.entrySet()) {
            String cat = entry.getKey();
            TextView tv = entry.getValue();
            if (tv == null) continue;

            int color = getColorForCategory(cat);
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadius(20f);
            shape.setColor(color);

            if (cat.equalsIgnoreCase(selectedCategory)) {
                shape.setStroke(4, Color.BLACK);
                tv.setAlpha(1.0f);
            } else {
                tv.setAlpha(0.5f);
            }

            tv.setBackground(shape);
        }
    }

    private int getColorForCategory(String category) {
        switch (category.toUpperCase()) {
            case "CLUB":
                return Color.parseColor("#8A2BE2");
            case "FOOD":
                return Color.parseColor("#D32F2F");
            case "SPORT":
                return Color.parseColor("#388E3C");
            case "SOCIAL":
                return Color.parseColor("#F57C00");
            case "STUDENT GOV":
                return Color.parseColor("#1976D2");
            default:
                return Color.parseColor("#616161");
        }
    }

    private void showTimePicker() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                (timePicker, selectedHour, selectedMinute) -> {
                    Calendar endCal = Calendar.getInstance();
                    endCal.set(Calendar.HOUR_OF_DAY, selectedHour);
                    endCal.set(Calendar.MINUTE, selectedMinute);
                    endCal.set(Calendar.SECOND, 0);

                    // If picked time is earlier than current time today, roll over to tomorrow
                    if (endCal.getTimeInMillis() <= System.currentTimeMillis()) {
                        endCal.add(Calendar.DAY_OF_MONTH, 1);
                    }

                    String amPm = selectedHour >= 12 ? "PM" : "AM";
                    int displayHour = (selectedHour % 12 == 0) ? 12 : (selectedHour % 12);

                    String formattedTime = String.format(Locale.US, "%d:%02d %s", displayHour, selectedMinute, amPm);
                    if (etEndTime != null) {
                        etEndTime.setText(formattedTime);
                    }

                    selectedEndTime = new Timestamp(endCal.getTime());
                }, hour, minute, false);

        timePickerDialog.show();
    }

    private void processGalleryImage(Uri uri) {
        if (getContext() == null) return;
        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap != null) {
                processCameraImage(bitmap);
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Failed to process photo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void processCameraImage(Bitmap bitmap) {
        Bitmap scaled = scaleDown(bitmap, 600f);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        scaled.compress(Bitmap.CompressFormat.JPEG, 75, baos);
        byte[] bytes = baos.toByteArray();
        photoBase64 = Base64.encodeToString(bytes, Base64.DEFAULT);

        if (ivPhotoPreview != null) {
            ivPhotoPreview.setImageBitmap(scaled);
            ivPhotoPreview.setVisibility(View.VISIBLE);
        }
    }

    private Bitmap scaleDown(Bitmap realImage, float maxImageSize) {
        float ratio = Math.min(
                maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        if (ratio >= 1.0f) return realImage;

        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());
        return Bitmap.createScaledBitmap(realImage, width, height, true);
    }

    private void attemptPostEvent() {
        String name = etEventName != null ? etEventName.getText().toString().trim() : "";
        String locationLabel = etEventLocation != null ? etEventLocation.getText().toString().trim() : "";
        String description = etEventDescription != null ? etEventDescription.getText().toString().trim() : "";

        if (name.isEmpty() || locationLabel.isEmpty()) {
            Toast.makeText(getContext(), "Name and Location are required (*)", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentUid = FirebaseHelper.getCurrentUid();
        if (currentUid == null) {
            Toast.makeText(getContext(), "User not logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch current user details to attach author metadata
        FirebaseHelper.getDb().collection("users").document(currentUid)
                .get()
                .addOnSuccessListener(doc -> {
                    String postedByName = "Anonymous";
                    String postedByType = "student";

                    if (doc.exists()) {
                        User user = doc.toObject(User.class);
                        if (user != null) {
                            postedByType = user.getAccountType() != null ? user.getAccountType() : "student";
                            if ("org".equalsIgnoreCase(postedByType)) {
                                postedByName = user.getOrgName() != null ? user.getOrgName() : "Organization";
                            } else {
                                postedByName = user.getUsername() != null ? user.getUsername() : "Student";
                            }
                        }
                    }

                    double lat = 28.6024;
                    double lng = -81.2001;

                    // Fetch current user GPS location
                    if (getContext() != null
                            && (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {

                        LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                        if (lm != null) {
                            try {
                                Location gpsLoc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                Location netLoc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                Location bestLoc = gpsLoc != null ? gpsLoc : netLoc;

                                if (bestLoc != null) {
                                    lat = bestLoc.getLatitude();
                                    lng = bestLoc.getLongitude();
                                }
                            } catch (SecurityException ignored) {}
                        }
                    }

                    saveEventToFirestore(currentUid, postedByName, postedByType, name, locationLabel, description, lat, lng);
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to read profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void saveEventToFirestore(String currentUid, String postedByName, String postedByType,
                                      String name, String locationLabel, String description,
                                      double latitude, double longitude) {

        Event newEvent = new Event(
                null,
                name,
                selectedCategory,
                locationLabel,
                latitude,
                longitude,
                selectedEndTime,
                description,
                photoBase64,
                currentUid,
                postedByName,
                postedByType,
                Timestamp.now(),
                true
        );

        FirebaseHelper.getDb().collection("events")
                .add(newEvent)
                .addOnSuccessListener(ref -> {
                    Toast.makeText(getContext(), "Event posted successfully!", Toast.LENGTH_SHORT).show();
                    dismiss();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to post event: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}