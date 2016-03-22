package tablita;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Creado por akino on 03-17-16.
 */
public class Grafico {

    public CategoryAxis xAxis;
    public NumberAxis yAxis;
    public XYChart.Series series;
    public String labelX;
    public String labelY;
    public String titulo;
    public String nombreSerie;
    public List<BigDecimal> elementos;

    public LineChart<String, Number> lineas;

    List<String> categorias;

    {
    }


    public Grafico(List<BigDecimal> elementos, TipoGrafico tipo) {
        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        lineas = new LineChart<String,Number>(xAxis,yAxis);
        this.elementos = elementos;
        series = new XYChart.Series();

        LocalDateTime dt = LocalDateTime.now();


//        if (elementos.size() == 7) {
        if (tipo == TipoGrafico.DIARIO) {
            categorias = new ArrayList<>(Arrays.asList("Lun", "Mar", "Mie", "Jue", "Vie", "Sáb", "Dom"));
            for (int i = 1; i <= 7; i++) {
                BigDecimal val = BigDecimal.ZERO;
                if(elementos.get(i-1) != null) {
                    val = elementos.get(i - 1);
                }
                series.getData().add( new XYChart.Data(categorias.get(i-1), val));
            }
        }
//        if (elementos.size() == 12) {
        if (tipo == TipoGrafico.MENSUAL) {
            categorias = new ArrayList<>(Arrays.asList("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul","Ago","Sep","Oct","Nov","Dic"));
            for (int i = 1; i <= 12; i++) {
                BigDecimal val = BigDecimal.ZERO;
                if(elementos.get(i-1) != null) {
                    val = elementos.get(i - 1);
                }
                series.getData().add( new XYChart.Data(categorias.get(i-1), val));
            }
        }

        if(tipo == TipoGrafico.DIARIO_MES){
            for (int i = 1; i <= elementos.size(); i++) {
                BigDecimal val = BigDecimal.ZERO;
                if(elementos.get(i-1) != null) {
                    val = elementos.get(i - 1);
                }
                series.getData().add( new XYChart.Data(String.valueOf(i), val));
            }
        }

        lineas.getData().addAll(series);

        lineas.setPrefSize(900,700);

    }

    public CategoryAxis getxAxis() {
        return xAxis;
    }

    public void setxAxis(CategoryAxis xAxis) {
        this.xAxis = xAxis;
    }

    public NumberAxis getyAxis() {
        return yAxis;
    }

    public void setyAxis(NumberAxis yAxis) {
        this.yAxis = yAxis;
    }

    public XYChart.Series getSeries() {
        return series;
    }

    public void setSeries(XYChart.Series series) {
        this.series = series;
    }

    public String getLabelX() {
        return labelX;
    }

    public void setLabelX(String labelX) {
        this.labelX = labelX;
        xAxis.setLabel(this.labelX);
    }

    public String getLabelY() {
        return labelY;
    }

    public void setLabelY(String labelY) {
        this.labelY = labelY;
        yAxis.setLabel(this.labelY);
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
        lineas.setTitle(this.titulo);
    }

    public String getNombreSerie() {
        return nombreSerie;
    }

    public void setNombreSerie(String nombreSerie) {
        this.nombreSerie = nombreSerie;
        this.series.setName(this.nombreSerie);
    }

    public LineChart<String, Number> getLineas() {
        return lineas;
    }

    public void setLineas(LineChart<String, Number> lineas) {
        this.lineas = lineas;
    }
}
