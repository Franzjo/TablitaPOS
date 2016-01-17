package tablita;

import tablita.persistencia.JPAControllers.ConfigsJpaController;

import java.util.prefs.Preferences;

/**
 * Creado por akino on 12-05-15.
 */
public class Preferencias {

    ConfigsJpaController configJpa = new ConfigsJpaController(CreadorEntityManager.emf());

    public static int PROPINA = 5;

    public Preferencias() {

    }

    void init(){
        Preferences prefs = Preferences.userNodeForPackage(Preferencias.class);
    }
}
