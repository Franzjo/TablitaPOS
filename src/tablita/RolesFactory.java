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

    public static int EMPLEADOROL;
    public static int CAJEROROL;
    public static int ADMINROL;



    static List<Roles>
        roles = rolesJpa.findRolesEntities();

    public static void initialize(){
        EMPLEADOROL = empleado().getIdRol();
        CAJEROROL = cajero().getIdRol();
        ADMINROL = admin().getIdRol();
    }

    public static Roles empleado (){
        for (Roles r: roles) {
            if(r.getNombre().equalsIgnoreCase("empleado"))
                EMPLEADOROL = r.getIdRol();
                return r;
        }
        return null;
    }
    public static Roles cajero (){
        for (Roles r: roles) {
            if(r.getNombre().equalsIgnoreCase("cajero"))
                CAJEROROL = r.getIdRol();
                return r;
        }
        return null;
    }
    public static Roles admin (){
        for (Roles r: roles) {
            if(r.getNombre().equalsIgnoreCase("admin"))
                ADMINROL = r.getIdRol();
                return r;
        }
        return null;
    }
}
