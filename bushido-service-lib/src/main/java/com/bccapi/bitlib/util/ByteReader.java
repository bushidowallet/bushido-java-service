package com.bccapi.bitlib.util;

import com.bccapi.bitlib.model.CompactInt;

import java.nio.charset.Charset;

public class ByteReader {

   public static class InsufficientBytesException extends Exception {

      private static final long serialVersionUID = 1L;
   }

   private static final Charset UTF8_CHARSET = Charset.forName("UTF8");
   
   private byte[] _buf;
   private int _index;

   public ByteReader(byte[] buf) {
      _buf = buf;
      _index = 0;
   }

   public ByteReader(byte[] buf, int index) {
      _buf = buf;
      _index = index;
   }

   public byte get() throws InsufficientBytesException {
      checkAvailable(1);
      return _buf[_index++];
   }

   public int getShortLE() throws InsufficientBytesException {
      checkAvailable(2);
      return (((_buf[_index++] & 0xFF) << 0) | ((_buf[_index++] & 0xFF) << 8)) & 0xFFFF;
   }

   public int getIntLE() throws InsufficientBytesException {
      checkAvailable(4);
      return ((_buf[_index++] & 0xFF) << 0) | ((_buf[_index++] & 0xFF) << 8) | ((_buf[_index++] & 0xFF) << 16)
            | ((_buf[_index++] & 0xFF) << 24);
   }

   public long getLongLE() throws InsufficientBytesException {
      checkAvailable(8);
      return ((_buf[_index++] & 0xFFL) << 0) | ((_buf[_index++] & 0xFFL) << 8) | ((_buf[_index++] & 0xFFL) << 16)
            | ((_buf[_index++] & 0xFFL) << 24) | ((_buf[_index++] & 0xFFL) << 32) | ((_buf[_index++] & 0xFFL) << 40)
            | ((_buf[_index++] & 0xFFL) << 48) | ((_buf[_index++] & 0xFFL) << 56);
   }

   /**
    * Gets a java long type from 4 bytes of data representing 32-bit unsigned integer
    * @return 32-bit unsigned integer
    * @throws InsufficientBytesException when less than 4 bytes available
    */
   public long getUInt32() throws InsufficientBytesException {
      checkAvailable(4);
      byte[] intBytes = getBytes(4);
      return byteAsULong(intBytes[0]) | (byteAsULong(intBytes[1]) << 8) | (byteAsULong(intBytes[2]) << 16) | (byteAsULong(intBytes[3]) << 24);
   }

   public int getUInt8() throws InsufficientBytesException {
      checkAvailable(1);
      return ((_buf[_index++] & 0xFF) << 0);
   }

   public int getUInt16LE() throws InsufficientBytesException {
      checkAvailable(2);
      return ((_buf[_index++] & 0xFF) << 0) | ((_buf[_index++] & 0xFF) << 8);
   }

   public int getUInt32LE() throws InsufficientBytesException {
      checkAvailable(4);
      return ((_buf[_index++] & 0xFF) << 0) | ((_buf[_index++] & 0xFF) << 8) | ((_buf[_index++] & 0xFF) << 16)
              | ((_buf[_index++] & 0xFF) << 24);
   }

   private long byteAsULong(byte b) {
      return ((long) b) & 0x00000000000000FFL;
   }

   public byte[] getBytes(int size) throws InsufficientBytesException {
      checkAvailable(size);
      byte[] bytes = new byte[size];
      System.arraycopy(_buf, _index, bytes, 0, size);
      _index += size;
      return bytes;
   }

   public String getString() throws InsufficientBytesException {
      int length = getIntLE();
      byte[] bytes = getBytes(length);
      return new String(bytes, UTF8_CHARSET);
   }

   public void skip(int num) throws InsufficientBytesException {
      checkAvailable(num);
      _index += num;
   }

   public void reset() {
      _index = 0;
   }

   public long getCompactInt() throws InsufficientBytesException {
      return CompactInt.fromByteReader(this);
   }

   public Sha256Hash getSha256Hash() throws InsufficientBytesException {
      return new Sha256Hash(getBytes(32));
   }

   public Sha256Hash getSha256Hash(boolean reverse) throws InsufficientBytesException {
      checkAvailable(32);
      if (reverse) {
         return new Sha256Hash(BitUtils.reverseBytes(getBytes(32)));
      }
      return new Sha256Hash(getBytes(32));
   }

   public int getPosition() {
      return _index;
   }

   public void setPosition(int index) {
      _index = index;
   }

   public final int available() {
      return _buf.length - _index;
   }

   private final void checkAvailable(int num) throws InsufficientBytesException {
      if (_buf.length - _index < num) {
         throw new InsufficientBytesException();
      }
   }
}
