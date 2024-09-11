package org.redmark;

import java.util.concurrent.TimeUnit;
import org.redmark.ASCIIArtGenerator.ASCIIArtFont;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

public class Timer implements Runnable {

  ASCIIArtGenerator artGenerator = new ASCIIArtGenerator();
  Long delay = 0L;
  TextGraphics textGraphics;
  Screen screen;
  int terminalHeight, terminalWidth;

  Timer(String delay, TextGraphics textGraphics, Screen screen) {
    this.textGraphics = textGraphics;
    this.delay = convertToMili(delay);
    this.screen = screen;
  }

  public void run() {
    try {
      startTimer();
    } catch (Exception e) {
      System.out.println(e.toString());
    }

  }

  public void setDimensions(int terminalHeight, int terminalWidth) {
    this.terminalWidth = terminalWidth;
    this.terminalHeight = terminalHeight;
  }

  public void startTimer() throws Exception {
    System.out.println("Starting Timer for  " + delay);
    Long startTime = System.currentTimeMillis();
    Long elapsedTime = System.currentTimeMillis() - startTime;
    Long remainingTime = delay - elapsedTime;
    while (elapsedTime < delay) {
      elapsedTime = System.currentTimeMillis() - startTime;
      remainingTime = delay - elapsedTime;
      try {
        String textOutPut = artGenerator
            .printTextArt(convertoMinSec(remainingTime),
                ASCIIArtGenerator.ART_SIZE_MEDIUM, ASCIIArtFont.ART_FONT_MONO, "#");
        String[] textArray = textOutPut.split(":");
        for (int row = 5, i = 0; row < terminalHeight && i < textArray.length; row++, i++) {
          textGraphics.putString(5, row, textArray[i]);

        }
        ;
        screen.refresh();
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        System.out.println(e.toString());
      }
      resetText(textGraphics);

    }
  }

  public void stopTimer() {
    try {
      System.out.println("stopping Thread " + Thread.currentThread().toString());
      Thread.sleep(10000);
    } catch (Exception e) {
      System.out.println(e.toString());
    }
  }

  public void displayTime(Long time) {
    System.out.println(time);
  }

  public Long convertToMili(String time) {
    String mins, secs;
    Long minsL = 0L, secsL;
    if (time.split(":").length > 1) {
      mins = time.split(":")[0];
      secs = time.split(":")[1];
      minsL = Long.parseLong(mins);
      secsL = Long.parseLong(secs);
    } else {
      secs = time;
      secsL = Long.parseLong(secs);
    }
    Long millis = (minsL * 60 * 1000) + (secsL * 1000);
    return millis;

  }

  public String convertoMinSec(Long timeInMilli) {
    Long secs = timeInMilli / 1000;
    Long mins = 0L;

    if (secs >= 60) {
      mins = secs / 60;
      secs = secs % 60;
    }

    return new String(mins + "m:" + secs + "s");
  }

  public void resetText(TextGraphics textGraphics) {
    for (int row = 1; row < terminalHeight; row++) {
      textGraphics.putString(1, row, " ".repeat(terminalWidth));
    }
  }
}
