package de.fhswf.fit.ws2024.catalogdb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.codec.digest.Crypt;

import java.util.Objects;

@Entity
@Table(name = "user")
public class User {
    @Id
    @Size(max = 20)
    @Column(name = "username", nullable = false, length = 20)
    private String username;

    @Size(max = 128)
    @NotNull
    @Column(name = "password", nullable = false, length = 128)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }

    /**
     * Es genügt der Gültigkeit, wenn ein Name und Passwort existieren, welche nicht leer sind.
     * @return Boolescher Wert, ob die Kategorie gültig ist.
     */
    public boolean isValid() {
        return username != null && !username.isBlank() && password != null && !password.isBlank();
    }

    /**
     * Überprüft, ob das übergebene Passwort mit dem gespeicherten Passwort übereinstimmt.
     * @param password Das zu überprüfende Passwort.
     * @return Boolescher Wert, ob das Passwort korrekt ist.
     */
    public boolean checkPassword(String password) {
        return this.password.equals(Crypt.crypt(password, this.password));
    }
}