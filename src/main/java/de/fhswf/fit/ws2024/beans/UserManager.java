package de.fhswf.fit.ws2024.beans;

import java.io.Serializable;

import org.apache.commons.codec.digest.Crypt;

import de.fhswf.fit.ws2024.catalogdb.User;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

@Named("UserManager")
@SessionScoped
public class UserManager implements Serializable {
	private static final long serialVersionUID = 1L;

	private User current;

	private boolean loggedIn;

//	@Inject
//	private CatalogManagerFactory catalogManager;
	
	public UserManager() {
		current = new User();
	}

	public String login() {
		String outcome = "failure";
		if (current.getUsername() != null && !current.getUsername().isBlank()
				&& current.getPassword() != null
				&& !current.getPassword().isBlank()) {
			EntityManagerFactory factory = CatalogManagerFactory.getInstance();
			EntityManager manager = factory.createEntityManager();

			User user = manager.find(User.class, current.getUsername());

			if (Crypt.crypt(current.getPassword(), user.getPassword()).equals(user.getPassword())) {
				System.out.println("Login Successful");
				loggedIn = true;
				current = user;
				outcome = "home";
			}

		}
		return outcome;
	}

	public String logout() {
		loggedIn = false;
		current = new User();
		return "home";
	}

	public void setUsername(String username) {
		current.setUsername(username);
	}

	public String getUsername() {
		return current.getUsername();
	}

	public void setPassword(String password) {
		current.setPassword(password);
	}

	public String getPassword() {
		return current.getPassword();
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public User getCurrent() {
		EntityManagerFactory factory = CatalogManagerFactory.getInstance();
		EntityManager manager = factory.createEntityManager();

		EntityTransaction tx = manager.getTransaction();
		tx.begin();
		try {
			current = manager.find(User.class, getUsername());

			tx.commit();
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
			tx.rollback();
		}

		return current;
	}

	public void createUser() {
		EntityManagerFactory factory = CatalogManagerFactory.getInstance();
		EntityManager manager = factory.createEntityManager();
		EntityTransaction tx = manager.getTransaction();
		String generatedHash = Crypt.crypt(current.getPassword());
		current.setPassword(generatedHash);
		tx.begin();
		try {
			manager.persist(current);
			tx.commit();
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
			tx.rollback();
		}
	}
}
