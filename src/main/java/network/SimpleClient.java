package network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SimpleClient {
  public static void main(String[] args) throws Throwable {
    Socket socket = new Socket("127.0.0.1", 8000);
    System.out.println("Connection accepted");
    PrintWriter pw = new PrintWriter(socket.getOutputStream());
    pw.println("Hello, is there anybody there...");
    pw.flush();
    BufferedReader br = new BufferedReader(
        new InputStreamReader(socket.getInputStream()));
    System.out.println(br.readLine());
  }
}
