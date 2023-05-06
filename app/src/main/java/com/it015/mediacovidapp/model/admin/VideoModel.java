package com.it015.mediacovidapp.model.admin;

public class VideoModel {

    String judul;
    String uri;
    String TglPosting;
    String WaktuPosting;
    String author;
    int love;
    int komen;
    int view;
    boolean status_love;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    boolean selected;
    int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isStatus_love() {
        return status_love;
    }

    public void setStatus_love(boolean status_love) {
        this.status_love = status_love;
    }

    public String getTglPosting() {
        return TglPosting;
    }

    public void setTglPosting(String tglPosting) {
        TglPosting = tglPosting;
    }

    public String getWaktuPosting() {
        return WaktuPosting;
    }

    public void setWaktuPosting(String waktuPosting) {
        WaktuPosting = waktuPosting;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getLove() {
        return love;
    }

    public void setLove(int love) {
        this.love = love;
    }

    public int getKomen() {
        return komen;
    }

    public void setKomen(int komen) {
        this.komen = komen;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public VideoModel(String judul, String uri, String tglPosting, String waktuPosting, String author, int love, int komen, int view) {
        this.judul = judul;
        this.uri = uri;
        TglPosting = tglPosting;
        WaktuPosting = waktuPosting;
        this.author = author;
        this.love = love;
        this.komen = komen;
        this.view = view;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public VideoModel() {

    }
}
