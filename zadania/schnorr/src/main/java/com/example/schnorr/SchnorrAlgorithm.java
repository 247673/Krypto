package com.example.schnorr;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class SchnorrAlgorithm {
    BigInteger p, q, h, a, v, r, x, s1, s2, z;
    int qSize = 140;
    int pSize = 512;
    Random random = new Random();
    MessageDigest digest;

    public SchnorrAlgorithm() {
        generujKlucz();
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
    public void generujKlucz() {
        //parametry p, q, h sa podawane do publicznej wiadomosci
        q = BigInteger.probablePrime(qSize, random);
        do {
            p = BigInteger.probablePrime(pSize, random);
        } while (!p.subtract(BigInteger.ONE).mod(q).equals(BigInteger.ZERO)); //p = 1 mod q
        do {
            h = new BigInteger(pSize-2, random).add(BigInteger.TWO); // pSize -2 zeby h miescilo sie w zakresie h < p-1 a +2 zeby h != 1
        } while (!h.modPow(p.subtract(BigInteger.ONE).divide(q), p).equals(BigInteger.ONE)); // h^q = 1 mod p
        do {
            a = new BigInteger(pSize-1, random).add(BigInteger.ONE); // 1 < a < p-1
        } while (a.compareTo(BigInteger.ONE) <= 0 || a.compareTo(p.subtract(BigInteger.ONE)) >= 0);
        v = h.modPow(a, p).modInverse(p); // v = ((h^a)^-1) mod p
        // klucz publiczny -> v
        // klucz prywatny -> a
    }

    public BigInteger[] generujPodpis (byte[] tekst) {
        do {
            r = new BigInteger(qSize - 1, random); // 0 < r <= q-1
        } while (r.compareTo(BigInteger.ZERO) <= 0 || r.compareTo(q.subtract(BigInteger.ONE)) > 0);
        x = h.modPow(r, p); // x = (h ^ r) mod p
        String concatenatedMessage = new String(tekst) + x.toString(); //konkatenacja x z M (tekst)
        byte[] hashedConcatenatedMessage = digest.digest(concatenatedMessage.getBytes()); // digest uzywa SHA-256
        s1 = new BigInteger(1, hashedConcatenatedMessage); // s1 = f(Mx)
        s2 = r.add(a.multiply(s1)).mod(q); // s2 = (r + as1) mod q
        BigInteger[] podpis = new BigInteger[2];
        podpis[0] = s1;
        podpis[1] = s2;
        return podpis; // (s1, s2) to podpis dla M
    }

    public boolean weryfikujPodpis (byte[] tekst, BigInteger[] podpis) {
        z = h.modPow(podpis[1], p).multiply(v.modPow(podpis[0], p)).mod(p); // z = ((h ^ s2) * (v ^ s1)) mod p
        // if s1 = f(Mz) return true else return false
        String concatenatedMessage = new String(tekst) + z.toString();
        byte[] hashedConcatenatedMessage = digest.digest(concatenatedMessage.getBytes());
        s1 = new BigInteger(1, hashedConcatenatedMessage);
        if (s1.compareTo(podpis[0]) == 0) {
            return true;
        } else {
            return false;
        }
    }
}