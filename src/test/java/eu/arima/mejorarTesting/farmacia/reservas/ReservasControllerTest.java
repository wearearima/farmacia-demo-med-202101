package eu.arima.mejorarTesting.farmacia.reservas;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservasController.class)
class ReservasControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ReservasService reservasService;

    @MockBean
    PedidosAlmacenService pedidosAlmacenService;

    @Test
    @DisplayName("/reservas (post) con la información para una reserva realiza la reserva devuelviendo la información de recogida de la misma")
    void post_reservas_devuelve_informacion_recogida_reserva() throws Exception {
        InfoRecogidaReserva informacionRecogida = new InfoRecogidaReserva(Optional.of(5L) , 2, 0);
        when(reservasService.reservarMedicamento(1, 2)).thenReturn(informacionRecogida);

        ResultActions resultado = mockMvc.perform(post("/reservas")
                .contentType(APPLICATION_JSON)
                .content("{\"idMedicamento\": \"1\", \"unidades\": \"2\"}"));

        assertAll("La respuesta tiene la información de la recogida de la reserva",
                () -> resultado.andExpect(status().isOk()),
                () -> resultado.andExpect(jsonPath("$.idReserva").value(5L)),
                () -> resultado.andExpect(jsonPath("$.unidadesRecoger").value(2)),
                () -> resultado.andExpect(jsonPath("$.unidadesPendientes").value(0)));
    }
}