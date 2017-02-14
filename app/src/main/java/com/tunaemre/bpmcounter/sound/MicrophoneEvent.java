package com.tunaemre.bpmcounter.sound;

public interface MicrophoneEvent
{
    void soundMeter(double dB);

    void onBeat(double time);
}
