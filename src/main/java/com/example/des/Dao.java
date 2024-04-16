package com.example.des;

public interface Dao<T> extends AutoCloseable {
    byte[] read() throws Exception;
    void write(byte data[]) throws Exception;
    void writeCipher(String message);
    String readCipher() throws Exception;
}
