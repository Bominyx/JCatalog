package de.fhswf.fit.ws2024.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import de.fhswf.fit.ws2024.catalogdb.Category;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

@Named("CategoryManager")
@SessionScoped
public class CategoryManager implements Serializable {
    private static final long serialVersionUID = 1L;

    private Category current;

    @Inject
    private CatalogManagerFactory catalogManagerFactory;

    public CategoryManager() {
        current = new Category();
    }

    public Category getCurrent() {
        return current;
    }

    public void setCurrent(Category current) {
        this.current = current;
    }

    public List<Category> getCategories() {
        EntityManagerFactory factory = catalogManagerFactory.getFactory();
        EntityManager manager = factory.createEntityManager();
        List<Category> categories = manager.createQuery("SELECT c FROM Category c").getResultList();
        System.out.println("Categories fetched: " + categories.size());
        return categories;
    }

    public void select(jakarta.faces.event.ActionEvent actionEvent) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
        Integer selectedId = Integer.valueOf((String) params.get("selectedId"));
        EntityManagerFactory factory = catalogManagerFactory.getFactory();

        EntityManager manager = factory.createEntityManager();

        EntityTransaction tx = manager.getTransaction();
        tx.begin();
        try {
            current = manager.find(Category.class, selectedId);
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

    public void createCategory() {
        if (current.isValid()) {
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

            FacesContext context = FacesContext.getCurrentInstance();
            try {
                context.getExternalContext().redirect("categories.jsf");
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
            context.responseComplete();
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new jakarta.faces.application.FacesMessage("Category name must not be empty."));
        }
    }

    public void updateCategory() {
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
                context.getExternalContext().redirect("categories.jsf");
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
            context.responseComplete();
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new jakarta.faces.application.FacesMessage("Category name must not be empty."));
        }
    }

    public void deleteCategory() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
        Integer selectedId = Integer.valueOf(params.get("selectedId"));
        EntityManagerFactory factory = catalogManagerFactory.getFactory();

        EntityManager manager = factory.createEntityManager();

        EntityTransaction tx = manager.getTransaction();
        tx.begin();
        try {
            current = manager.find(Category.class, selectedId);
            if (current.getProducts().isEmpty()) {
                manager.remove(current);
                tx.commit();
            } else {
                tx.rollback();

                FacesContext.getCurrentInstance().addMessage(null,
                        new jakarta.faces.application.FacesMessage("Category cannot be deleted because it has associated products."));
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            tx.rollback();
        }

        try {
            facesContext.getExternalContext().redirect("categories.jsf");
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        facesContext.responseComplete();
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
            current = manager.find(Category.class, selectedId);
            tx.commit();
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            tx.rollback();
        }


        try {
            facesContext.getExternalContext().redirect("category_update.jsf");
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        facesContext.responseComplete();
    }

    public void createRedirect(jakarta.faces.event.ActionEvent actionEvent) {
        current = new Category();

        FacesContext context = FacesContext.getCurrentInstance();
        try {
            context.getExternalContext().redirect("category_create.jsf");
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        context.responseComplete();
    }
}

