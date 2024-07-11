package com.example.SpringBatch.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class VideoStats {
    UUID videoId;
    Long videoView;
    Long videoPlaytime;

}
