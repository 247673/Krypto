package com.example.des;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;

public class DES {
    static String strKey = "0123456789ABCDEF";

    static byte[] bitKey;

    static final byte[] IP = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
    };

    static final byte[] IPPowerMinus1 = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25};

    static final byte[] PC1 = {57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34,
            26, 18, 10, 2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 52, 44,
            36, 63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30,
            22, 14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 28, 20, 12, 4};
    static final byte[] PC2 = {
            14, 17, 11, 24, 1, 5,
            3, 28, 15, 6, 21, 10,
            23, 19, 12, 4, 26, 8,
            16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32
    };

    static final byte[] E = {
            32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1
    };

    final static byte[] SBoxes = {
            14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7, // S1
            0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8,
            4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0,
            15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13,
            15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10, // S2
            3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5,
            0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15,
            13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9,
            10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8, // S3
            13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1,
            13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7,
            1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12,
            7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15, // S4
            13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9,
            10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4,
            3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14,
            2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9, // S5
            14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6,
            4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14,
            11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3,
            12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11, // S6
            10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8,
            9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6,
            4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13,
            4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1, // S7
            13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6,
            1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2,
            6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12,
            13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7, // S8
            1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2,
            7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8,
            2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11
    };
    final static byte[] P = {
            16, 7, 20, 21, 29, 12, 28, 17,
            1, 15, 23, 26, 5, 18, 31, 10,
            2, 8, 24, 14, 32, 27, 3, 9,
            19, 13, 30, 6, 22, 11, 4, 25
    };
    static String coded;
    static String decrypted = "";

    public static String stringToHex(String message) {
        StringBuilder hexString = new StringBuilder();
        byte[] bytes = new byte[0];
        try {
            bytes = message.getBytes("windows-1250");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        // Przekształć każdy znak wiadomości na jego kod ASCII
        for (byte b : bytes) {
            // Zamień kod ASCII na jego reprezentację szesnastkową
            String hexValue = Integer.toHexString(b & 0xFF);

            // Dodaj zero na początku, jeśli reprezentacja szesnastkowa ma tylko jeden znak
            if (hexValue.length() == 1) {
                hexValue = "0" + hexValue;
            }

            // Dodaj reprezentację szesnastkową do wynikowego ciągu
            hexString.append(hexValue);
        }

        return hexString.toString();
    }
    public static byte[][] permuteBlocks(byte[][] array) {
        byte[][] permutatedBlock = new byte[array.length][8];
        int blockIndex = 0;
        byte current = 0;
        for (int j = 0; j<array.length; j++) {
            for (int i = 0; i < IP.length; i++) {
                int bitIndex = (IP[i]-1) % 8;
                int byteIndex = (IP[i]-1) / 8;
                String temp = byteToBits(array[j][byteIndex]);
                char bit = temp.charAt(bitIndex);
                current = (byte) (current << 1);
                if (bit == '1') current = (byte) (current | 1);
                if (i % 8 == 7) {
                    permutatedBlock[j][blockIndex++] = current;
                    current = 0;
                }
            }
            blockIndex = 0;
        }
        return permutatedBlock;
    }


    public static String encrypt(String plaintext) {
        StringBuilder C = new StringBuilder();
        String messHex = stringToHex(plaintext);
        byte[] messByte = HexToByte(messHex);
        byte[][] blocks = divideIntoBlocks(messByte);
        blocks = addPadding(blocks);
        blocks = permuteBlocks(blocks);
        byte[][] subKeys = getSubKeys();
        byte[] L = new byte[4];
        byte[] R = new byte[4];
        String[] LString = new String[17];
        String[] RString = new String[17];
        for (int i = 0; i < blocks.length; i++) {
            System.arraycopy(blocks[i], 0, L, 0, 4);
            System.arraycopy(blocks[i], 4, R, 0, 4);
            StringBuilder Lbuilder = new StringBuilder();
            StringBuilder Rbuilder = new StringBuilder();
            for (int k = 0; k<L.length; k++) {
                Lbuilder.append(byteToBits(L[k]));
                Rbuilder.append(byteToBits(R[k]));
            }
            LString[0] = String.valueOf(Lbuilder);
            RString[0] = String.valueOf(Rbuilder);
            for (int j = 1; j<17; j++) {
                LString[j] = RString[j-1];
                if (j == 16) {
                    LString[j - 1] = LString[j - 1].substring(0, 32);
                }
                RString[j] = XOR(LString[j - 1], fForEncrypting(RString, subKeys[j - 1]));
            }
            String finish = RString[16] + LString[16];
            String result = permuteWithIPMinus1(finish);
            C.append(result);
        }
        coded = binaryToHex(String.valueOf(C));
        return coded;
    }
    public static String decrypt(String hex) {
        StringBuilder D = new StringBuilder();
        byte[] messByte = HexToByte(hex);
        byte[][] blocks = divideIntoBlocks(messByte);
        byte[][] subKeys = getSubKeys();
        byte[] L = new byte[4];
        byte[] R = new byte[4];
        String[] LString = new String[17];
        String[] RString = new String[17];
        for (int i = 0; i < blocks.length; i++) {
            System.arraycopy(blocks[i], 0, L, 0, 4);
            System.arraycopy(blocks[i], 4, R, 0, 4);
            StringBuilder Lbuilder = new StringBuilder();
            StringBuilder Rbuilder = new StringBuilder();
            for (int k = 0; k<L.length; k++) {
                Lbuilder.append(byteToBits(L[k]));
                Rbuilder.append(byteToBits(R[k]));
            }
            LString[0] = String.valueOf(Lbuilder);
            RString[0] = String.valueOf(Rbuilder);
            int count = 16;
            for (int j = 1; j < 17; j++) {
                LString[j] = RString[j - 1];
                if (j == 16) {
                    LString[j] = LString[j].substring(0, 32);
                }
                RString[j] = XOR(LString[j - 1], fForEncrypting(RString, subKeys[count-j]));
            }
            String finish = RString[16] + LString[16];
            String result = permuteWithIPMinus1(finish);
            D.append(result);
        }
        System.out.println(D);
        byte[] byt = binaryStringToBytes(String.valueOf(D));
        System.out.println(Arrays.toString(byt));
        decrypted = binaryToAscii(String.valueOf(D));
        return decrypted;
    }

    public static String binaryToHex(String binaryStr) {
        // Sprawdź, czy długość ciągu binarnego jest wielokrotnością 4
        if (binaryStr.length() % 4 != 0) {
            throw new IllegalArgumentException("Długość ciągu binarnego musi być wielokrotnością 4.");
        }

        // Utwórz tablicę mapowania binarnego na szesnastkowe
        String[] binaryToHexMap = {"0000", "0001", "0010", "0011",
                "0100", "0101", "0110", "0111",
                "1000", "1001", "1010", "1011",
                "1100", "1101", "1110", "1111"};

        StringBuilder hexStr = new StringBuilder();
        // Przetwarzaj ciąg binarny po 4 bity i zamieniaj je na odpowiadające im znaki szesnastkowe
        for (int i = 0; i < binaryStr.length(); i += 4) {
            String chunk = binaryStr.substring(i, i + 4);
            for (int j = 0; j < binaryToHexMap.length; j++) {
                if (binaryToHexMap[j].equals(chunk)) {
                    hexStr.append(Integer.toHexString(j).toUpperCase());
                    break;
                }
            }
        }

        return hexStr.toString();
    }

    public static byte[] binaryStringToBytes(String binaryString) {
        int length = binaryString.length();
        byte[] bytes = new byte[length / 8];

        // Iterujemy po ciągu binarnym, dzieląc go na 8-bitowe kawałki
        for (int i = 0; i < length; i += 8) {
            String byteString = binaryString.substring(i, i + 8);
            byte b = (byte) Integer.parseInt(byteString, 2); // Parsujemy ciąg binarny do bajta
            bytes[i / 8] = b; // Wstawiamy bajt do tablicy bajtów
        }

        return bytes;
    }
    public static String removePadding(String paddedBlock) {
        int paddedValue = paddedBlock.charAt(7) - '0'; // Konwersja znaku na liczbę
        for (int i = 6; i > 7 - paddedValue; i--) {
            if (paddedBlock.charAt(i) != paddedBlock.charAt(7)) {
                return paddedBlock;
            }
        }
        return paddedBlock.substring(0, 8 - paddedValue);
    }
    public static String binaryToAscii(String binaryString) {
        StringBuilder asciiString = new StringBuilder();

        // Iteruj po ciągu binarnym, dzieląc go na kawałki po 8 bitów
        for (int i = 0; i < binaryString.length(); i += 8) {
            // Pobierz kolejny bajt z ciągu binarnego
            String byteString = binaryString.substring(i, Math.min(i + 8, binaryString.length()));

            // Przekonwertuj bajt na znak ASCII
            int asciiValue = Integer.parseInt(byteString, 2);
            char asciiChar = (char) asciiValue;

            // Dodaj znak ASCII do wynikowego ciągu
            asciiString.append(asciiChar);
        }

        return asciiString.toString();
    }

    public static String permuteWithIPMinus1(String R) {
        StringBuilder permutedMessage = new StringBuilder();
        for (int i = 0; i < IPPowerMinus1.length; i++) {
            // Pobierz indeks bitu do permutacji z tablicy E
            int index = IPPowerMinus1[i] - 1;
            // Dodaj ten bit do wiadomości permutowanej
            permutedMessage.append(R.charAt(index));
        }
        return permutedMessage.toString();
    }
    public static String fForEncrypting(String[] R, byte[] subKey){
        StringBuilder Rstring = new StringBuilder();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i<R.length; i++){
            Rstring.append(R[i]);
        }
        String permutatedR = permuteWithE(String.valueOf(Rstring));
        StringBuilder keyString = new StringBuilder();
        for (int i = 0; i<subKey.length; i++){
            keyString.append(byteToBits(subKey[i]));
        }
        String xorResult = XOR(permutatedR, String.valueOf(keyString));
        String[] B = new String[8];
        for (int i = 0; i<8; i++) {
            for (int j = 0; j < 6; j++) {
                if (B[i] == null) {
                    B[i] = String.valueOf(xorResult.charAt((6 * i) + j));
                } else {
                    B[i] += xorResult.charAt((6 * i) + j);
                }
            }
            String RowNum = String.valueOf(B[i].charAt(0)) +
                    B[i].charAt(5);
            StringBuilder ColNum = new StringBuilder();
            for (int k = 1; k<5; k++){
                ColNum.append(B[i].charAt(k));
            }
            int row = Integer.parseInt(RowNum, 2);
            int col = Integer.parseInt(String.valueOf(ColNum), 2);
            int Index = 64*i+(8 * row + col);
            result.append(byteToBits(SBoxes[Index]));
        }
        return permuteWithP(String.valueOf(result));
    }
    public static String permuteWithP(String R) {
        StringBuilder permutedMessage = new StringBuilder();
        for (int i = 0; i < P.length; i++) {
            // Pobierz indeks bitu do permutacji z tablicy E
            int index = P[i] - 1;
            // Dodaj ten bit do wiadomości permutowanej
            permutedMessage.append(R.charAt(index));
        }
        return permutedMessage.toString();
    }
    public static String permuteWithE(String R) {
        StringBuilder permutedMessage = new StringBuilder();
        for (int i = 0; i < E.length; i++) {
            // Pobierz indeks bitu do permutacji z tablicy E
            int index = E[i] - 1;
            // Dodaj ten bit do wiadomości permutowanej
            permutedMessage.append(R.charAt(index));
        }
        return permutedMessage.toString();
    }
    public static String XOR(String L, String R) {
        if (L.length() != R.length()) {
            throw new IllegalArgumentException("Długości wiadomości nie są równe");
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < L.length(); i++) {
            // Pobierz kolejne bity z obu wiadomości
            char bit1 = L.charAt(i);
            char bit2 = R.charAt(i);
            // Wykonaj operację XOR na parach bitów
            if (bit1 == bit2) {
                result.append('0'); // Bity są takie same, więc wynik jest 0
            } else {
                result.append('1'); // Bity są różne, więc wynik jest 1
            }
        }
        return result.toString();
    }

    public static byte[][] divideIntoBlocks(byte[] messByte) {
        // Określenie liczby bloków na podstawie długości ciągu binarnego i wielkości bloku
        int numBlocks = (int) Math.ceil((double) messByte.length / 8);
        // Inicjalizacja tablicy dla bloków
        byte[][] blocks = new byte[numBlocks][8];

        // Podział ciągu binarnego na bloki
        for (int i = 0; i < numBlocks; i++) {
            // Obliczenie indeksu początkowego dla bieżącego bloku
            int startIndex = i * 8;
            // Obliczenie indeksu końcowego dla bieżącego bloku
            int endIndex = Math.min((i + 1) * 8, messByte.length);
            // Wycięcie bieżącego bloku z bajtow
            for (int j = startIndex; j < endIndex; j++) {
                blocks[i][j % 8] = messByte[j];
            }
        }

        return blocks;
    }
    
    public static byte[][] addPadding(byte[][] blocks) {
        byte[][] paddedBlocks = new byte[blocks.length][8];
        for (int i = 0; i < blocks.length; i++) {
            int copyLength = Math.min(blocks[i].length, 8);
            System.arraycopy(blocks[i], 0, paddedBlocks[i], 0, copyLength);
            // Nie ma potrzeby wypełniać zerami, ponieważ nowe tablice bajtów są automatycznie inicjalizowane zerami.
        }
        return paddedBlocks;
    }


    public static byte[] HexToByte(String s) {
        byte[] key = new byte[s.length() / 2];
        for (int i = 0; i < s.length() / 2; i++) {
            key[i] = (byte) Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
        }
        return key;
    }

    public static String byteToBits(byte b)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++)
        {
            sb.append(b >> (8 - (i + 1)) & 0x0001);
        }
        return sb.toString();
    }

    public static byte[] turnOffLast() {
        byte[] key;
        key = HexToByte(strKey);
        for (int i = 0; i < strKey.length() / 2; i++) {
            key[i] = (byte) (key[i] & 0xFE);
        }
        return key;
    }
    public static byte[] permutateWithPC1(byte[] array) {
        byte[] permutatedKey = new byte[array.length - 1];
        int blockIndex = 0;
        byte current = 0;
        for (int i = 0; i < PC1.length; i++) {
            int bitIndex = (PC1[i]) % 8;
            int byteIndex = (PC1[i]) / 8;
            String temp = byteToBits(array[byteIndex]);
            char bit = temp.charAt(bitIndex);
            current = (byte) (current << 1);
            if (bit == '1') current = (byte) (current | 1);
            if (i % 8 == 7) {
                permutatedKey[blockIndex++] = current;
                current = 0;
            }
        }
        return permutatedKey;
    }

    public static byte[] hexToBytes(String tekst)
    {
        if (tekst == null || tekst.length() < 2) return null;
        else { if (tekst.length() % 2 != 0) tekst += '0';
            int len = tekst.length() / 2;
            byte[] wynik = new byte[len];
            for (int i = 0; i < len; i++) {
                wynik[i] = (byte) Integer.parseInt(tekst.substring(i * 2, i * 2 + 2), 16);
            }
            return wynik;
        }
    }

    public static byte[][] getSubKeys() {
        byte[][] subKeys = new byte[16][];
        byte[] combined;
        bitKey = hexToBytes(strKey);
        bitKey = permutate(PC1, bitKey, 7);
        byte[] leftHalf = new byte[4];
        byte[] rightHalf = new byte[4];

        System.arraycopy(bitKey, 0, leftHalf, 0, 3);
        leftHalf[3] = (byte) (bitKey[3] & 0xF0);
        rightHalf[0] = (byte) ((bitKey[3] & 0x0F) << 4 | (bitKey[4] & 0xF0) >> 4);

        for (int i = 1; i < 3; i++) {
            rightHalf[i] = (byte) ((bitKey[i + 3] & 0x0F) << 4 | (bitKey[i + 4] & 0xF0) >> 4);
        }
        rightHalf[3] = (byte) ((bitKey[6] & 0x0F) << 4);

        for (int i = 0; i < 16; i++) {
            if (i == 0 || i == 1 || i == 8 || i == 15) {
                leftHalf = rotateLeft(leftHalf,1);
                rightHalf = rotateLeft(rightHalf, 1);
            }
            else {
                leftHalf = rotateLeft(leftHalf, 2);
                rightHalf = rotateLeft(rightHalf, 2);
            }
            combined = concatenateHalves(leftHalf, rightHalf);
            subKeys[i] = permutate(PC2, combined, 6);
        }
        return subKeys;
    }

    public static byte[] concatenateHalves(byte[] leftHalf, byte[] rightHalf) {
        byte[] combined = new byte[7]; // Tablica na połączone połówki (56 bitów)

        // Kopiowanie 3 pierwszych bajtów z lewej połowy
        System.arraycopy(leftHalf, 0, combined, 0, 3);

        // Ustawienie 4. bajtu: 4 najmłodsze bity z 4. bajtu z lewej połowy + 4 najstarsze bity z 1. bajtu z prawej połowy
        combined[3] = (byte) (((leftHalf[3] & 0xF0)) | ((rightHalf[0] & 0xF0) >> 4));

        // Kopiowanie 3 kolejnych bajtów z prawej połowy, przesuwając bity
        for (int j = 4; j < 7; j++) {
            combined[j] = (byte) (((rightHalf[j - 4] & 0x0F) << 4) | ((rightHalf[j - 3] & 0xF0) >> 4));
        }

        return combined;
    }

    public static byte[] permutate(byte[] array1, byte[] array2, int n) {
        byte[] permutated = new byte[n];
        int index = 0;
        byte current = 0;

        for (int i = 0; i < array1.length; i++) {
            int bitIndex = (array1[i] - 1) % 8;
            int byteIndex = (array1[i] - 1) / 8;
            String temp = byteToBits(array2[byteIndex]);
            char bit = temp.charAt(bitIndex);
            current = (byte) (current << 1);
            if (bit == '1') current = (byte) (current | 1);
            if (i % 8 == 7) {
                permutated[index++] = current;
                current = 0;
            }
        }
        return permutated;
    }

    public static byte[] rotateLeft(byte[] in, int step) {
        byte[] out = new byte[(27) / 8 + 1];
        for (int i = 0; i < 28; i++)
        {
            int position = (i + step) % 28;
            int byteIndex = position / 8;
            int bitIndex = position % 8;
            byte temp = in[byteIndex];
            int val = temp >> (7 - bitIndex) & 1;
            int posByte = i / 8;
            int posBit = i % 8;
            byte oldByte = out[posByte];
            oldByte = (byte) (((0xFF7F >> posBit) & oldByte) & 0xFF);
            byte newByte = (byte) ((val << (7 - posBit)) | oldByte);
            out[posByte] = newByte;
        }
        return out;
    }

}