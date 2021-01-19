package eu.arima.mejorarTesting.farmacia.medicamentos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class MedicamentosServiceTest {

    public static final long ID_MEDICAMENTO = 1L;
    private MedicamentosService medicamentosService;

    @Mock
    private Medicamento medicamentoBD;
    @Mock
    private MedicamentosRepository medicamentosRepository;

    @BeforeEach
    void setUp() {
        medicamentosService = new MedicamentosService(medicamentosRepository);
        when(medicamentosRepository.findById(ID_MEDICAMENTO)).thenReturn(Optional.of(medicamentoBD));
    }

    @Test
    @DisplayName("getMedicamento devuelve el medicamento recuperado de BD")
    void getMedicamento_devuelve_medicamento_BD() throws MedicamentoCaducadoException {
        Medicamento medicamento = medicamentosService.getMedicamento(ID_MEDICAMENTO);
        assertEquals(medicamentoBD, medicamento);
    }

    @Test
    @DisplayName("getMedicamento lanza MedicamentoCaducadoException si el medicamento está caducado")
    void getMedicamento_lanza_excepcion_si_medicamento_caducado() {
        when(medicamentoBD.estaCaducado()).thenReturn(true);
        assertThrows(MedicamentoCaducadoException.class, ()-> medicamentosService.getMedicamento(ID_MEDICAMENTO));
    }
}