package eu.arima.mejorarTesting.farmacia.reservas;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Optional;

@Service
public class PedidosAlmacenService {

    private final PedidosAlmacenRepository pedidosAlmacenRepository;
    private final AlmacenClient almacenClient;


    public PedidosAlmacenService(PedidosAlmacenRepository pedidosAlmacenRepository,
                                 AlmacenClient almacenClient) {
        this.pedidosAlmacenRepository = pedidosAlmacenRepository;
        this.almacenClient = almacenClient;
    }

    public List<PedidoAlmacen> getAllPedidosAlmacen() {
        return pedidosAlmacenRepository.findAll();
    }


    public Optional<PedidoAlmacen> realizarPedido(Long idMedicamento, Integer unidades) {

        Optional<PedidoAlmacen> pedidoAlmacen = Optional.empty();
        try {
            Long idReservaAlmacen = almacenClient.solicitarReservaMedicamento(idMedicamento, unidades);
            if (idReservaAlmacen != null) {
                pedidoAlmacen = Optional
                        .of(pedidosAlmacenRepository
                                .save(new PedidoAlmacen(idReservaAlmacen, idMedicamento, unidades)));
            }
        } catch (WebClientResponseException.BadRequest e) {
            //capturamos la excepción porque en realidad lo tratamos ya que queremos devolver empty
        }
        return pedidoAlmacen;

    }

}
