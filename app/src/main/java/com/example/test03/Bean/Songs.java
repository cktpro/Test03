package com.example.test03.Bean;

import java.io.Serializable;

public class Songs implements Serializable {

    private int songId;
    private String songTitle;
    private String songContent;

    public Songs()  {

    }

    public Songs(String songTitle, String songContent) {
        this.songTitle= songTitle;
        this.songContent= songContent;
    }

    public Songs(int songId, String songTitle, String songContent) {
        this.songId= songId;
        this.songTitle= songTitle;
        this.songContent= songContent;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }
    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }


    public String getSongContent() {
        return songContent;
    }

    public void setNoteContent(String songContent) {
        this.songContent = songContent;
    }


    @Override
    public String toString()  {
        return this.songTitle;
    }

}
