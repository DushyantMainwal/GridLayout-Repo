package com.dushyant.vinsolgrid;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //    private GridView gridView;
//    private GridAdapter gridAdapter;
    private List<String> numberWordsList;
    private RecyclerView recyclerView;
    private ImageView settings;

    private SharedPreferences sharedPreferences;
    private static final int DATA_CHANGE_REQUEST = 101;

    private int columnWidth = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Home");

        sharedPreferences = getSharedPreferences(StaticData.PREF_NAME, MODE_PRIVATE);

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
                startActivityForResult(new Intent(MainActivity.this, SettingsActivity.class), DATA_CHANGE_REQUEST);
            }
        });

        columnWidth = sharedPreferences.getInt(StaticData.COLUMN_WIDTH, StaticData.DEFAULT_WIDTH);

        GridRecyclerAdapter gridRecyclerAdapter = new GridRecyclerAdapter(this, numberWordsList);
        recyclerView = findViewById(R.id.recycler_View);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);

        recyclerView.setLayoutManager(gridLayoutManager);
        if (sharedPreferences.getBoolean(StaticData.ENABLE_ITEMS_ANIMATION, true))
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        else
            recyclerView.setItemAnimator(null);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(gridRecyclerAdapter);

        if (sharedPreferences.getBoolean(StaticData.ENABLE_START_ANIMATION, true)) {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.grid_item_anim);
            LayoutAnimationController controller = new LayoutAnimationController(animation, .2f);
            animation.setDuration(sharedPreferences.getInt(StaticData.START_ANIM_TIME, StaticData.DEFAULT_START_DURATION));
            recyclerView.setLayoutAnimation(controller);
        }

        ViewTreeObserver viewTreeObserver = recyclerView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                calculateSize();
            }
        });
    }

    private void calculateSize() {
        int spanCount = (int) Math.floor(recyclerView.getWidth() / convertDPToPixels(columnWidth));
        ((GridLayoutManager) recyclerView.getLayoutManager()).setSpanCount(spanCount);
    }

    private float convertDPToPixels(int dp) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float logicalDensity = metrics.density;
        return dp * logicalDensity;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DATA_CHANGE_REQUEST && resultCode == RESULT_OK) {
            recreate();
        }
    }
}
