package com.avr.mediastreamingserver.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Video {
    @Id
    private String hash;
    private String title;
    private String fileName;
    private Long duration;
    private String description;
    private String thumbnailPath;
    private LocalDateTime uploadedAt;
}

/*
* {
    "streams": [
        {
            "index": 0,
            "codec_name": "hevc",
            "codec_long_name": "H.265 / HEVC (High Efficiency Video Coding)",
            "profile": "Main",
            "codec_type": "video",
            "codec_tag_string": "[0][0][0][0]",
            "codec_tag": "0x0000",
            "width": 1920,
            "height": 802,
            "coded_width": 1920,
            "coded_height": 808,
            "closed_captions": 0,
            "film_grain": 0,
            "has_b_frames": 2,
            "sample_aspect_ratio": "1:1",
            "display_aspect_ratio": "960:401",
            "pix_fmt": "yuv420p",
            "level": 123,
            "color_range": "tv",
            "color_space": "bt709",
            "color_transfer": "bt709",
            "color_primaries": "bt709",
            "chroma_location": "left",
            "refs": 1,
            "view_ids_available": "",
            "view_pos_available": "",
            "r_frame_rate": "24/1",
            "avg_frame_rate": "24/1",
            "time_base": "1/1000",
            "start_pts": 0,
            "start_time": "0.000000",
            "extradata_size": 2516,
            "disposition": {
                "default": 1,
                "dub": 0,
                "original": 0,
                "comment": 0,
                "lyrics": 0,
                "karaoke": 0,
                "forced": 0,
                "hearing_impaired": 0,
                "visual_impaired": 0,
                "clean_effects": 0,
                "attached_pic": 0,
                "timed_thumbnails": 0,
                "non_diegetic": 0,
                "captions": 0,
                "descriptions": 0,
                "metadata": 0,
                "dependent": 0,
                "still_image": 0,
                "multilayer": 0
            },
            "tags": {
                "DURATION": "01:47:33.959000000"
            }
        },
        {
            "index": 1,
            "codec_name": "aac",
            "codec_long_name": "AAC (Advanced Audio Coding)",
            "profile": "LC",
            "codec_type": "audio",
            "codec_tag_string": "[0][0][0][0]",
            "codec_tag": "0x0000",
            "sample_fmt": "fltp",
            "sample_rate": "48000",
            "channels": 6,
            "channel_layout": "5.1",
            "bits_per_sample": 0,
            "initial_padding": 1024,
            "r_frame_rate": "0/0",
            "avg_frame_rate": "0/0",
            "time_base": "1/1000",
            "start_pts": -21,
            "start_time": "-0.021000",
            "extradata_size": 5,
            "disposition": {
                "default": 1,
                "dub": 0,
                "original": 0,
                "comment": 0,
                "lyrics": 0,
                "karaoke": 0,
                "forced": 0,
                "hearing_impaired": 0,
                "visual_impaired": 0,
                "clean_effects": 0,
                "attached_pic": 0,
                "timed_thumbnails": 0,
                "non_diegetic": 0,
                "captions": 0,
                "descriptions": 0,
                "metadata": 0,
                "dependent": 0,
                "still_image": 0,
                "multilayer": 0
            },
            "tags": {
                "language": "eng",
                "title": "Surround",
                "DURATION": "01:47:33.994000000"
            }
        },
        {
            "index": 2,
            "codec_name": "ass",
            "codec_long_name": "ASS (Advanced SSA) subtitle",
            "codec_type": "subtitle",
            "codec_tag_string": "[0][0][0][0]",
            "codec_tag": "0x0000",
            "r_frame_rate": "0/0",
            "avg_frame_rate": "0/0",
            "time_base": "1/1000",
            "start_pts": -21,
            "start_time": "-0.021000",
            "duration_ts": 6453994,
            "duration": "6453.994000",
            "extradata_size": 522,
            "disposition": {
                "default": 0,
                "dub": 0,
                "original": 0,
                "comment": 0,
                "lyrics": 0,
                "karaoke": 0,
                "forced": 0,
                "hearing_impaired": 0,
                "visual_impaired": 0,
                "clean_effects": 0,
                "attached_pic": 0,
                "timed_thumbnails": 0,
                "non_diegetic": 0,
                "captions": 0,
                "descriptions": 0,
                "metadata": 0,
                "dependent": 0,
                "still_image": 0,
                "multilayer": 0
            },
            "tags": {
                "language": "eng",
                "title": "English",
                "DURATION": "01:38:42.500000000"
            }
        },
        {
            "index": 3,
            "codec_name": "ass",
            "codec_long_name": "ASS (Advanced SSA) subtitle",
            "codec_type": "subtitle",
            "codec_tag_string": "[0][0][0][0]",
            "codec_tag": "0x0000",
            "r_frame_rate": "0/0",
            "avg_frame_rate": "0/0",
            "time_base": "1/1000",
            "start_pts": -21,
            "start_time": "-0.021000",
            "duration_ts": 6453994,
            "duration": "6453.994000",
            "extradata_size": 522,
            "disposition": {
                "default": 0,
                "dub": 0,
                "original": 0,
                "comment": 0,
                "lyrics": 0,
                "karaoke": 0,
                "forced": 0,
                "hearing_impaired": 0,
                "visual_impaired": 0,
                "clean_effects": 0,
                "attached_pic": 0,
                "timed_thumbnails": 0,
                "non_diegetic": 0,
                "captions": 0,
                "descriptions": 0,
                "metadata": 0,
                "dependent": 0,
                "still_image": 0,
                "multilayer": 0
            },
            "tags": {
                "language": "eng",
                "title": "English (CC)",
                "DURATION": "01:40:21.014000000"
            }
        },
        {
            "index": 4,
            "codec_name": "ass",
            "codec_long_name": "ASS (Advanced SSA) subtitle",
            "codec_type": "subtitle",
            "codec_tag_string": "[0][0][0][0]",
            "codec_tag": "0x0000",
            "r_frame_rate": "0/0",
            "avg_frame_rate": "0/0",
            "time_base": "1/1000",
            "start_pts": -21,
            "start_time": "-0.021000",
            "duration_ts": 6453994,
            "duration": "6453.994000",
            "extradata_size": 522,
            "disposition": {
                "default": 0,
                "dub": 0,
                "original": 0,
                "comment": 0,
                "lyrics": 0,
                "karaoke": 0,
                "forced": 0,
                "hearing_impaired": 0,
                "visual_impaired": 0,
                "clean_effects": 0,
                "attached_pic": 0,
                "timed_thumbnails": 0,
                "non_diegetic": 0,
                "captions": 0,
                "descriptions": 0,
                "metadata": 0,
                "dependent": 0,
                "still_image": 0,
                "multilayer": 0
            },
            "tags": {
                "language": "spa",
                "title": "Español (Latinoamérica)",
                "DURATION": "01:47:23.875000000"
            }
        },
        {
            "index": 5,
            "codec_name": "ass",
            "codec_long_name": "ASS (Advanced SSA) subtitle",
            "codec_type": "subtitle",
            "codec_tag_string": "[0][0][0][0]",
            "codec_tag": "0x0000",
            "r_frame_rate": "0/0",
            "avg_frame_rate": "0/0",
            "time_base": "1/1000",
            "start_pts": -21,
            "start_time": "-0.021000",
            "duration_ts": 6453994,
            "duration": "6453.994000",
            "extradata_size": 522,
            "disposition": {
                "default": 0,
                "dub": 0,
                "original": 0,
                "comment": 0,
                "lyrics": 0,
                "karaoke": 0,
                "forced": 0,
                "hearing_impaired": 0,
                "visual_impaired": 0,
                "clean_effects": 0,
                "attached_pic": 0,
                "timed_thumbnails": 0,
                "non_diegetic": 0,
                "captions": 0,
                "descriptions": 0,
                "metadata": 0,
                "dependent": 0,
                "still_image": 0,
                "multilayer": 0
            },
            "tags": {
                "language": "fre",
                "title": "Français (Canada)",
                "DURATION": "01:47:23.875000000"
            }
        },
        {
            "index": 6,
            "codec_name": "ass",
            "codec_long_name": "ASS (Advanced SSA) subtitle",
            "codec_type": "subtitle",
            "codec_tag_string": "[0][0][0][0]",
            "codec_tag": "0x0000",
            "r_frame_rate": "0/0",
            "avg_frame_rate": "0/0",
            "time_base": "1/1000",
            "start_pts": -21,
            "start_time": "-0.021000",
            "duration_ts": 6453994,
            "duration": "6453.994000",
            "extradata_size": 522,
            "disposition": {
                "default": 0,
                "dub": 0,
                "original": 0,
                "comment": 0,
                "lyrics": 0,
                "karaoke": 0,
                "forced": 0,
                "hearing_impaired": 0,
                "visual_impaired": 0,
                "clean_effects": 0,
                "attached_pic": 0,
                "timed_thumbnails": 0,
                "non_diegetic": 0,
                "captions": 0,
                "descriptions": 0,
                "metadata": 0,
                "dependent": 0,
                "still_image": 0,
                "multilayer": 0
            },
            "tags": {
                "language": "fre",
                "title": "Français (Canada) (SDH)",
                "DURATION": "01:47:23.875000000"
            }
        }
    ],
    "format": {
        "filename": "/Users/avr/Downloads/movies/Lilo and Stich 2025 1080p REPACK WEB-DL HEVC x265 5.1 BONE.mkv",
        "nb_streams": 7,
        "nb_programs": 0,
        "nb_stream_groups": 0,
        "format_name": "matroska,webm",
        "format_long_name": "Matroska / WebM",
        "start_time": "-0.021000",
        "duration": "6453.994000",
        "size": "1928146841",
        "bit_rate": "2390019",
        "probe_score": 100,
        "tags": {
            "creation_time": "2025-07-22T11:42:35.000000Z",
            "ENCODER": "Lavf61.7.100"
        }
    }
}
* */