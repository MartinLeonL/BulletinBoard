package com.example.bulletinboard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.bulletinboard.models.Event;
import com.example.bulletinboard.models.Report;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ListenerRegistration;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EventInfoPopup extends DialogFragment {

    private static final String ARG_EVENT_ID = "ARG_EVENT_ID";

    private String eventId;
    private Event currentEvent;
    private ListenerRegistration eventListener;

    private TextView tvEventName;
    private TextView tvCategoryBadge;
    private TextView tvEventLocation;
    private TextView tvEventEndTime;
    private TextView tvEventDescription;
    private ImageView ivEventImage;
    private TextView tvPostedByName;
    private Button btnVoteYes;
    private Button btnVoteNo;

    public static EventInfoPopup newInstance(String eventId) {
        EventInfoPopup fragment = new EventInfoPopup();
        Bundle args = new Bundle();
        args.putString(ARG_EVENT_ID, eventId);
        fragment.setArguments(args);
        return fragment;
    }

    public EventInfoPopup() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventId = getArguments().getString(ARG_EVENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.popup_event_info, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null && getContext() != null) {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int maxHeight = (int) (metrics.heightPixels * 0.78);

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

        tvEventName = view.findViewById(R.id.tv_eventName);
        tvCategoryBadge = view.findViewById(R.id.tv_categoryBadge);
        tvEventLocation = view.findViewById(R.id.tv_eventLocation);
        tvEventEndTime = view.findViewById(R.id.tv_eventEndTime);
        tvEventDescription = view.findViewById(R.id.tv_eventDescription);
        ivEventImage = view.findViewById(R.id.iv_eventImage);
        tvPostedByName = view.findViewById(R.id.tv_postedByName);
        btnVoteYes = view.findViewById(R.id.btn_voteYes);
        btnVoteNo = view.findViewById(R.id.btn_voteNo);

        Button btnReport = view.findViewById(R.id.btn_reportEvent);
        if (btnReport != null) {
            btnReport.setOnClickListener(v -> submitReport());
        }

        if (eventId != null) {
            listenToEventData();
        }
    }

    private void listenToEventData() {
        eventListener = FirebaseHelper.getDb().collection("events").document(eventId)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null || snapshot == null || !snapshot.exists()) return;
                    Event event = snapshot.toObject(Event.class);
                    if (event != null) {
                        event.setEventId(snapshot.getId());
                        currentEvent = event;
                        bindEventData(event);
                    }
                });
    }

    private void bindEventData(Event event) {
        if (tvEventName != null) {
            tvEventName.setText(event.getName() != null ? event.getName() : "");
        }

        if (tvEventLocation != null) {
            tvEventLocation.setText(event.getLocationLabel() != null ? event.getLocationLabel() : "");
        }

        // Category Badge
        if (tvCategoryBadge != null) {
            String category = event.getCategory() != null ? event.getCategory() : "OTHER";
            tvCategoryBadge.setText(category);

            int color = getColorForCategory(category);
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadius(20f);
            shape.setColor(color);
            tvCategoryBadge.setBackground(shape);
        }

        // End Time
        if (tvEventEndTime != null) {
            if (event.getEndTime() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.US);
                String formatted = "Until: " + sdf.format(event.getEndTime().toDate());
                tvEventEndTime.setText(formatted);
                tvEventEndTime.setVisibility(View.VISIBLE);
            } else {
                tvEventEndTime.setVisibility(View.GONE);
            }
        }

        // Description
        if (tvEventDescription != null) {
            String desc = event.getDescription();
            if (desc != null && !desc.trim().isEmpty()) {
                tvEventDescription.setText(desc);
                tvEventDescription.setVisibility(View.VISIBLE);
            } else {
                tvEventDescription.setText("No description provided.");
            }
        }

        // Photo
        if (ivEventImage != null) {
            if (event.getPhotoBase64() != null && !event.getPhotoBase64().isEmpty()) {
                try {
                    byte[] bytes = Base64.decode(event.getPhotoBase64(), Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    if (bitmap != null) {
                        ivEventImage.setImageBitmap(bitmap);
                        ivEventImage.setVisibility(View.VISIBLE);
                    } else {
                        ivEventImage.setVisibility(View.GONE);
                    }
                } catch (Exception ex) {
                    ivEventImage.setVisibility(View.GONE);
                }
            } else {
                ivEventImage.setVisibility(View.GONE);
            }
        }

        // Author / Posted By
        if (tvPostedByName != null) {
            String authorName = event.getPostedByName() != null ? event.getPostedByName() : "Anonymous";
            SpannableString spannable = new SpannableString(authorName);
            spannable.setSpan(new UnderlineSpan(), 0, spannable.length(), 0);
            tvPostedByName.setText(spannable);
            tvPostedByName.setPaintFlags(tvPostedByName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            // If posted by an organization, allow clicking to view Org Info
            if ("org".equalsIgnoreCase(event.getPostedByType())) {
                tvPostedByName.setOnClickListener(v -> new OrgInfoPopup().show(getParentFragmentManager(), "OrgInfoPopup"));
            } else {
                tvPostedByName.setOnClickListener(null);
            }
        }

        // Voting Buttons State
        updateVotingUI(event);
    }

    private void updateVotingUI(Event event) {
        String currentUid = FirebaseHelper.getCurrentUid();
        Map<String, String> votes = event.getVotes();
        String userVote = (votes != null && currentUid != null) ? votes.get(currentUid) : null;

        if (btnVoteYes != null) {
            btnVoteYes.setOnClickListener(v -> submitVote("yes"));
            if ("yes".equalsIgnoreCase(userVote)) {
                btnVoteYes.setAlpha(1.0f);
            } else {
                btnVoteYes.setAlpha(0.6f);
            }
        }

        if (btnVoteNo != null) {
            btnVoteNo.setOnClickListener(v -> submitVote("no"));
            if ("no".equalsIgnoreCase(userVote)) {
                btnVoteNo.setAlpha(1.0f);
            } else {
                btnVoteNo.setAlpha(0.6f);
            }
        }
    }

    private void submitVote(String voteValue) {
        if (currentEvent == null || eventId == null) return;
        String currentUid = FirebaseHelper.getCurrentUid();
        if (currentUid == null) {
            Toast.makeText(getContext(), "Please log in to vote.", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> votes = currentEvent.getVotes();
        if (votes == null) {
            votes = new HashMap<>();
        }

        votes.put(currentUid, voteValue);

        // Calculate crowd moderation per PROJECT_SPEC.md
        int yesCount = 0;
        int noCount = 0;
        for (String v : votes.values()) {
            if ("yes".equalsIgnoreCase(v)) yesCount++;
            else if ("no".equalsIgnoreCase(v)) noCount++;
        }

        int totalVotes = votes.size();
        boolean isActive = true;
        if (totalVotes >= 3 && noCount > yesCount) {
            isActive = false; // Crowd moderation deactivates event
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("votes", votes);
        updates.put("active", isActive);
        updates.put("isActive", isActive);

        FirebaseHelper.getDb().collection("events").document(eventId)
                .update(updates)
                .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Vote recorded!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Vote failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void submitReport() {
        if (eventId == null) return;
        String currentUid = FirebaseHelper.getCurrentUid();

        Report report = new Report(
                null,
                "event",
                eventId,
                currentUid,
                Timestamp.now()
        );

        FirebaseHelper.getDb().collection("reports")
                .add(report)
                .addOnSuccessListener(ref -> Toast.makeText(getContext(), "Report submitted. Thank you for keeping campus safe.", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to submit report: " + e.getMessage(), Toast.LENGTH_SHORT).show());
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (eventListener != null) {
            eventListener.remove();
            eventListener = null;
        }
    }
}