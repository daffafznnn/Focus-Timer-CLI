package main.services;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class NotificationService {
  private Clip clip;

  public void playSound(String soundFile) {
    try {
      File file = new File(soundFile);
      AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
      clip = AudioSystem.getClip();
      clip.open(audioStream);
      clip.start();
      clip.loop(Clip.LOOP_CONTINUOUSLY);
      System.out.println("[Notifikasi Suara]: " + soundFile);
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
      e.printStackTrace();
    }
  }

  public void stopSound() {
    if (clip != null && clip.isRunning()) {
      clip.stop();
      clip.close();
      System.out.println("[Notifikasi Suara Dihentikan]");
    }
  }
}