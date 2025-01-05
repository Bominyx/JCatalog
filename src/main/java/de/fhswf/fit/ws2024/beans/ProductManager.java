package de.fhswf.fit.ws2024.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import de.fhswf.fit.ws2024.catalogdb.Product;
import de.fhswf.fit.ws2024.catalogdb.ProductCategory;
import de.fhswf.fit.ws2024.catalogdb.ProductCategoryId;
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

    @Inject
    private CategoryManager categoryManager;

    public ProductManager() {
        current = new Product();
    }

    public Product getCurrent() {
        return current;
    }

    public void select(jakarta.faces.event.ActionEvent actionEvent) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
        String selectedId = params.get("selectedId");

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

    public void createRedirect(jakarta.faces.event.ActionEvent actionEvent) {
        current = new Product();

        FacesContext context = FacesContext.getCurrentInstance();
        try {
            context.getExternalContext().redirect("product_create.jsf");
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        context.responseComplete();
    }

    public void createProduct() {
        if (current.isValid()) {
            EntityManagerFactory factory = catalogManagerFactory.getFactory();
            EntityManager manager = factory.createEntityManager();
            EntityTransaction tx = manager.getTransaction();
            tx.begin();
            try {
                manager.persist(current);
                manager.flush(); // ID wird erst nach dem Flush gesetzt

                // Relation erstellen
                ProductCategory productCategory = new ProductCategory();
                productCategory.setProduct(current);
                productCategory.setCategory(categoryManager.getCurrent());
                ProductCategoryId productCategoryId = new ProductCategoryId();
                productCategoryId.setProductId(current.getId());
                productCategoryId.setCategoryId(categoryManager.getCurrent().getId());
                productCategory.setId(productCategoryId);
                manager.persist(productCategory);
                tx.commit();
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
                tx.rollback();
            }

            FacesContext context = FacesContext.getCurrentInstance();
            try {
                context.getExternalContext().redirect("products.jsf");
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }


            context.responseComplete();
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new jakarta.faces.application.FacesMessage("Product name must not be empty."));
        }
    }

    public void updateRedirect(jakarta.faces.event.ActionEvent actionEvent) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
        Integer selectedId = Integer.valueOf((String) params.get("selectedId"));
        EntityManagerFactory factory = catalogManagerFactory.getFactory();

        EntityManager manager = factory.createEntityManager();

        EntityTransaction tx = manager.getTransaction();
        tx.begin();
        try {
            current = manager.find(Product.class, selectedId);
            tx.commit();
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            tx.rollback();
        }


        try {
            facesContext.getExternalContext().redirect("product_update.jsf");
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        facesContext.responseComplete();
    }

    public void updateProduct() {
        if (current.isValid()) {
            EntityManagerFactory factory = catalogManagerFactory.getFactory();
            EntityManager manager = factory.createEntityManager();
            EntityTransaction tx = manager.getTransaction();
            tx.begin();
            try {
                manager.merge(current);
                tx.commit();
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
                tx.rollback();
            }

            FacesContext context = FacesContext.getCurrentInstance();
            try {
                context.getExternalContext().redirect("products.jsf");
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
            context.responseComplete();
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new jakarta.faces.application.FacesMessage("Product name must not be empty."));
        }
    }

    public void deleteProduct() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
        Integer selectedId = Integer.valueOf(params.get("selectedId"));
        EntityManagerFactory factory = catalogManagerFactory.getFactory();

        EntityManager manager = factory.createEntityManager();

        EntityTransaction tx = manager.getTransaction();
        tx.begin();
        try {
            current = manager.find(Product.class, selectedId);
            manager.remove(current);
            tx.commit();

        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            tx.rollback();
        }

        try {
            facesContext.getExternalContext().redirect("products.jsf");
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        facesContext.responseComplete();
    }
}
