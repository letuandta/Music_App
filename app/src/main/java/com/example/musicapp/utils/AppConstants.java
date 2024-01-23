package com.example.musicapp.utils;

public class AppConstants {
    public final static class MusicBundleKey {
        public static final String ACTION_MUSIC = "action_music";
        public static final String SONG_JSON = "song_json";
        public static final String POSITION = "position";
        public static final String TOTAL = "total";
        public static final String IS_PLAYING = "is_playing";
        public static final String DURATION = "duration";
        public static final String ACTION_SEND_DATA_TO_ACTIVITY = "action_send_data_to_activity";
        public static final String SKIP_DURATION = "skip_duration";
        public static final String TYPE = "type";
        public static final String KEY_SEARCH = "key_search";
    }

    public final static class MusicPlayerActions {
        public static final int ACTION_PLAY_OR_PAUSE = 1;
        public static final int ACTION_START = 2;
        public static final int ACTION_NEXT = 3;
        public static final int ACTION_PREVIOUS = 4;
        public static final int ACTION_STOP = 5;
        public static final int ACTION_CHANGE_SHUFFLE = 6;
        public static final int ACTION_CHANGE_LOOPING = 7;
        public static final int ACTION_SKIP = 8;
    }

    public final static class MusicPlayerType {
        public static final String FAVORITES_SONG = "favorites_song";
        public static final String RECOMMEND_SONG = "recommend_song";

        public static final String SEARCH_SONG = "search_song";
        public static final String SEARCH_SONG_OFFLINE = "search_song_offline";
    }

}
