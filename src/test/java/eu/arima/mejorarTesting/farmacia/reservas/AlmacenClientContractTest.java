package eu.arima.mejorarTesting.farmacia.reservas;

import eu.arima.mejorarTesting.farmacia.WebClientTestConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {AlmacenClient.class})
@AutoConfigureStubRunner(ids = {"eu.arima.mejorarTesting:almacen:+:stubs:9091"})
@ContextConfiguration(classes = {WebClientTestConfiguration.class})
class AlmacenClientContractTest {

    public static final long ID_MED_DISPONIBLE = 4L;
    public static final long ID_MED_NO_DISPONIBLE = 34L;

    public static final int UNIDADES = 10;
    public static final long ID_RESERVA_ALMACEN = 1L;

    @Autowired
    private AlmacenClient almacenClient;

    @Test
    @DisplayName("realizarPedido cuando se hace una petición de una reserva con disponibilidad se recupera la información de la reserva generada en el almacen")
    void realizarPedido_solicitar_pedido_almacen_OK() {

        Long resultado = almacenClient.solicitarReservaMedicamento(ID_MED_DISPONIBLE, UNIDADES);
        assertEquals(ID_RESERVA_ALMACEN, resultado);

    }

    @Test
    @DisplayName("realizarPedido cuando se hace una petición de una reserva sin disponibilidad devuelve BAD REQUEST")
    void realizarPedido_solicitar_pedido_almacen_KO() {

        assertThrows(WebClientResponseException.BadRequest.class, () -> almacenClient
                .solicitarReservaMedicamento(ID_MED_NO_DISPONIBLE, UNIDADES));

    }

}