package de.fhswf.fit.ws2024.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.fhswf.fit.ws2024.catalogdb.Category;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

@Named("CategoryManager")
@SessionScoped
public class CategoryManager implements Serializable
{
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private Category current;

//   @Inject
//   private CatalogManagerFactory catalogManager;
   
   public Category getCurrent()
   {
      return current;
   }

   public void setCurrent(Category current)
   {
      this.current = current;
   }

   public Collection getCategories()
   {
      EntityManagerFactory factory = CatalogManagerFactory.getInstance();
      EntityManager manager = factory.createEntityManager();
      List<Category> categories = manager.createQuery("SELECT c FROM Category c").getResultList();
      System.out.println("Categories fetched: " + categories.size());
//      return manager.createQuery("SELECT c FROM Category c ").getResultList();
      return categories;
   }

   public void select(jakarta.faces.event.ActionEvent actionEvent)
   {
      FacesContext facesContext = FacesContext.getCurrentInstance();
      Map params = facesContext.getExternalContext().getRequestParameterMap();
      Integer selectedId = Integer.valueOf((String) params.get("selectedId"));
//      System.out.println(selectedId);

//      EntityManagerFactory factory = Persistence
//            .createEntityManagerFactory("catalog");
      EntityManagerFactory factory = CatalogManagerFactory.getInstance();
      
      EntityManager manager = factory.createEntityManager();

      EntityTransaction tx = manager.getTransaction();
      tx.begin();
      try
      {
         current = manager.find(Category.class, selectedId);
//         System.out.println(current.getName());
         tx.commit();
      }
      catch (Exception ex)
      {
         ex.printStackTrace(System.err);
         tx.rollback();
      }

      FacesContext context = FacesContext.getCurrentInstance();
      try
      {
         context.getExternalContext().redirect("products.jsf");
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      context.responseComplete();
   }

    public void createCategory(){
            EntityManagerFactory factory = CatalogManagerFactory.getInstance();
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

    public void deleteCategory(){
            EntityManagerFactory factory = CatalogManagerFactory.getInstance();
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
