package com.example.android.miwok;

public class Word {

    private String defaultTranslation;

    private String miwokTranslation;

    private int imageResourceId;

    private int soundResourceId;

    public Word(String defaultTranslation, String miwokTranslation, int soundResourceId) {
        this(defaultTranslation, miwokTranslation, -1, soundResourceId);
    }

    public Word(String defaultTranslation, String miwokTranslation, int imageResourceId, int soundResourceId) {
        this.defaultTranslation = defaultTranslation;
        this.miwokTranslation = miwokTranslation;
        this.imageResourceId = imageResourceId;
        this.soundResourceId = soundResourceId;
    }



    public String getDefaultTranslation() {
        return defaultTranslation;
    }

    public String getMiwokTranslation() {
        return miwokTranslation;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public int getSoundResourceId() {
        return soundResourceId;
    }
}
