package tablita;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

/**
 * Created by akino on 11-09-15.
 */
public class CreadorEntityManager {

    private static final String HOST = "HOST";
    private static final String NODE = "/tablita/controllers";
    private static final String USER = "USER";
    private static final String PASS = "PASS";

    private static Map<String, String> connectionSettings = new HashMap<>();

    public static EntityManagerFactory emf (){
        Preferences preferences = Preferences.userRoot().node(NODE);

        String url = "jdbc:mysql://"+preferences.get(HOST, "localhost")+":3306/TablitaDB";
        System.out.println("url = " + url);

        String user = preferences.get(USER,"root");
        System.out.println("user = " + user);

        String pass = preferences.get(PASS, "isss");
        System.out.println("pass = " + pass);

        connectionSettings.put("javax.persistence.jdbc.driver", "com.mysql.jdbc.Driver");
        connectionSettings.put("javax.persistence.jdbc.url", url);
        connectionSettings.put("javax.persistence.jdbc.user", user);
        connectionSettings.put("javax.persistence.jdbc.password", pass);

        return Persistence.createEntityManagerFactory("TablitaPOSPU", connectionSettings);
    }
}
