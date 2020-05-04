package blockio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.ThreadLocalRandom;

public class MakeLargeFile {
  public static void main(String[] args) {
    long start = System.nanoTime();
    try (FileChannel fc = new FileOutputStream("data.dat").getChannel();) {
      byte[] byteArray = new byte[1024 * 1024]; // 1 MB
      for (int i = 0; i < 1024; i++) {
        ThreadLocalRandom.current().nextBytes(byteArray);
        ByteBuffer bb = ByteBuffer.wrap(byteArray);
        fc.write(bb);
      }
      System.out.println("Finished writing");
    } catch (IOException ioe) {
      System.out.println("IO problem " + ioe.getMessage());
    }
    long time = System.nanoTime() - start;
    System.out.println("try completed, time was " + (time / 1_000_000.0)
        + " milliseconds");
  }
}
