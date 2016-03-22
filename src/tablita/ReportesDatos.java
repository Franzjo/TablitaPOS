package tablita;

import tablita.persistencia.JPAControllers.VentasJpaController;
import tablita.persistencia.Productos;
import tablita.persistencia.Ventas;

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Creado por akino on 03-17-16.
 */
public class ReportesDatos {

    VentasJpaController ventasJpaController;
    private List<Ventas> ventasDiarias;

    public ReportesDatos() {
        ventasJpaController = new VentasJpaController(CreadorEntityManager.emf());
        ventasDiarias = ventasJpaController.ventasDiarias();
    }

    public BigDecimal totalDiario(){
        if(ventasDiarias.size() == 0 ) return BigDecimal.ZERO;
        return ventasDiarias.stream().filter(v -> !v.isActiva()).map(Ventas::getTotal).reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    public BigDecimal propinaTotalDiaria()
    {
        if(ventasDiarias.size() == 0 ) return BigDecimal.ZERO;
        return ventasDiarias.stream().filter(v -> !v.isActiva()).map(Ventas::getPropina).reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    public BigDecimal totalSemana(){
        List<Ventas> ventas = ventasJpaController.findTotalThisSemana();

        if(ventas.size() == 0 ) return BigDecimal.ZERO;
        return ventas.stream().filter(v -> !v.isActiva()).map(Ventas::getTotal).reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    public BigDecimal totalMes(){
        LocalDateTime hoy = LocalDateTime.now();
        List<Ventas> ventas = ventasJpaController.findByMes(hoy.getMonthValue());

        if(ventas.size() == 0 ) return BigDecimal.ZERO;
        return ventas.stream().filter(v -> !v.isActiva()).map(Ventas::getTotal).reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    /**
     * Devuelve la venta total anual
     * @return
     */
    public BigDecimal totalAnual(){
        List<Ventas> ventas = ventasJpaController.findTotalByAnio();
        if(ventas.size() == 0 ) return BigDecimal.ZERO;
        return ventas.stream().filter(v -> !v.isActiva()).map(Ventas::getTotal).reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    /**
     * Devuelve una lista con la venta diara por semana
     * @return
     */
    public List<BigDecimal> totalDiarioSemana(){
        List<Ventas> ventas = ventasJpaController.findTotalThisSemana();
        List<BigDecimal> totales = new ArrayList<>();
        if(ventas.size() == 0 ) return totales;

        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate today = LocalDate.now(ZoneId.of("America/El_Salvador"));
        LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);

        TemporalField diaDeSemana = WeekFields.of(Locale.getDefault()).dayOfWeek();

        for (int i = 1; i <= 7; i++) {
            LocalDateTime hoy = todayMidnight.with(diaDeSemana,i);
            LocalDateTime maniana = hoy.plusDays(1);

            BigDecimal ventaDia = ventas.stream()
                    .filter(ventas1 -> !ventas1.isActiva())
                    .filter(ventas1 -> isBetween(hoy.toInstant(ZoneOffset.ofHours(0)),maniana.toInstant(ZoneOffset.ofHours(0)),ventas1.getFechaHora().toInstant()))
                    .map(Ventas::getTotal)
                    .reduce(BigDecimal.ZERO,BigDecimal::add);

            totales.add(ventaDia);
        }
        return totales;
    }

    /**
     * Devuelve una lista con la venta diara de todos los días del mes dado
     * @param mes mes del cual se desea obtener la venta diara
     * @param esteMes si es True se toman todos los días del mes actual
     * @return Una lista con el total de la venta de cada mes
     */
    public List<BigDecimal> totalDiarioMensual(int mes, boolean esteMes){
        List<Ventas> ventas = ventasJpaController.findTotalThisMes();
        List<BigDecimal> totales = new ArrayList<>();
        if(ventas.size() == 0 ) return totales;

        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate today = LocalDate.now(ZoneId.of("America/El_Salvador"));

        LocalDateTime todayMidnight;

        if(!esteMes){
            todayMidnight = LocalDateTime.of(today, midnight).withMonth(mes);
        } else {
            todayMidnight = LocalDateTime.of(today, midnight);
        }

        for (int i = 1; i <= todayMidnight.getMonth().maxLength(); i++) {
            LocalDateTime hoy = todayMidnight.withDayOfMonth(i);
            LocalDateTime maniana = hoy.plusDays(1);

            BigDecimal ventaDia = ventas.stream()
                    .filter(ventas1 -> !ventas1.isActiva())
                    .filter(ventas1 -> isBetween(hoy.toInstant(ZoneOffset.ofHours(0)),maniana.toInstant(ZoneOffset.ofHours(0)),ventas1.getFechaHora().toInstant()))
                    .map(Ventas::getTotal)
                    .reduce(BigDecimal.ZERO,BigDecimal::add);

            totales.add(ventaDia);


        }

        return totales;

    }

    /**
     * Devuelve una lista de la venta total mensual para este año
     * @return lista diara de la venta mensual para este año
     */
    public List<BigDecimal> totalMensualAnual(){
        List<Ventas> ventas = ventasJpaController.findTotalByAnio();
        List<BigDecimal> totales = new ArrayList<>();
        if(ventas.size() == 0 ) return totales;

        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate today = LocalDate.now(ZoneId.of("America/El_Salvador"));

        return totalesMes(ventas, midnight, today);
    }

    /**
     * Devuelve una lista de la venta total mensual para este año
     * @param anio del cual se obtendra la venta mensual
     * @return lista diara de la venta mensual para este año
     */
    public List<BigDecimal> totalMensualAnual(int anio){
        List<Ventas> ventas = ventasJpaController.findTotalByAnio();
        List<BigDecimal> totales = new ArrayList<>();
        if(ventas.size() == 0 ) return totales;

        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate today = LocalDate.now(ZoneId.of("America/El_Salvador")).withYear(anio);

        return totalesMes(ventas, midnight, today);

    }

    /**
     * Lista los productos mas vendidos, lista desordenada
     * @param mes mes del cual se desea consultar
     * @param esteMes si es true se devuelve el mes actual
     * @return Lista desordenada de todos los productos vendidos
     */
    public List<Productos> productosPopularesMes(int mes, boolean esteMes){
        List<Ventas> ventas;
        if(esteMes) {
            ventas = ventasJpaController.findTotalThisMes();
        } else {
            ventas = ventasJpaController.findByMes(mes);
        }
        List<Productos> productos = new ArrayList<>();
        ventas.stream().filter(vt -> !vt.isActiva()).forEach(v -> {
            v.getDetallesVentasCollection().stream().forEach( dv -> {
                for (int i = 0; i < dv.getCantidad(); i++) {
                    productos.add(dv.getIdProductos());
                }
            });
        });
        return productos;
    }

    public List<Productos> productosPopularesAnual(int anio, boolean esteAnio){
        List<Ventas> ventas;
        if(esteAnio){
            ventas = ventasJpaController.findTotalByAnio();
        } else {
            ventas = ventasJpaController.findTotalByAnio(anio);
        }
        List<Productos> productos = new ArrayList<>();
        ventas.stream().filter(vt -> !vt.isActiva()).forEach(v -> {
            v.getDetallesVentasCollection().stream().forEach( dv -> {
                for (int i = 0; i < dv.getCantidad(); i++) {
                    productos.add(dv.getIdProductos());
                }
            });
        });
        return productos;
    }

    /*********************
     *                   *
     * METODOS PRIVADOS  *
     *                   *
    **********************/

    private List<BigDecimal> totalesMes(List<Ventas> ventas, LocalTime midnight, LocalDate today){
        List<BigDecimal> totales = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            LocalDateTime primerDiaMes = LocalDateTime.of(today,midnight).withDayOfMonth(1).withMonth(i);
            LocalDateTime ultimoDiaMes = primerDiaMes.plusDays(primerDiaMes.getMonth().maxLength());

            BigDecimal ventaDia = ventas.stream()
                    .filter(ventas1 -> !ventas1.isActiva())
                    .filter(ventas1 -> isBetween(primerDiaMes.toInstant(ZoneOffset.ofHours(0)),ultimoDiaMes.toInstant(ZoneOffset.ofHours(0)),ventas1.getFechaHora().toInstant()))
                    .map(Ventas::getTotal)
                    .reduce(BigDecimal.ZERO,BigDecimal::add);
            totales.add(ventaDia);
        }

        return totales;

    }

    private boolean isBetween(Instant ini, Instant fin, Instant val){
        return val.isAfter(ini) & val.isBefore(fin);
    }


}
