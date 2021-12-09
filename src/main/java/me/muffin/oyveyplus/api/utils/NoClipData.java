package me.muffin.oyveyplus.api.utils;

public class NoClipData {
    private boolean dummySpectator = false;
    private boolean noClipping = false;
    private boolean fullbright = false;
    private boolean insideBlock = false;

    public boolean isInsideBlock() {
        return insideBlock;
    }

    public void setInsideBlock(boolean insideBlock) {
        this.insideBlock = insideBlock;
    }

    public boolean noClip() {
        return noClipping;
    }

    public boolean fullBright() {
        return fullbright;
    }

    public boolean spectator() {
        return noClip() && dummySpectator;
    }

    public void setNoClipping(boolean noClipping) {
        this.noClipping = noClipping;
    }

    public void setFullbright(boolean fullbright) {
        this.fullbright = fullbright;
    }

    public void setDummySpectator(boolean value) {
        dummySpectator = value;
    }
}
