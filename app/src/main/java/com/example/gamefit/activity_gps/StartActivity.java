package com.example.gamefit.activity_gps;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamefit.R;
import com.example.gamefit.databinding.ActivityStartBinding;
import com.example.gamefit.databinding.ViewIndicatorsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class StartActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private ActivityStartBinding binding;
    private ViewIndicatorsBinding indicatorsBinding;
    private Context context;
    private MapPresenter presenter;
    private TextView tvGpsStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        //panggil method showRewardDialog
        showRewardDialog();

        // Inflate the layout using ViewBinding
        binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        indicatorsBinding = ViewIndicatorsBinding.bind(findViewById(R.id.container));
        presenter = new MapPresenter(this);

        // Set up the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initialize GPS status TextView
        tvGpsStatus = findViewById(R.id.tvGpsStatus);

        // Set up start/stop button
        indicatorsBinding.btnStartStop.setOnClickListener(v -> {
            if (indicatorsBinding.btnStartStop.getText().equals(getString(R.string.start_label))) {
                startTracking();
                indicatorsBinding.btnStartStop.setText(R.string.stop_label);
            } else {
                stopTracking();
                findViewById(R.id.map).setVisibility(View.VISIBLE);
                indicatorsBinding.btnStartStop.setText(R.string.start_label);
            }
        });

        presenter.onViewCreated();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        presenter.getUi().observe(this, this::updateUi);

        presenter.onMapLoaded();
        map.getUiSettings().setZoomControlsEnabled(true);
    }

    private void startTracking() {
        indicatorsBinding.txtPace.setText("");
        indicatorsBinding.txtDistance.setText("");
        indicatorsBinding.txtTime.setBase(SystemClock.elapsedRealtime());
        indicatorsBinding.txtTime.start();
        map.clear();

        presenter.startTracking();
    }

    private void stopTracking() {
        presenter.stopTracking();
        indicatorsBinding.txtTime.stop();
    }

    @SuppressLint("MissingPermission")
    private void updateUi(Ui ui) {
        if (binding != null) {
            if (ui.getCurrentLocation() != null && !ui.getCurrentLocation().equals(map.getCameraPosition().target)) {
                map.setMyLocationEnabled(true);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(ui.getCurrentLocation(), 14f));
                // Hide GPS status message when location is obtained
                tvGpsStatus.setVisibility(View.GONE);
            } else {
                // Show GPS status message if location is not yet obtained
                tvGpsStatus.setVisibility(View.VISIBLE);
            }
            indicatorsBinding.txtDistance.setText(ui.getFormattedDistance());
            indicatorsBinding.txtPace.setText(ui.getFormattedPace());
            if (ui.getUserPath() != null) {
                drawRoute(ui.getUserPath());
            }
        }
    }

    private void drawRoute(List<LatLng> locations) {
        PolylineOptions polylineOptions = new PolylineOptions();
        map.clear();
        polylineOptions.getPoints().addAll(locations);
        map.addPolyline(polylineOptions);
    }

    private void showRewardDialog() {
        // Buat instance dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.view_dialog_start);

        //set dialog background
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);

        //set dialog width and height manually with percentage
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.70);
        //height wrap content
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        // Atur fokus dan dimAmount untuk membuat latar belakang agak hitam
        if (dialog.getWindow() != null) {
            dialog.getWindow().setDimAmount(0.8f); // Set dim amount (0.0 - 1.0)
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }

        // Atur data jarak dan laju
        TextView distanceTextView = dialog.findViewById(R.id.tvDistance);
        TextView speedTextView = dialog.findViewById(R.id.tvPace);
        distanceTextView.setText("Jarak lari mencapai X m");
        speedTextView.setText("Laju lari lebih dari X");

        // Atur aksi untuk tombol OK
        Button okButton = dialog.findViewById(R.id.btnOk);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Tampilkan dialog
        dialog.show();
    }
}
