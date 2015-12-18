package tablita;

import tablita.persistencia.DetallesVentas;
import tablita.persistencia.Ventas;

import javax.print.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

/**
 * Created by akino on 11-24-15.
 */
public class ServicioImpresion {






    private static byte[] titulo(){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String nombre = "RESTAURANTE LA TABLITA CAMPESTRE\n";

        try{
            outputStream.write(Codigos.fuenteA());
            outputStream.write(Codigos.center());
            outputStream.write(Codigos.subrayar());
            outputStream.write(nombre.getBytes());
            outputStream.write(Codigos.alinearIzquierda());
            outputStream.write(Codigos.deSubrayar());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }

    private static byte[] encabezadoCocina(Ventas v) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String mesa = "Mesa";
        String numero = String.valueOf(v.getIdMesa().getNumeroMesa())+"\n";
        String horaText = "Hora";
        String hora = String.valueOf(java.time.LocalTime.now())+"\n";
        String item = "ITEM\n";
        String cantidad = "CANTIDAD";
        try {
            outputStream.write(Codigos.fuenteB());
            outputStream.write(Codigos.subrayar());
            outputStream.write(Codigos.separador());
            outputStream.write(Codigos.deSubrayar());
            outputStream.write(mesa.getBytes());
            outputStream.write(Codigos.tab());
            outputStream.write(numero.getBytes());
            outputStream.write(horaText.getBytes());
            outputStream.write(Codigos.tab());
            outputStream.write(hora.getBytes());
            outputStream.write(cantidad.getBytes());
            outputStream.write(Codigos.tab());
            outputStream.write(Codigos.tab());
            outputStream.write(Codigos.tab());
            outputStream.write(item.getBytes());


        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }

    public static void imprimirCocina(List<DetallesVentas> detallesVentas) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;

        PrintService[] pservices =
                PrintServiceLookup.lookupPrintServices(flavor, null);

        PrintService ps;
        if(pservices.length > 0){
            ps = PrintServiceLookup.lookupDefaultPrintService();
        } else {
            return;
        }

        outputStream.write(Codigos.init());
        outputStream.write(titulo());
        outputStream.write(encabezadoCocina(detallesVentas.get(0).getIdVentas()));

        for (DetallesVentas dv :
                detallesVentas) {
            String item = dv.getIdProductos().getItem() + "\n";
            String cantidad = String.valueOf(dv.getCantidad());

            outputStream.write(Codigos.fuenteA());
            outputStream.write(cantidad.getBytes());
            outputStream.write(Codigos.tab());
            outputStream.write(Codigos.tab());
            outputStream.write(Codigos.tab());
            outputStream.write(item.getBytes());
        }

        outputStream.write(Codigos.fin());

        byte[] impresion = outputStream.toByteArray();
        Doc doc = new SimpleDoc(impresion, flavor, null);
        DocPrintJob job = ps.createPrintJob();

        try {
            job.print(doc, null);
        } catch (Exception er) {

        }
    }

    public static void imprimirTicket(List<DetallesVentas> detallesVentas, Ventas ventas) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;

        PrintService[] pservices =
                PrintServiceLookup.lookupPrintServices(flavor, null);

        PrintService ps;
        if(pservices.length > 0){
            ps = PrintServiceLookup.lookupDefaultPrintService();
        } else {
            return;
        }

        outputStream.write(Codigos.init());
        outputStream.write(titulo());
        outputStream.write(encabezadoTicket(ventas));

        for (DetallesVentas dv :
                detallesVentas) {
            String item = dv.getIdProductos().getItem() + "\n";
            String cantidad = String.valueOf(dv.getCantidad());
            String precio = "$ "+String.valueOf(dv.getIdProductos().getPrecio());
            BigDecimal t = dv.getIdProductos().getPrecio().multiply(new BigDecimal(dv.getCantidad()));
            String total = "$ "+String.valueOf(t)+"\n";

            outputStream.write(Codigos.fuenteA());

            outputStream.write(item.getBytes());

            outputStream.write(Codigos.tab());
            outputStream.write(Codigos.tab());
            outputStream.write(cantidad.getBytes());

            outputStream.write(Codigos.tab());
            outputStream.write(precio.getBytes());

            outputStream.write(Codigos.tab());
            outputStream.write(Codigos.tab());
            outputStream.write(total.getBytes());
        }

        outputStream.write(totalTicket(ventas));
        outputStream.write(footer());

        outputStream.write(Codigos.fin());

        byte[] impresion = outputStream.toByteArray();
        Doc doc = new SimpleDoc(impresion, flavor, null);
        DocPrintJob job = ps.createPrintJob();

        try {
            job.print(doc, null);
        } catch (Exception er) {

        }
    }

    private static byte[] encabezadoTicket(Ventas ventas) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String mesa = "Mesa: ";
        String numero = String.valueOf(ventas.getIdMesa().getNumeroMesa());
        String fecha = "Fecha: ";
        LocalDate ldt = ventas.getFechaHora().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String fechaF = String.valueOf(ldt);


        String inicio = "Inicio: ";
        LocalTime ltInicio = ventas.getFechaHora().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
        String horaInicio = String.valueOf(ltInicio);

        String fin = "Fin: ";
        LocalTime ltFin = java.time.LocalTime.now();
        String horaFin = String.valueOf(ltFin);

        String item = "ITEM/CANTIDAD";
        String precio = "PRECIO";
        String total = "TOTAL";
        try {
            outputStream.write(Codigos.fuenteB());
            outputStream.write(Codigos.separador());
            //mesa
            outputStream.write(Codigos.negrita());
            outputStream.write(mesa.getBytes());
            outputStream.write(Codigos.desNegrita());
            outputStream.write(numero.getBytes());
            outputStream.write(Codigos.tab());
            outputStream.write(Codigos.tab());

            //Fecha
            outputStream.write(Codigos.negrita());
            outputStream.write(fecha.getBytes());
            outputStream.write(Codigos.desNegrita());
            outputStream.write(fechaF.getBytes());
            outputStream.write(Codigos.tab());

            outputStream.write(Codigos.lineFeed());

            //Inicio
            outputStream.write(Codigos.negrita());
            outputStream.write(inicio.getBytes());
            outputStream.write(Codigos.desNegrita());
            outputStream.write(horaInicio.getBytes());
            outputStream.write(Codigos.tab());

            //Fin
            outputStream.write(Codigos.negrita());
            outputStream.write(fin.getBytes());
            outputStream.write(Codigos.desNegrita());
            outputStream.write(horaFin.getBytes());
            outputStream.write(Codigos.tab());

            outputStream.write(Codigos.lineFeed());


            //Titulos
            outputStream.write(Codigos.fuenteA());
            outputStream.write(Codigos.negrita());
            outputStream.write(Codigos.subrayar());

            outputStream.write(item.getBytes());
            outputStream.write(Codigos.tab());
            outputStream.write(precio.getBytes());
            outputStream.write(Codigos.tab());
            outputStream.write(Codigos.tab());
            outputStream.write(total.getBytes());
            outputStream.write(Codigos.desNegrita());
            outputStream.write(Codigos.deSubrayar());
            outputStream.write(Codigos.lineFeed());


        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }

    private static byte[] totalTicket(Ventas ventas) throws IOException {
        String total = "Total:";
        String totalCobro = "$ "+String.valueOf(ventas.getTotal())+"\n";
        String subTotal = "SubTotal:";
        String subTotalCobro = "$ "+ String.valueOf(ventas.getSubtotal())+"\n";
        String propina = "Propina (5%):";
        String propinaCobro =  "$ "+String.valueOf(ventas.getPropina())+"\n";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();


        outputStream.write(Codigos.separador());
        outputStream.write(Codigos.tab());
        outputStream.write(Codigos.tab());
        outputStream.write(subTotal.getBytes());
        outputStream.write(Codigos.tab());
        outputStream.write(subTotalCobro.getBytes());

        outputStream.write(Codigos.tab());
        outputStream.write(Codigos.tab());
        outputStream.write(propina.getBytes());
        outputStream.write(Codigos.tab());
        outputStream.write(Codigos.tab());
        outputStream.write(propinaCobro.getBytes());

        outputStream.write(Codigos.negrita());
        outputStream.write(Codigos.tab());
        outputStream.write(Codigos.tab());
        outputStream.write(total.getBytes());
        outputStream.write(Codigos.tab());
        outputStream.write(Codigos.tab());
        outputStream.write(totalCobro.getBytes());
        outputStream.write(Codigos.desNegrita());
        outputStream.write(Codigos.lineFeed());
        outputStream.write(Codigos.separador());
        return outputStream.toByteArray();
    }

    private static byte[] footer() throws IOException {
        String footer = "LE ESPERAMOS PRONTO";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        outputStream.write(Codigos.center());
        outputStream.write(footer.getBytes());

        return outputStream.toByteArray();
    }
}