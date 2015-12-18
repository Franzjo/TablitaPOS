package tablita;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Creado por akino on 12-13-15.
 */
public class Codigos {

    static byte[] cut = new byte[]{27, 105};
    static byte[] lf = new byte[]{10};
    static private byte[] bytesImpresion;

    public static byte[] getBytesImpresion(byte[] bytes) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(bytesImpresion);
        outputStream.write(bytes);
        bytesImpresion = outputStream.toByteArray();
        return bytesImpresion;
    }

    public static byte[] lineFeed() {
        return new byte[]{10};
    }

    public static byte[] fuenteA(){
        return new byte[]{27, 33, 0};
    }
    public static byte[] fuenteB(){
        return new byte[]{27, 33, 1};
    }

    public static byte[] center() {
        return new byte[]{27, 97, 1};
    }

    public static byte[] alinearIzquierda() {
        return new byte[]{27, 97, 0};
    }

    public static byte[] alinearDerecha() {
        return new byte[]{27, 97, 2};
    }

    public static byte[] init() {
        return new byte[]{27, 64};
    }

    public static byte[] subrayar(){
        return new byte[]{0x1b, 0x2d, 2};
    }

    public static byte[] deSubrayar(){
        return new byte[]{0x1b, 0x2d, 0};
    }

    public static byte[] fin(){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(lf);
            outputStream.write(lf);
            outputStream.write(lf);
            outputStream.write(lf);
            outputStream.write(lf);
            outputStream.write(cut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }

    public static byte[] negrita(){
        return new byte[]{27, 69, 1};
    }

    public static byte[] desNegrita(){
        return new byte[]{27, 69, 0};
    }

    public static byte[] tab(){
        return new byte[]{9};
    }

    public static byte[] separador() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String barra = "                                       \n";

        outputStream.write(negrita());
        outputStream.write(center());
        outputStream.write(subrayar());
        outputStream.write(barra.getBytes());
        outputStream.write(alinearIzquierda());
        outputStream.write(deSubrayar());
        outputStream.write(desNegrita());

        return outputStream.toByteArray();
    }
}
