package eu.arima.mejorarTesting.farmacia.medicamentos;

import org.apache.tomcat.jni.Local;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="medicamentos")
public class Medicamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String codigo;
    private int unidadesStock;
    private LocalDate fechaCaducidad;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getUnidadesStock() {
        return unidadesStock;
    }

    public void setUnidadesStock(int unidadesStock) {
        this.unidadesStock = unidadesStock;
    }

    public LocalDate getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(LocalDate fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public boolean estaCaducado() {
        if (fechaCaducidad != null){
            return fechaCaducidad.isBefore(LocalDate.now());
        }
        return false;
    }

    public boolean tieneStockSuficiente(int unidadesRequeridas) {
        return unidadesRequeridas <= unidadesStock;
    }

    public void disminuirStock(int unidades) {
        unidadesStock -= unidades;
    }
}
