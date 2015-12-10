package tablita;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Created by akino on 11-21-15.
 */
public class Animaciones {

    private static ScaleTransition stFront;
    private static ScaleTransition stBack;
    private static javafx.animation.TranslateTransition izqDerecha;
    private static javafx.animation.TranslateTransition derIzquierda;



    private static Node back;

    public Animaciones() {

    }


    public static void animateDerecha(Node panel){
        izqDerecha = new TranslateTransition(Duration.millis(400),panel);
        izqDerecha.setToX(0);

    }


    public static void animateAdelante(Node front){
        stFront = new ScaleTransition(Duration.millis(400),front);
        stFront.setFromX(1);
        stFront.setToX(0);
        Animaciones.back = front;

        stFront.setOnFinished(event -> {
            System.out.println("finished");
            animateReversa(Animaciones.back);
        });

        stFront.play();
        System.out.println("played");
    }

    private static void animateReversa(Node node){
        stBack = new ScaleTransition(Duration.millis(400), node);
        stBack.setFromX(0);
        stBack.setToX(1);
        System.out.println("playedBack");
        stBack.play();
    }

}
