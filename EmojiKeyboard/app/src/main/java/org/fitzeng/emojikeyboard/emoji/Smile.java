package org.fitzeng.emojikeyboard.emoji;

public class Smile {

    private int resId;
    private String info;

    public Smile(int resId, String info) {
        this.resId = resId;
        this.info = info;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
