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

import java.util.ArrayList;
import java.util.List;

public class FamilyActivity extends AppCompatActivity {

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
        words.add(new Word("father", "әpә", R.drawable.family_father, R.raw.family_father));
        words.add(new Word("mother", "әṭa", R.drawable.family_mother, R.raw.family_mother));
        words.add(new Word("son", "angsi", R.drawable.family_son, R.raw.family_son));
        words.add(new Word("daughter", "tune", R.drawable.family_daughter, R.raw.family_daughter));
        words.add(new Word("older brother", "taachi", R.drawable.family_older_brother, R.raw.family_older_brother));
        words.add(new Word("younger brother", "chalitti", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        words.add(new Word("older sister", "teṭe", R.drawable.family_older_sister, R.raw.family_older_sister));
        words.add(new Word("younger sister", "kolliti", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        words.add(new Word("grandmother", "ama", R.drawable.family_grandmother, R.raw.family_grandmother));
        words.add(new Word("grandfather", "paapa", R.drawable.family_grandfather, R.raw.family_grandfather));

        WordAdapter wordAdapter = new WordAdapter(this, words);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(wordAdapter);

        listView.setOnItemClickListener((adapterView, view, position, l) -> {
            releaseMediaPlayer();
            am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

            int focusRequest = am.requestAudioFocus(focusStateChange, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT, AudioManager.STREAM_MUSIC);

            if (focusRequest == AudioManager.AUDIOFOCUS_GAIN) {
                Word word = words.get(position);
                mp = MediaPlayer.create(FamilyActivity.this, word.getSoundResourceId());
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
