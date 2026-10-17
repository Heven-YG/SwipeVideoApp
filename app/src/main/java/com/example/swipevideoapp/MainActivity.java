package com.example.swipevideoapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays a vertical feed of preset online videos.
 */
public class MainActivity extends AppCompatActivity {

    /** Adapter that displays videos and manages playback. */
    private VideoAdapter videoAdapter;

    /**
     * Sets up the layout, video list, and swipe controls.
     *
     * @param savedInstanceState previously saved activity state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (view, insets) -> {
                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );

                    view.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );

        ViewCompat.requestApplyInsets(findViewById(R.id.main));

        ViewPager2 videosViewPager =
                findViewById(R.id.videosViewPager);

        List<VideoItem> videoItems = new ArrayList<>();

        videoItems.add(new VideoItem(
                "001",
                "https://raw.githubusercontent.com/Heven-YG/DemoVideos/main/video1.MP4",
                "computer parts",
                "computer parts video."
        ));

        videoItems.add(new VideoItem(
                "002",
                "https://raw.githubusercontent.com/Heven-YG/DemoVideos/main/video2.MP4",
                "NBA basketball ",
                "Lemelo Ball assist to jaden mcdaniel."
        ));

        videoItems.add(new VideoItem(
                "003",
                "https://raw.githubusercontent.com/Heven-YG/DemoVideos/main/video3.MP4",
                "EuroLeague Basketball",
                "Fornier makes a Basket."
        ));

        videoAdapter = new VideoAdapter(videoItems);

        videosViewPager.setOrientation(
                ViewPager2.ORIENTATION_VERTICAL
        );

        videosViewPager.setAdapter(videoAdapter);

        videosViewPager.registerOnPageChangeCallback(
                new ViewPager2.OnPageChangeCallback() {

                    /**
                     * Changes playback to the selected video.
                     *
                     * @param position selected page position
                     */
                    @Override
                    public void onPageSelected(int position) {
                        videoAdapter.setSelectedPosition(position);
                    }
                }
        );
    }

    /**
     * Enables playback when the app is visible and active.
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (videoAdapter != null) {
            videoAdapter.setPlaybackEnabled(true);
        }
    }

    /**
     * Stops playback when the user leaves the app.
     */
    @Override
    protected void onPause() {
        if (videoAdapter != null) {
            videoAdapter.setPlaybackEnabled(false);
        }

        super.onPause();
    }
}