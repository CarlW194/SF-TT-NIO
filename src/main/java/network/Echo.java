package network;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;

public class Echo {
  public static void main(String[] args) throws Throwable {
    Selector sel = Selector.open();
    ServerSocketChannel ssc = ServerSocketChannel.open();
    ssc.bind(new InetSocketAddress(8000));
    ssc.configureBlocking(false);
    ssc.register(sel, SelectionKey.OP_ACCEPT);

    ByteBuffer bb = ByteBuffer.allocate(4096);
    CharBuffer cb = CharBuffer.allocate(4096);
    Charset charset = Charset.forName("UTF-8");
    CharsetEncoder encoder = charset.newEncoder();
    CharsetDecoder decoder = charset.newDecoder();
    while(true) {
      sel.select(); // blocks until something interesting...
//      System.out.println("Selected something!!!");
      Iterator<SelectionKey> keys = sel.selectedKeys().iterator();
      while (keys.hasNext()) {
        SelectionKey key = keys.next();
        int readyOps = key.readyOps();
        if ((readyOps & SelectionKey.OP_ACCEPT) != 0) {
          System.out.println("Accepted something");
          SocketChannel sc = ((ServerSocketChannel)(key.channel())).accept();
          sc.configureBlocking(false);
          sc.register(sel, SelectionKey.OP_READ);
        }
        if ((readyOps & SelectionKey.OP_READ) != 0) {
          SocketChannel sc = (SocketChannel)key.channel();
          sc.read(bb); // read IO, write INTO buffer
          bb.flip(); // Prepare buffer for read FROM
          decoder.decode(bb, cb, true); // Read FROM bb and writes INTO cb
          bb.clear(); // Ready for next operation
          cb.flip(); // prepare char buffer for read FROM
          String text = cb.toString(); // read FROM cb
          cb.clear();
          System.out.println("READ: " + text);
          // now prepare for echo...
          sc.register(sel, SelectionKey.OP_WRITE, text);
        }
        if ((readyOps & SelectionKey.OP_WRITE) != 0) {
          SocketChannel sc = (SocketChannel)key.channel();
          String response = (String)key.attachment();
          cb.put("REPLY" + response + "\n"); // Write INTO cb
          cb.flip(); // prepare to read FROM cb
          encoder.encode(cb, bb, true);
          bb.flip();
          cb.clear();
          sc.write(bb);
          bb.clear();
          sc.close();
        }
        keys.remove();
      }
    }
  }
}
