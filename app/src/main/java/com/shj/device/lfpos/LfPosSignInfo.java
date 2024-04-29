package com.shj.device.lfpos;

/* loaded from: classes2.dex */
public class LfPosSignInfo {
    private Integer id;
    private boolean serverTested = false;
    private LfPosSignStep step;

    public LfPosSignStep getStep() {
        return this.step;
    }

    public void setStep(LfPosSignStep lfPosSignStep) {
        this.step = lfPosSignStep;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer num) {
        this.id = num;
    }

    public boolean isServerTested() {
        return this.serverTested;
    }

    public void setServerTested(boolean z) {
        this.serverTested = z;
    }
}
