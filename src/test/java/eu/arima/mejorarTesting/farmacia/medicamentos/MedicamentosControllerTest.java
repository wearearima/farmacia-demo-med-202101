package eu.arima.mejorarTesting.farmacia.medicamentos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MedicamentosController.class)
class MedicamentosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicamentosService medicamentosService;

    @Test
    @DisplayName("/medicamento/idMedicamento (get) devuelve el medicamento recuperado de la BD")
    void get_medicamento_devuelve_medicamento_solicitado() throws Exception {
        Medicamento medicamento = new Medicamento();
        medicamento.setId(1L);
        medicamento.setCodigo("IBU-600");
        medicamento.setFechaCaducidad(LocalDate.of(2021, 1, 21));
        medicamento.setUnidadesStock(23);

        when(medicamentosService.getMedicamento(1L)).thenReturn(medicamento);

        ResultActions resultado = mockMvc
                .perform(MockMvcRequestBuilders.get("/medicamentos/1"));


        assertAll("La respuesta a la petición tiene la información del medicamento",
                () -> resultado.andExpect(status().isOk()),
                () -> resultado.andExpect(jsonPath("$.id").value(medicamento.getId())),
                () -> resultado.andExpect(jsonPath("$.codigo").value(medicamento.getCodigo())),
                () -> resultado.andExpect(jsonPath("$.fechaCaducidad")
                        .value(medicamento.getFechaCaducidad().toString())),
                () -> resultado.andExpect(jsonPath("$.unidadesStock").value(medicamento.getUnidadesStock()))
        );
    }
}