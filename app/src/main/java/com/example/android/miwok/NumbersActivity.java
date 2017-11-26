package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class NumbersActivity extends AppCompatActivity {

    private MediaPlayer mp;
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
        words.add(new Word("one", "lutti", R.drawable.number_one, R.raw.number_one));
        words.add(new Word("two", "otiiko", R.drawable.number_two, R.raw.number_two));
        words.add(new Word("three", "tolookosu", R.drawable.number_three, R.raw.number_three));
        words.add(new Word("four", "oyyisa", R.drawable.number_four, R.raw.number_four));
        words.add(new Word("five", "massokka", R.drawable.number_five, R.raw.number_five));
        words.add(new Word("six", "temmokka", R.drawable.number_six, R.raw.number_six));
        words.add(new Word("seven", "kenekaku", R.drawable.number_seven, R.raw.number_seven));
        words.add(new Word("eight", "kawinta", R.drawable.number_eight, R.raw.number_eight));
        words.add(new Word("nine", "wo’e", R.drawable.number_nine, R.raw.number_nine));
        words.add(new Word("ten", "na’aacha", R.drawable.number_ten, R.raw.number_ten));

        WordAdapter wordAdapter = new WordAdapter(this, words);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(wordAdapter);

        listView.setOnItemClickListener((adapterView, view, position, l) -> {
            releaseMediaPlayer();
            am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

            int focusRequest = am.requestAudioFocus(focusStateChange, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT, AudioManager.STREAM_MUSIC);

            if (focusRequest == AudioManager.AUDIOFOCUS_GAIN) {
                Word word = words.get(position);
                mp = MediaPlayer.create(NumbersActivity.this, word.getSoundResourceId());
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
