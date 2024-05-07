package com.example.schnorr;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileDao implements Dao<String> {
    private final String fileName;

    public FileDao(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public byte[] read() throws Exception
    {
        FileInputStream fis = new FileInputStream(fileName);
        int lenght = fis.available();
        byte[] data = new byte[lenght];
        fis.read(data);
        fis.close();
        return data;
    }

    @Override
    public void write(byte data[]) throws Exception
    {
        FileOutputStream fos = new FileOutputStream(fileName);
        fos.write(data);
        fos.close();
    }

    @Override
    public void writeCipher(String message) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {
            writer.write(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String readCipher() {
        StringBuilder message = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                message.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return message.toString();
    }

    @Override
    public void close() {
    }
}
