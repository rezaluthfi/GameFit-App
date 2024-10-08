package com.example.gamefit.activity_list;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamefit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ActivityFragment extends Fragment {

    private RecyclerView recyclerView;
    private ActivityAdapter adapter;
    private List<ActivityItem> activityList;
    private SearchView searchView;
    private TextView noResultsTextView;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_activity, container, false);

        noResultsTextView = rootView.findViewById(R.id.noResultsTextView);

        searchView = rootView.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchActivityFromFirestore(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    loadActivitiesFromFirestore();
                } else {
                    searchActivityFromFirestore(newText);
                }
                return true;
            }
        });

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        activityList = new ArrayList<>();
        adapter = new ActivityAdapter(getContext(), activityList, item -> {
            // Handle item click if needed
        });
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadActivitiesFromFirestore();

        return rootView;
    }

    private void loadActivitiesFromFirestore() {
        db.collection("activities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            activityList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ActivityItem activity = document.toObject(ActivityItem.class);
                                activityList.add(activity);
                            }
                            Log.d("Firestore", "Activities loaded: " + activityList.size());
                            adapter.notifyDataSetChanged();
                            updateNoResultsMessage();
                        } else {
                            Log.w("Firestore", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void addActivityToFirestore(ActivityItem activity) {
        db.collection("activities")
                .add(activity)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Firestore", "DocumentSnapshot added with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error adding document", e);
                });
    }

    //search activity from firestore
    private void searchActivityFromFirestore(String query) {
        db.collection("activities")
                .orderBy("activity")
                .startAt(query)
                .endAt(query + "\uf8ff")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            activityList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ActivityItem activity = document.toObject(ActivityItem.class);
                                activityList.add(activity);
                            }
                            Log.d("Firestore", "Search results loaded: " + activityList.size());
                            adapter.notifyDataSetChanged();
                            updateNoResultsMessage();
                        } else {
                            Log.w("Firestore", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    //no results found
    private void updateNoResultsMessage() {
        if (activityList.isEmpty()) {
            noResultsTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noResultsTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
