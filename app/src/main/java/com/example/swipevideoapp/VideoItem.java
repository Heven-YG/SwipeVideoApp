package com.example.swipevideoapp;

/**
 * Stores the information for one video in the swipe feed.
 */
public class VideoItem {

    /** Unique identifier for the video. */
    public final String videoId;

    /** Direct online link to the video file. */
    public final String videoURL;

    /** Title displayed on the video. */
    public final String videoTitle;

    /** Short description displayed below the title. */
    public final String videoDescription;

    /**
     * Creates a video item.
     *
     * @param videoId unique video identifier
     * @param videoURL direct link to the video file
     * @param videoTitle title of the video
     * @param videoDescription short description of the video
     */
    public VideoItem(String videoId, String videoURL,
                     String videoTitle, String videoDescription) {
        this.videoId = videoId;
        this.videoURL = videoURL;
        this.videoTitle = videoTitle;
        this.videoDescription = videoDescription;
    }
}