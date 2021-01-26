package eu.arima.mejorarTesting.farmacia.reservas;

import org.springframework.data.jpa.repository.JpaRepository;


public interface PedidosAlmacenRepository extends JpaRepository<PedidoAlmacen, Long> {

    PedidoAlmacen findByIdReservaAlmacen(long idReservaAlmacen);
}
