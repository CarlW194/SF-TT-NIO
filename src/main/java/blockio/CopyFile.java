package blockio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CopyFile {
  public static void main(String[] args) {
    for (int tries = 0; tries < 3; tries++) {
      long start = System.nanoTime();
      try (FileInputStream fis = new FileInputStream("data.dat");
           FileOutputStream fos = new FileOutputStream("output.dat")) {
        byte[] buf = new byte[1024 * 1024];
        int count;
        while ((count = fis.read(buf)) > 0) {
          fos.write(buf, 0, count);
        }
        System.out.println("Done copying");
      } catch (IOException ioe) {
        System.out.println("IO problem " + ioe.getMessage());
      }
      long time = System.nanoTime() - start;
      System.out.println("Time to copy " + (time / 1_000_000.0) + " ms");
    }
  }
}
