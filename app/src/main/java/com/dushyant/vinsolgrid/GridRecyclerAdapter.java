package com.dushyant.vinsolgrid;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class GridRecyclerAdapter extends RecyclerView.Adapter<GridRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<String> numberWordsList;

    public GridRecyclerAdapter(Context context, List<String> numberWordsList) {
        this.context = context;
        this.numberWordsList = numberWordsList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textView;
        LinearLayout mainLayout;

        ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            textView = itemView.findViewById(R.id.text_name);
            mainLayout = itemView.findViewById(R.id.item_layout);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        int spacing = context.getSharedPreferences(StaticData.PREF_NAME, Context.MODE_PRIVATE)
                .getInt(StaticData.GRID_SPACING, StaticData.DEFAULT_SPACING);
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                spacing,
                context.getResources().getDisplayMetrics()
        );

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(px, px, px, px);
        holder.cardView.setLayoutParams(layoutParams);

        holder.textView.setText(Html.fromHtml(String.format("%s", numberWordsList.get(position))));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.cardView.setElevation(4);
        }

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.cardView.setElevation(0);
                }

                if (context.getSharedPreferences(StaticData.PREF_NAME, Context.MODE_PRIVATE)
                        .getBoolean(StaticData.ENABLE_FLIP_ANIMATION, true)) {
                    AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.flip_animation);
                    set.setTarget(holder.cardView);
                    set.setDuration(context.getSharedPreferences(StaticData.PREF_NAME, Context.MODE_PRIVATE)
                            .getInt(StaticData.FLIP_ANIM_TIME, StaticData.DEFAULT_FLIP_DURATION));
                    set.start();

                    set.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            removeItem(holder.getAdapterPosition());
                        }
                    });
                } else
                    removeItem(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return numberWordsList.size();
    }


    public void removeItem(int position) {
        numberWordsList.remove(position);
//        notifyDataSetChanged();
        notifyItemRemoved(position);
    }
}
