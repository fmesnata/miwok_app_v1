package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ColorsActivity extends AppCompatActivity {

    MediaPlayer mp;
    private AudioManager am;
    private AudioManager.OnAudioFocusChangeListener focusStateChange = focusState -> {
        Log.d("focusState", "focusState : " + focusState);
        switch (focusState) {
            case AudioManager.AUDIOFOCUS_GAIN:
                mp.start();
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                mp.stop();
                releaseMediaPlayer();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                mp.pause();
                mp.seekTo(0);
                break;
            default:
                throw new IllegalStateException("Wrong state");
        }
    };

    private MediaPlayer.OnCompletionListener onCompletionListener = mediaPlayer -> releaseMediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        final List<Word> words = new ArrayList<>();
        words.add(new Word("red", "weṭeṭṭi", R.drawable.color_red, R.raw.color_red));
        words.add(new Word("green", "chokokki", R.drawable.color_green, R.raw.color_green));
        words.add(new Word("brown", "ṭakaakki", R.drawable.color_brown, R.raw.color_brown));
        words.add(new Word("gray", "ṭopoppi", R.drawable.color_gray, R.raw.color_gray));
        words.add(new Word("black", "kululli", R.drawable.color_black, R.raw.color_black));
        words.add(new Word("white", "kelelli", R.drawable.color_white, R.raw.color_white));
        words.add(new Word("dusty yellow", "ṭopiisә", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        words.add(new Word("mustard yello", "chiwiiṭә", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));

        WordAdapter wordAdapter = new WordAdapter(this, words);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(wordAdapter);

        listView.setOnItemClickListener((adapterView, view, position, l) -> {
            releaseMediaPlayer();

            am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

            int focusRequest = am.requestAudioFocus(focusStateChange, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT, AudioManager.STREAM_MUSIC);

            if (focusRequest == AudioManager.AUDIOFOCUS_GAIN) {
                Word word = words.get(position);
                mp = MediaPlayer.create(ColorsActivity.this, word.getSoundResourceId());
                mp.start();
                mp.setOnCompletionListener(onCompletionListener);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        if (mp != null) {
            mp.release();
            am.abandonAudioFocus(focusStateChange);
        }
    }
}
