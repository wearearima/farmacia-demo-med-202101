package eu.arima.mejorarTesting.farmacia.reservas;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservasController {

    ReservasService reservasService;
    PedidosAlmacenService pedidosAlmacenService;

    public ReservasController(ReservasService reservasService,
                              PedidosAlmacenService pedidosAlmacenService) {
        this.reservasService = reservasService;
        this.pedidosAlmacenService = pedidosAlmacenService;
    }

    @GetMapping
    List<Reserva> listarReservas() {
        return reservasService.getAllReservas();
    }

    @PostMapping
    InfoRecogidaReserva reservar(@Validated @RequestBody SolicitudReserva solicitudReserva) {
        return reservasService.reservarMedicamento(solicitudReserva.getIdMedicamento(), solicitudReserva.getUnidades());
    }

    @GetMapping("/almacen")
    List<PedidoAlmacen> listarPedidosAlmacen() {
        return pedidosAlmacenService.getAllPedidosAlmacen();
    }
}
