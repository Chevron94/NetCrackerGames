/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.encode;

import java.util.Random;

/**
 *
 * @author Ivan
 */
public class passwordGenerator {

    public static String generatePassword(int length) {
        String passwordChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(length);
        Random rnd = new Random();

        for (int i = 0; i < sb.capacity(); i++) {
            char nextChar = passwordChars.charAt(rnd.nextInt(passwordChars.length()));
            sb.append(nextChar);
        }

        return sb.toString();
    }
}
