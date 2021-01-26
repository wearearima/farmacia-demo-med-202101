package eu.arima.mejorarTesting.farmacia.reservas;

import java.util.Optional;

public class InfoRecogidaReserva {
    private final Optional<Long> idReserva;
    private final int unidadesRecoger;
    private final int unidadesPendientes;

    public InfoRecogidaReserva(Optional<Long> idReserva, int unidadesRecoger, int unidadesPendientes) {
        this.idReserva = idReserva;
        this.unidadesRecoger = unidadesRecoger;
        this.unidadesPendientes = unidadesPendientes;
    }

    public Optional<Long> getIdReserva() {
        return idReserva;
    }

    public int getUnidadesRecoger() {
        return unidadesRecoger;
    }

    public int getUnidadesPendientes() {
        return unidadesPendientes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InfoRecogidaReserva that = (InfoRecogidaReserva) o;

        if (unidadesRecoger != that.unidadesRecoger) {
            return false;
        }
        if (unidadesPendientes != that.unidadesPendientes) {
            return false;
        }
        return idReserva.equals(that.idReserva);
    }

    @Override
    public int hashCode() {
        int result = idReserva.hashCode();
        result = 31 * result + unidadesRecoger;
        result = 31 * result + unidadesPendientes;
        return result;
    }
}
