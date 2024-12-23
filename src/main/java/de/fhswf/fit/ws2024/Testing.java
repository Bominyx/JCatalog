package de.fhswf.fit.ws2024;

import de.fhswf.fit.ws2024.beans.UserManager;
import org.apache.commons.codec.digest.Crypt;

public class Testing {
    public static void main(String[] args) {
        String generatedHash = Crypt.crypt("ABC");
        System.out.println("generatedHash: " + generatedHash);

        UserManager userManager = new UserManager();
        userManager.setUsername("TestUser7");
        userManager.setPassword("TestPassword");
        System.out.println("Username: " + userManager.getUsername());
        System.out.println("Password: " + userManager.getPassword());
        userManager.createUser();
        userManager.setPassword("TestPassword");
        System.out.println(userManager.login());
    }
}
