package eu.arima.mejorarTesting.farmacia.reservas;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pedidosAlmacen")
public class PedidoAlmacen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long idReservaAlmacen;

    private long idMedicamento;

    private int unidadesPedidas;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate fechaPedido;

    public PedidoAlmacen(long idReservaAlmacen, long idMedicamento, int unidades) {
        this.idReservaAlmacen = idReservaAlmacen;
        this.idMedicamento = idMedicamento;
        this.unidadesPedidas = unidades;
    }

    public PedidoAlmacen() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdReservaAlmacen() {
        return idReservaAlmacen;
    }

    public void setIdReservaAlmacen(long idReservaAlmacen) {
        this.idReservaAlmacen = idReservaAlmacen;
    }

    public long getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(long idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public int getUnidadesPedidas() {
        return unidadesPedidas;
    }

    public void setUnidadesPedidas(int unidadesPedidas) {
        this.unidadesPedidas = unidadesPedidas;
    }

    public LocalDate getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDate fechaPedido) {
        this.fechaPedido = fechaPedido;
    }
    @PrePersist
    void prePersist() {
        this.fechaPedido = LocalDate.now();
    }
}
