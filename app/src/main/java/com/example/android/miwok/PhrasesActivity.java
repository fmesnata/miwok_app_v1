package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class PhrasesActivity extends AppCompatActivity {

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
        words.add(new Word("Where are you going?", "minto wuksus", R.raw.phrase_where_are_you_going));
        words.add(new Word("What is your name?", "tinnә oyaase'nә", R.raw.phrase_what_is_your_name));
        words.add(new Word("My name is...", "oyaaset...", R.raw.phrase_my_name_is));
        words.add(new Word("How are you feeling?", "michәksәs?", R.raw.phrase_how_are_you_feeling));
        words.add(new Word("I’m feeling good.", "kuchi achit", R.raw.phrase_im_feeling_good));
        words.add(new Word("Are you coming?", "әәnәs'aa?", R.raw.phrase_are_you_coming));
        words.add(new Word("Yes, I’m coming.", "hәә’ әәnәm", R.raw.phrase_yes_im_coming));
        words.add(new Word("I’m coming.", "әәnәm", R.raw.phrase_im_coming));
        words.add(new Word("Let’s go.", "yoowutis", R.raw.phrase_lets_go));
        words.add(new Word("Come here.", "әnni'nem", R.raw.phrase_come_here));

        WordAdapter wordAdapter = new WordAdapter(this, words);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(wordAdapter);

        listView.setOnItemClickListener((adapterView, view, position, l) -> {
            releaseMediaPlayer();
            am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

            int focusRequest = am.requestAudioFocus(focusStateChange, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT, AudioManager.STREAM_MUSIC);

            if (focusRequest == AudioManager.AUDIOFOCUS_GAIN) {
                Word word = words.get(position);
                mp = MediaPlayer.create(PhrasesActivity.this, word.getSoundResourceId());
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
