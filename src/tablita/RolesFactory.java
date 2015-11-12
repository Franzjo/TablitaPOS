package tablita;

import tablita.persistencia.JPAControllers.RolesJpaController;
import tablita.persistencia.Roles;

import javax.persistence.EntityManagerFactory;
import java.util.List;

/**
 * Created by akino on 11-09-15.
 */
public class RolesFactory {
    //EntityManagerFactory emf = CreadorEntityManager.emf();

    //RolesJpaController rolesJpa = new RolesJpaController(emf);

    private static EntityManagerFactory emf = CreadorEntityManager.emf();
    private static tablita.persistencia.JPAControllers.RolesJpaController rolesJpa = new RolesJpaController(emf);
    static List<Roles> roles = rolesJpa.findRolesEntities();

    public static Roles empleado (){
        for (Roles r: roles) {
            if(r.getNombre().equalsIgnoreCase("empleado"))
                return r;
        }
        return null;
    }
    public static Roles cajero (){
        for (Roles r: roles) {
            if(r.getNombre().equalsIgnoreCase("cajero"))
                return r;
        }
        return null;
    }
    public static Roles admin (){
        for (Roles r: roles) {
            if(r.getNombre().equalsIgnoreCase("admin"))
                return r;
        }
        return null;
    }
}
