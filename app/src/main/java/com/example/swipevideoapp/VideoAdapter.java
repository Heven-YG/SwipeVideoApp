package com.example.swipevideoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Displays video pages and plays only the selected video.
 */
public class VideoAdapter
        extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    /** Videos displayed in the app. */
    private final List<VideoItem> videoItems;

    /** Video pages currently attached to the screen. */
    private final Set<VideoViewHolder> attachedHolders = new HashSet<>();

    /** Position of the selected video. */
    private int selectedPosition = 0;

    /** Whether the app is allowed to play videos. */
    private boolean playbackEnabled = false;

    /**
     * Creates the adapter.
     *
     * @param videoItems list of videos to display
     */
    public VideoAdapter(List<VideoItem> videoItems) {
        this.videoItems = videoItems;
    }

    /**
     * Creates a page using the video XML layout.
     *
     * @param parent parent view
     * @param viewType type of page
     * @return holder for the new page
     */
    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_container_video, parent, false);

        return new VideoViewHolder(view);
    }

    /**
     * Adds a video's information to a page.
     *
     * @param holder page to update
     * @param position position in the video list
     */
    @Override
    public void onBindViewHolder(
            @NonNull VideoViewHolder holder, int position) {

        holder.bind(videoItems.get(position));

        if (attachedHolders.contains(holder)) {
            updatePlayback();
        }
    }

    /**
     * Returns the number of videos.
     *
     * @return video count
     */
    @Override
    public int getItemCount() {
        return videoItems.size();
    }

    /**
     * Tracks a page when it becomes attached.
     *
     * @param holder attached page
     */
    @Override
    public void onViewAttachedToWindow(@NonNull VideoViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        attachedHolders.add(holder);
        updatePlayback();
    }

    /**
     * Stops a page when it leaves the screen.
     *
     * @param holder detached page
     */
    @Override
    public void onViewDetachedFromWindow(@NonNull VideoViewHolder holder) {
        holder.stop();
        attachedHolders.remove(holder);
        super.onViewDetachedFromWindow(holder);
    }

    /**
     * Releases playback before a page is reused.
     *
     * @param holder page being recycled
     */
    @Override
    public void onViewRecycled(@NonNull VideoViewHolder holder) {
        holder.stop();
        attachedHolders.remove(holder);
        super.onViewRecycled(holder);
    }

    /**
     * Selects the video that should play after a swipe.
     *
     * @param position selected video position
     */
    public void setSelectedPosition(int position) {
        selectedPosition = position;
        updatePlayback();
    }

    /**
     * Enables or disables playback when the app opens or closes.
     *
     * @param enabled true to allow playback
     */
    public void setPlaybackEnabled(boolean enabled) {
        playbackEnabled = enabled;
        updatePlayback();
    }

    /**
     * Stops other pages before starting the selected page.
     */
    private void updatePlayback() {
        for (VideoViewHolder holder : attachedHolders) {
            if (!playbackEnabled
                    || holder.getBindingAdapterPosition() != selectedPosition) {
                holder.stop();
            }
        }

        if (playbackEnabled) {
            for (VideoViewHolder holder : attachedHolders) {
                if (holder.getBindingAdapterPosition() == selectedPosition) {
                    holder.play();
                }
            }
        }
    }

    /**
     * Holds the views and playback state for one video page.
     */
    static class VideoViewHolder extends RecyclerView.ViewHolder {

        /** View that displays the video. */
        private final VideoView videoView;

        /** Loading indicator. */
        private final ProgressBar progressBar;

        /** Text showing the video title. */
        private final TextView title;

        /** Text showing the video description. */
        private final TextView description;

        /** Text showing the unique video ID. */
        private final TextView videoId;

        /** Video assigned to this page. */
        private VideoItem videoItem;

        /** Whether this page has requested playback. */
        private boolean playing = false;

        /**
         * Finds the views inside the video layout.
         *
         * @param itemView layout for one video
         */
        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);

            videoView = itemView.findViewById(R.id.videoView);
            progressBar = itemView.findViewById(R.id.videoProgressBar);
            title = itemView.findViewById(R.id.textVideoTitle);
            description = itemView.findViewById(R.id.textVideoDescription);
            videoId = itemView.findViewById(R.id.textVideoId);
        }

        /**
         * Displays the information for a video.
         *
         * @param item video to display
         */
        void bind(VideoItem item) {
            stop();
            videoItem = item;

            title.setText(item.videoTitle);
            description.setText(item.videoDescription);
            videoId.setText("ID: " + item.videoId);
        }

        /**
         * Loads and loops this page's video.
         */
        void play() {
            if (playing || videoItem == null) {
                return;
            }

            if (videoItem.videoURL.trim().isEmpty()) {
                progressBar.setVisibility(View.GONE);
                return;
            }

            playing = true;
            progressBar.setVisibility(View.VISIBLE);
            description.setText(videoItem.videoDescription);

            videoView.setOnPreparedListener(mediaPlayer -> {
                progressBar.setVisibility(View.GONE);
                mediaPlayer.setLooping(true);

                if (playing) {
                    videoView.start();
                }
            });

            videoView.setOnErrorListener((mediaPlayer, what, extra) -> {
                progressBar.setVisibility(View.GONE);
                description.setText(
                        videoItem.videoDescription
                                + "\nUnable to play video. Check the link."
                );
                return true;
            });

            videoView.setVideoPath(videoItem.videoURL);
        }

        /**
         * Stops playback and releases the video player.
         */
        void stop() {
            playing = false;
            videoView.setOnPreparedListener(null);
            videoView.setOnErrorListener(null);
            videoView.stopPlayback();
            progressBar.setVisibility(View.GONE);
        }
    }
}