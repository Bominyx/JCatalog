package de.fhswf.fit.ws2024.beans;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@ApplicationScoped
public class CatalogManagerFactory {

    private static EntityManagerFactory factory;

    private CatalogManagerFactory() {}
    
//    @PostConstruct
//    public void init() {
//        factory = Persistence.createEntityManagerFactory("JCatalog");
//    }

    static public EntityManagerFactory getInstance() {
    	if (factory == null) {
    		factory = Persistence.createEntityManagerFactory("JCatalog");
    	}
        return factory;
    }

    @PreDestroy
    public void destroy() {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
    }
}
