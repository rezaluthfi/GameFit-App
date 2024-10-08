package com.example.gamefit;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import com.example.gamefit.login.LoginActivity;
import com.example.gamefit.menu.BerlanggananActivity;
import com.example.gamefit.menu.RiwayatActivity;
import com.example.gamefit.menu.VoucherActivity;

public class ProfileFragment extends Fragment {

    LinearLayout voucherku, riwayatAktivitas, berlangganan, keluar;
    TextView fullName, username, email;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        fullName = view.findViewById(R.id.tv_full_name);
        username = view.findViewById(R.id.tv_username);
        email = view.findViewById(R.id.tv_email);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Pastikan pengguna terautentikasi sebelum mengambil userId
        FirebaseUser currentUser = fAuth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();

            DocumentReference documentReference = fStore.collection("users").document(userId);
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        fullName.setText(documentSnapshot.getString("fName"));
                        email.setText(documentSnapshot.getString("email"));
                        username.setText(documentSnapshot.getString("fName"));
                    } else {
                        // Handle the case where documentSnapshot is null
                        fullName.setText("N/A");
                        email.setText("N/A");
                        username.setText("N/A");
                    }
                }
            });
        } else {
            // Handle the case where the user is not authenticated
            // Redirect to login activity or show appropriate message
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

        voucherku = view.findViewById(R.id.ll_voucherku);
        riwayatAktivitas = view.findViewById(R.id.ll_riwayat_aktivitas);
        berlangganan = view.findViewById(R.id.ll_berlangganan);
        keluar = view.findViewById(R.id.ll_signout);

        voucherku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VoucherActivity.class);
                startActivity(intent);
            }
        });

        riwayatAktivitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RiwayatActivity.class);
                startActivity(intent);
            }
        });

        berlangganan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BerlanggananActivity.class);
                startActivity(intent);
            }
        });

        // Tambahkan OnClickListener untuk menu keluar
        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflate the custom dialog view
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.view_dialog_log_out, null);

                // Create the AlertDialog
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                dialogBuilder.setView(dialogView);

                // Create and show the dialog
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                // Set the button actions
                Button btnOk = dialogView.findViewById(R.id.btnOk);
                TextView btnCancel = dialogView.findViewById(R.id.btnCancel);

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Proses logout dari Firebase Authentication
                        FirebaseAuth.getInstance().signOut();
                        //toast
                        Toast.makeText(getActivity(), "Berhasil keluar", Toast.LENGTH_SHORT).show();

                        // Redirect ke activity login atau activity lainnya setelah logout
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        // Akhiri activity saat ini
                        getActivity().finish();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });

        return view;
    }
}
