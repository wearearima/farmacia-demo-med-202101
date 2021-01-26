package eu.arima.mejorarTesting.farmacia.reservas;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.matchers.MatchType;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.JsonBody.json;

@Transactional
@SpringBootTest
@Testcontainers
class PedidosAlmacenServiceTest {

    @Container
    public static final MockServerContainer mockServerContainer = new MockServerContainer(DockerImageName
            .parse("jamesdbloom/mockserver:mockserver-5.10.0"));
    @Container
    private final static PostgreSQLContainer postgresContainer = new PostgreSQLContainer(DockerImageName.parse(
            "postgres:13"));

    PedidosAlmacenService pedidosAlmacenService;

    @Autowired
    PedidosAlmacenRepository pedidosAlmacenRepository;

    @Autowired
    AlmacenClient almacenClient;

    MockServerClient client;

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("almacen-server.url",mockServerContainer::getEndpoint);
    }

    @BeforeEach
    void setUp() {
        pedidosAlmacenService = new PedidosAlmacenService(pedidosAlmacenRepository, almacenClient);

        client = new MockServerClient(mockServerContainer.getHost(), mockServerContainer.getServerPort());
    }

    @Test
    @DisplayName("realizarPedido guarda y devuelve la información del pedido creado en el almacen")
    void guarda_informacion_pedido() {
        long numPedidosAntes = pedidosAlmacenRepository.count();

        long idReservaAlmacen = 95L;
        HttpResponse responseReservaOK = response().withStatusCode(HttpStatus.CREATED.value())
                                                   .withContentType(MediaType.APPLICATION_JSON)
                                                   .withBody(String.valueOf(idReservaAlmacen));
        client.when(crearRequestReserva(45L, 66)).respond(responseReservaOK);

        Optional<PedidoAlmacen> resultado = pedidosAlmacenService.realizarPedido(45L, 66);

        assertAll("Se ha guardado correctamente la petición al almacen en bd",
                () -> assertEquals(numPedidosAntes + 1, pedidosAlmacenRepository.count()),
                () -> assertEquals(95L, resultado.get().getIdReservaAlmacen()),
                () -> assertEquals(45, resultado.get().getIdMedicamento()),
                () -> assertEquals(66, resultado.get().getUnidadesPedidas()),
                () -> assertEquals(resultado.get(), pedidosAlmacenRepository.findByIdReservaAlmacen(idReservaAlmacen)));


    }

    @Test
    @DisplayName("realizarPedido si el pedido no se realiza correctamente en el almacen no se guarda nada")
    void pedido_no_realizado_correctamente_no_hace_nada() {
        long numPedidosAntes = pedidosAlmacenRepository.count();
        HttpResponse responseReservaKO = response().withStatusCode(HttpStatus.BAD_REQUEST.value())
                                                   .withContentType(MediaType.APPLICATION_JSON);
        client.when(crearRequestReserva(123L, 900)).respond(responseReservaKO);

        Optional<PedidoAlmacen> resultado = pedidosAlmacenService.realizarPedido(123L, 900);

        assertFalse(resultado.isPresent());
        assertEquals(numPedidosAntes, pedidosAlmacenRepository.count());
    }

    private HttpRequest crearRequestReserva(long idMedicamento, int unidades) {
        return request()
                .withMethod("POST")
                .withHeader(HttpHeaders.CONTENT_TYPE, org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .withPath("/reservas")
                .withBody(json("{" +
                               "\"idFarmacia\": 44, " +
                               "\"idMedicamento\": " + idMedicamento + ", " +
                               "\"unidades\": " + unidades +
                               "}",
                        MatchType.STRICT
                ));
    }
}