package de.fhswf.fit.ws2024.beans;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@ApplicationScoped
public class CatalogManagerFactory {

    private EntityManagerFactory factory;
    
    @PostConstruct
    public void init() {
        factory = Persistence.createEntityManagerFactory("JCatalog");
    }

    public EntityManagerFactory getFactory() {
        return factory;
    }

    @PreDestroy
    public void destroy() {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
    }

}
