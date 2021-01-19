package eu.arima.mejorarTesting.farmacia.reservas;

public class SolicitudReserva {
    private long idMedicamento;
    private int unidades;

    public SolicitudReserva(long idMedicamento, int unidades) {

        this.idMedicamento = idMedicamento;
        this.unidades = unidades;
    }

    public long getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(long idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }
}
