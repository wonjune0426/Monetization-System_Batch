package com.example.SpringBatch.projection;

import java.util.UUID;

public interface VideoStatsProjection {
    UUID getVideoId();
    Long getVideoView();
    Long getVideoPlaytime();

}
