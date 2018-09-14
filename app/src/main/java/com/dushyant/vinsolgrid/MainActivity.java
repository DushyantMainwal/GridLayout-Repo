package com.dushyant.vinsolgrid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.GridLayoutAnimationController;
import android.view.animation.LayoutAnimationController;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    private GridView gridView;
//    private GridAdapter gridAdapter;
    private List<String> numberWordsList;
    private RecyclerView recyclerView;
    private ImageView settings;
    private static final int sColumnWidth = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Home");

        numberWordsList = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            final int pos = i + 1;

            String superScript;
            if (pos == 1)
                superScript = "st";
            else if (pos == 2)
                superScript = "nd";
            else if (pos == 3)
                superScript = "rd";
            else
                superScript = "th";

            numberWordsList.add(pos + "<sup><small>" + superScript + "</small></sup>");
        }

//        gridView = findViewById(R.id.gridView);
//        gridAdapter = new GridAdapter(this, numberWordsList);
//        Animation animation = AnimationUtils.loadAnimation(this, R.anim.grid_item_anim);
//        GridLayoutAnimationController controller = new GridLayoutAnimationController(animation, .2f, .2f);
//        gridView.setLayoutAnimation(controller);
//
//        gridView.setAdapter(gridAdapter);

        settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        GridRecyclerAdapter gridRecyclerAdapter = new GridRecyclerAdapter(this, numberWordsList);
        recyclerView = findViewById(R.id.recycler_View);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(gridRecyclerAdapter);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.grid_item_anim);
        LayoutAnimationController controller = new LayoutAnimationController(animation, .2f);
        recyclerView.setLayoutAnimation(controller);

        ViewTreeObserver viewTreeObserver = recyclerView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                calculateSize();
            }
        });
    }

    private void calculateSize() {
        int spanCount = (int) Math.floor(recyclerView.getWidth() / convertDPToPixels(sColumnWidth));
        ((GridLayoutManager) recyclerView.getLayoutManager()).setSpanCount(spanCount);
    }

    private float convertDPToPixels(int dp) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float logicalDensity = metrics.density;
        return dp * logicalDensity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        recreate();
    }
}
