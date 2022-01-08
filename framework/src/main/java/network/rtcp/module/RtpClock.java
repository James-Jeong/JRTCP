package network.rtcp.module;

import java.util.concurrent.TimeUnit;

/**
 * @author kulikov
 */
public class RtpClock {

    //absolute time clock
    private final Clock wallClock;

    //the clock rate measured in Hertz.
    private int clockRate;
    private int scale;

    //the difference between media time measured by local and remote clock
    protected long drift;

    //the flag indicating the state of relation between local and remote clocks
    //the flag value is true if relation established
    private boolean isSynchronized;

    public RtpClock(Clock wallClock) {
        this.wallClock = wallClock;
    }

    public Clock getWallClock() {
        return wallClock;
    }

    public void setClockRate(int clockRate) {
        this.clockRate = clockRate;
        this.scale = clockRate/1000;
    }

    public int getClockRate() {
        return clockRate;
    }

    public void synchronize(long remote) {
        this.drift = remote - getLocalRtpTime();
        this.isSynchronized = true;
    }

    public boolean isSynchronized() {
        return this.isSynchronized;
    }

    public void reset() {
        this.drift = 0;
        this.clockRate = 0;
        this.isSynchronized = false;
    }

    public long getLocalRtpTime() {
        return scale * wallClock.getTime(TimeUnit.MILLISECONDS) + drift;
    }

    public long convertToAbsoluteTime(long timestamp) {
        return timestamp * 1000 / clockRate;
    }

    public long convertToRtpTime(long time) {
        return time * clockRate / 1000;
    }

}