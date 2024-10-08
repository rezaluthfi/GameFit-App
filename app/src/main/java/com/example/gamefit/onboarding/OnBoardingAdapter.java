package com.example.gamefit.onboarding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.gamefit.R;

public class OnBoardingAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    private int titles[] = {
            R.string.ob_title_01,
            R.string.ob_title_02,
            R.string.ob_title_03
    };

    private int descriptions[] = {
            R.string.ob_desc_01,
            R.string.ob_desc_02,
            R.string.ob_desc_03
    };

    private int images[] = {
            R.drawable.img_onboarding_1,
            R.drawable.img_onboarding_2,
            R.drawable.img_onboarding_3
    };

    public OnBoardingAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View v = layoutInflater.inflate(R.layout.view_onboarding, container, false);

        ImageView image = v.findViewById(R.id.image);
        TextView title = v.findViewById(R.id.title);
        TextView description = v.findViewById(R.id.description);

        image.setImageResource(images[position]);
        title.setText(titles[position]);
        description.setText(descriptions[position]);

        container.addView(v);
        return v;
    }
}
