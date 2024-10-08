package com.example.gamefit.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import com.example.gamefit.MainActivity;
import com.example.gamefit.R;
import com.example.gamefit.login.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class OnBoardingActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private CardView next;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private SaveState saveState;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        Log.d("OnBoardingActivity", "Activity Created");

        fAuth = FirebaseAuth.getInstance();

        // Check if the user is already logged in
        FirebaseUser currentUser = fAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d("OnBoardingActivity", "User already logged in. Redirecting to MainActivity");
            startActivity(new Intent(OnBoardingActivity.this, MainActivity.class));
            finish();
            return;
        }

        viewPager = findViewById(R.id.viewPager);
        next = findViewById(R.id.nextButton);
        dotsLayout = findViewById(R.id.indicatorContainer);
        saveState = new SaveState(this, "onBoarding");

        if (saveState.getState() == 1) {
            Log.d("OnBoardingActivity", "Onboarding already completed. Redirecting to RegisterActivity");
            startActivity(new Intent(OnBoardingActivity.this, RegisterActivity.class));
            finish();
            return;
        }

        OnBoardingAdapter adapter = new OnBoardingAdapter(this);
        viewPager.setAdapter(adapter);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("OnBoardingActivity", "Next button clicked. Moving to next page.");
                int currentItem = viewPager.getCurrentItem();
                viewPager.setCurrentItem(currentItem + 1, true);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                Log.d("OnBoardingActivity", "Page selected: " + position);
                dotsFunction(position);
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (position < 2) { // 3 slides, so < 2 (0, 1, 2)
                            viewPager.setCurrentItem(position + 1, true);
                        } else {
                            saveState.setState(1);
                            startActivity(new Intent(OnBoardingActivity.this, RegisterActivity.class));
                            finish();
                        }
                    }
                });
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        dotsFunction(0);
    }

    private void dotsFunction(int position) {
        dots = new TextView[3]; // Ensure this matches the number of onboarding screens
        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;")); // Use bullet point
            dots[i].setTextColor(getColor(R.color.white));
            dots[i].setTextSize(32);

            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[position].setTextColor(getColor(R.color.blue));
        }
    }
}
