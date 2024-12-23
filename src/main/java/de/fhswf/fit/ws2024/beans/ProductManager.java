package de.fhswf.fit.ws2024.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import de.fhswf.fit.ws2024.catalogdb.Product;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

@Named("ProductManager")
@SessionScoped
public class ProductManager implements Serializable {
    private static final long serialVersionUID = 1L;

    private Product current;

    @Inject
    private CatalogManagerFactory catalogManagerFactory;

    public Product getCurrent() {
        return current;
    }

    public void select(jakarta.faces.event.ActionEvent actionEvent) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
        String selectedId = (String) params.get("selectedId");

        EntityManagerFactory factory = catalogManagerFactory.getFactory();
        EntityManager manager = factory.createEntityManager();

        EntityTransaction tx = manager.getTransaction();
        tx.begin();
        try {
            System.out.println("Klasse: " + Product.class + "SelectedID: " + selectedId);
            current = manager.find(Product.class, Integer.parseInt(selectedId));
            System.out.println(current.getName() + ": " + current.getPrice());
            tx.commit();
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            tx.rollback();
        }

        FacesContext context = FacesContext.getCurrentInstance();
        try {
            context.getExternalContext().redirect("product.jsf");
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        context.responseComplete();
    }

    void createProduct() {
        EntityManagerFactory factory = catalogManagerFactory.getFactory();
        EntityManager manager = factory.createEntityManager();
        EntityTransaction tx = manager.getTransaction();
        tx.begin();
        try {
            manager.persist(current);
            tx.commit();
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            tx.rollback();
        }
    }

    public void deleteProduct() {
        EntityManagerFactory factory = catalogManagerFactory.getFactory();
        EntityManager manager = factory.createEntityManager();
        EntityTransaction tx = manager.getTransaction();
        tx.begin();
        try {
            manager.remove(current);
            tx.commit();
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            tx.rollback();
        }
    }
}
