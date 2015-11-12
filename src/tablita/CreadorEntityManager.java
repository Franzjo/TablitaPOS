package tablita;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by akino on 11-09-15.
 */
public class CreadorEntityManager {
    public static EntityManagerFactory emf (){
        return Persistence.createEntityManagerFactory("TablitaPOSPU");
    }
}
