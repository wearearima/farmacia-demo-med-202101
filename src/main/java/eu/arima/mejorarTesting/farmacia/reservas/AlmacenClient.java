package eu.arima.mejorarTesting.farmacia.reservas;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class AlmacenClient {

    //TODO debería ser un parámetro
    public static final Long ID_FARMACIA = 44L;
    public static final String RESERVAR_MEDICAMENTO_PATH = "/reservas";


    private final WebClient almacenWebClient;

    public AlmacenClient(WebClient almacenWebClient) {
        this.almacenWebClient = almacenWebClient;
    }


    Long solicitarReservaMedicamento(Long idMedicamento, Integer unidades) {
        Map<String, Object> data = new HashMap<>() {{
            put("idFarmacia", ID_FARMACIA);
            put("idMedicamento", idMedicamento);
            put("unidadesReservar", unidades);
        }};
        return almacenWebClient.post().uri(RESERVAR_MEDICAMENTO_PATH)
                                                                      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                                                      .body(BodyInserters.fromValue(data)).retrieve()
                                                                      .bodyToMono(Long.class).block();
    }
}
