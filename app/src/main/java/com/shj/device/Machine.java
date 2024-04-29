package com.shj.device;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class Machine {
    private boolean hasTemperature = false;
    private boolean hasHumidity = false;
    private boolean hasDoor = false;
    private Integer jgh = 0;
    private Integer temperature = 0;
    private Integer temperatureFault = 0;
    private Integer humidity = 0;
    private boolean doorIsOpen = false;
    private int coinMachineStatus = 0;
    private int paperMachineStatus = 0;
    private int posMachineStatus = 0;
    private long lastUpdateTemperatureTime = 0;
    private long lastUpdateHumidityTime = 0;
    private long lastUpdateDoorStatusTime = 0;
    private List<Integer> shelves = new ArrayList();
    private List<Integer> layers = new ArrayList();

    public Integer getJgh() {
        return this.jgh;
    }

    public void setJgh(Integer num) {
        this.jgh = num;
    }

    public void addShelf(int i) {
        if (this.shelves.contains(Integer.valueOf(i))) {
            return;
        }
        this.shelves.add(Integer.valueOf(i));
    }

    public void addLayer(int i) {
        if (this.layers.contains(Integer.valueOf(i))) {
            return;
        }
        this.layers.add(Integer.valueOf(i));
    }

    public List<Integer> getLayers() {
        return this.layers;
    }

    public List<Integer> getShelves() {
        return this.shelves;
    }

    public Integer getTemperature() {
        return this.temperature;
    }

    public Integer getTemperatureFault() {
        return this.temperatureFault;
    }

    public Integer setTemperatureFault(int i) {
        Integer valueOf = Integer.valueOf(i);
        this.temperatureFault = valueOf;
        return valueOf;
    }

    public String getTemperatureInfo() {
        if (170 == this.temperature.intValue() || System.currentTimeMillis() - this.lastUpdateTemperatureTime > 300000) {
            return "aa";
        }
        int intValue = this.temperature.intValue();
        if (intValue == 161) {
            return "a1";
        }
        if (intValue == 162) {
            return "a2";
        }
        return this.temperature + "℃";
    }

    public Integer getTemperatureState() {
        if (170 == this.temperature.intValue() || System.currentTimeMillis() - this.lastUpdateTemperatureTime > 300000) {
            return 3;
        }
        if (161 == this.temperature.intValue() || 162 == this.temperature.intValue()) {
            return 1;
        }
        return 0;
    }

    public void setTemperature(Integer num) {
        this.lastUpdateTemperatureTime = System.currentTimeMillis();
        this.temperature = num;
        this.hasTemperature = true;
    }

    public boolean isDoorIsOpen() {
        return this.doorIsOpen;
    }

    public void setDoorIsOpen(boolean z) {
        this.doorIsOpen = z;
        this.hasDoor = true;
        this.lastUpdateDoorStatusTime = System.currentTimeMillis();
    }

    public int getCoinMachineStatus() {
        return this.coinMachineStatus;
    }

    public void setCoinMachineStatus(int i) {
        this.coinMachineStatus = i;
    }

    public int getPaperMachineStatus() {
        return this.paperMachineStatus;
    }

    public void setPaperMachineStatus(int i) {
        this.paperMachineStatus = i;
    }

    public int getPosMachineStatus() {
        return this.posMachineStatus;
    }

    public void setPosMachineStatus(int i) {
        this.posMachineStatus = i;
    }

    public Integer getHumidity() {
        return this.humidity;
    }

    public String getHumidityInfo() {
        if (170 == this.humidity.intValue() || System.currentTimeMillis() - this.lastUpdateHumidityTime > 300000) {
            return "aa";
        }
        int intValue = this.humidity.intValue();
        if (intValue == 161) {
            return "a1";
        }
        if (intValue == 162) {
            return "a2";
        }
        return this.humidity + "%RH";
    }

    public void setHumidity(Integer num) {
        this.lastUpdateHumidityTime = System.currentTimeMillis();
        this.humidity = num;
        this.hasHumidity = true;
    }

    public boolean hasTemperature() {
        return this.hasTemperature;
    }

    public boolean hasHumidity() {
        return this.hasHumidity;
    }

    public boolean isHasDoor() {
        return this.hasDoor;
    }
}
