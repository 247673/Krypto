package com.example.des;

public class DaoFactory {
    public static Dao<String> getFileDao(String fileName) {
        if (fileName == null) {
            throw new NullPointerException("Nie podano nazwy pliku");
        }
        return new FileDao(fileName);
    }
}
