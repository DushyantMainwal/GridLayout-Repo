package com.dushyant.vinsolgrid;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class GridAdapter extends BaseAdapter {

    private Context context;
    private List<String> numberWordsList;

    public GridAdapter(Context context, List<String> numberWordsList) {
        this.context = context;
        this.numberWordsList = numberWordsList;
    }

    @Override
    public int getCount() {
        return numberWordsList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
        }

        final CardView cardView = convertView.findViewById(R.id.card_view);
        TextView textView = convertView.findViewById(R.id.text_name);
        final LinearLayout mainLayout = convertView.findViewById(R.id.item_layout);
        textView.setText(Html.fromHtml(String.format("%s", numberWordsList.get(position))));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cardView.setElevation(4);
        }

//        Animation animation = AnimationUtils.loadAnimation(context ,R.anim.grid_item_anim);
//        cardView.startAnimation(animation);

        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    cardView.setElevation(0);
                }

                AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.flip_animation);
                set.setTarget(cardView);
                set.setDuration(1000);
                set.start();

                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        removeItem(position);
                    }
                });
            }
        });

        return convertView;
    }

    public void addItems() {

    }


    public void removeItem(int position) {
        numberWordsList.remove(position);
        notifyDataSetChanged();
    }

}
