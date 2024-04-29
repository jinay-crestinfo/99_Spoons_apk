package com.shj.device;

/* loaded from: classes2.dex */
public class Picker {
    private int id;
    private int status = 0;
    private int waitTimer = 0;
    private int lastOfferShelf = -1;

    public Picker(int i) {
        this.id = i;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int i) {
        this.status = i;
    }

    public int getWaitTimer() {
        return this.waitTimer;
    }

    public void setWaitTimer(int i) {
        this.waitTimer = i;
    }

    public int getLastOfferShelf() {
        return this.lastOfferShelf;
    }

    public void setLastOfferShelf(int i) {
        this.lastOfferShelf = i;
    }
}
