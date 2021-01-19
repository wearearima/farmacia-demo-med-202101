package eu.arima.mejorarTesting.farmacia.medicamentos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.transaction.Transactional;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Testcontainers
class MedicamentosServiceIntegrationTest {

    @Container
    private final static PostgreSQLContainer postgresContainer = new PostgreSQLContainer(DockerImageName.parse("postgres:13"));

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Autowired
    MedicamentosService medicamentosService;
    @Autowired
    MedicamentosRepository medicamentosRepository;

    @Test
    @DisplayName("actualizarStock modifica en BD las unidades disponibles del medicamento")
    void actualizarStock_actualiza_unidades_medicamento() {
       assertEquals(50, medicamentosRepository.findById(1L).orElseThrow().getUnidadesStock());

       medicamentosService.actualizarStock(1L, 150);

       assertEquals(150, medicamentosRepository.findById(1L).orElseThrow().getUnidadesStock());
    }

    @Test
    @DisplayName("")
    void actualizarStock_lanza_excepcion_si_medicamento_no_existe() {
        NoSuchElementException infoException = assertThrows(NoSuchElementException.class, ()-> medicamentosService.actualizarStock(10L, 25));
        assertEquals("No value present", infoException.getMessage());
    }
}