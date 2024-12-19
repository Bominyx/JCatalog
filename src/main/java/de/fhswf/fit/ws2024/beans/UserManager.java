package de.fhswf.fit.ws2024.beans;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.codec.digest.Crypt;

import de.fhswf.fit.ws2024.catalogdb.User;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

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
		if (current.getUsername() != null && !current.getUsername().isEmpty()
				&& current.getPassword() != null
				&& !current.getPassword().isEmpty()) {
			EntityManagerFactory factory = CatalogManagerFactory.getInstance();
			EntityManager manager = factory.createEntityManager();
			
			Query query = manager.createQuery("SELECT u FROM User u where u.username = :username");
			query.setParameter("username", current.getUsername());

			List<User> results = query.getResultList();
			if (!results.isEmpty()) {
				String pwHashDB = results.get(0).getPassword();
				String[] parts = pwHashDB.split("\\$");
				String salt = "$" + parts[1] + "$" + parts[2];
				System.out.println("salt: " + salt);

				String generatedHash = Crypt.crypt(current.getPassword(), salt);
				System.out.println("pwHashDB: " + pwHashDB);
				System.out.println("genHash: " + generatedHash);
				if (generatedHash.equals(pwHashDB)) {
					System.out.println("Login Successful");
					loggedIn = true;
					current = (User) results.get(0);
					outcome = "success";
				}
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
