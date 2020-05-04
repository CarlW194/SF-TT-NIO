package blockio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class CopyTransfer {
  public static void main(String[] args) {
    for (int tries = 0; tries < 3; tries++) {
      long start = System.nanoTime();
      try (FileChannel fcIn = new FileInputStream("data.dat").getChannel();
           FileChannel fcOut = new FileOutputStream("output.dat").getChannel()) {
      fcIn.transferTo(0, fcIn.size(), fcOut);
      System.out.println("Done copying");
      } catch (IOException ioe) {
        System.out.println("IO problem " + ioe.getMessage());
      }
      long time = System.nanoTime() - start;
      System.out.println("Time to copy " + (time / 1_000_000.0) + " ms");
    }
  }
}
