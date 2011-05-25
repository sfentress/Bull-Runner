/*
 * SoundClip.java
 *
 * Created on July 24, 2006, 10:41 AM
 *
 * Copyright (C) 2006  Sam Fentress [please append any subsequent authors here]
 *
 * "Copyleft" under the terms of the GNU General Public License, version 2.0 or later
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * @author Sam Fentress
 * @version 0.05
 */

package format;

import java.io.File;

import javax.sound.sampled.*;

import sam.utilities.Logger;

/**
 * Play a *.wav or *.au file.
 *
 */
public class SoundClip {
    
    private AudioInputStream ais;
    private AudioFormat af;
    private DataLine.Info info;
    private int bufSize;
    private boolean initialized;
    private long clipLength;
    private static float gain;
    
    /**
     * Play a *.wav or *.au file
     *
     * @param fileName name of file to play
     */
    public SoundClip(String fileName) {
        try {
            initialized = true;
            ais = AudioSystem.getAudioInputStream(new File(fileName));
            af = ais.getFormat();
            info = new DataLine.Info(SourceDataLine.class, af);
            
            if (! AudioSystem.isLineSupported(info)) {
                Logger.log("Sound: unsupported line");
                initialized = false;
            }
            
            int frameRate = (int)af.getFrameRate();
            int frameSize = af.getFrameSize();
            bufSize = frameRate * frameSize / 10;
        }
        catch (Exception e) {
            System.out.println("Failed creating SoundClip:");
            Logger.log(e.toString());
            Logger.print();
            initialized = false;
        }
    }
    
    /**
     * Create a blank clip
     */
    public SoundClip() {
        initialized = false;
    }
    
    // Can only be used once
    public void play(){
        if (!initialized)
            return;
        
        try {   
            SourceDataLine line = (SourceDataLine)
            AudioSystem.getLine(info);
            line.open(af, bufSize);
            changeVolume(line);
            line.start();
            
            byte[] data = new byte[bufSize];
            int bytesRead;
            
            long startTime = System.currentTimeMillis();

            while ((bytesRead = ais.read(data, 0, data.length)) != -1)
                line.write(data, 0, bytesRead);

            long endTime = System.currentTimeMillis();
            
            clipLength = endTime - startTime;
            
            line.drain();
            line.stop();
            line.close();
        } catch (Exception e) {
            Logger.log(e.toString());
            Logger.print();
        }
    }
    
    public static void setGain(float value){
        gain = value;
    }
    
    private void changeVolume(SourceDataLine line){
        FloatControl gainControl = (FloatControl)line.getControl(FloatControl.Type.MASTER_GAIN);
        if (gain > gainControl.getMaximum())
            gain = gainControl.getMaximum();
        if (gain < gainControl.getMinimum())
            gain = gainControl.getMinimum();
        gainControl.setValue(gain);
    }
    
    public boolean isInitialized(){
        return initialized;
    }
    
    public void remove(){
        initialized = false;
    }
    
    // Can only be used after clip has played
    public long getClipLength(){
        return clipLength;
    }
    
    // Main method for testing purposes only
    public static void main(String[] args){
        gain = 20f;
        Logger.setVerbose(true);
        SoundClip sc = new SoundClip(0);
        System.exit(0);
    }
    
    // For testing purposes only
    public SoundClip(int x){
        SoundClip sc1 = new SoundClip("cut.wav");
        sc1.play();
        Logger.log("" + sc1.getClipLength());
    }
}
