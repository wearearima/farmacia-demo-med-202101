package eu.arima.mejorarTesting.farmacia.medicamentos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;

import java.time.LocalDate;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

class MedicamentoTest {

    public static final LocalDate HOY = LocalDate.now();

    @ParameterizedTest( name = "El día {0} el medicamento no está caducado")
    @DisplayName("Si la fecha de caducidad del medicamento es posterior a hoy, el medicamento no está caducado")
    @MethodSource("providerFechasPosteriores")
    void estaCaducado_si_fechaCaducidad_posterior_hoy_false(LocalDate fechaPosterior) {
        Medicamento medicamento = new Medicamento();
        medicamento.setFechaCaducidad(fechaPosterior);
        assertFalse(medicamento.estaCaducado());
    }

    static Stream<LocalDate> providerFechasPosteriores() {
        IntStream diasSumar = new Random().ints(10, 1, 3650);
        return diasSumar.mapToObj(HOY::plusDays);
    }

    @ParameterizedTest(name = "El día {0} el medicamento está caducado")
    @DisplayName("Si la fecha de caducidad del medicamento es anterior a hoy, el medicamento está caducado")
    @MethodSource("providerFechasAnteriores")
    void estaCaducado_si_fechaCaducidad_anterior_hoy_true(LocalDate fechaAnterior) {
        Medicamento medicamento = new Medicamento();
        medicamento.setFechaCaducidad(fechaAnterior);
        assertTrue(medicamento.estaCaducado());
    }

    static Stream<LocalDate> providerFechasAnteriores() {
        IntStream diasRestar = new Random().ints(10, 1, 3650);
        return diasRestar.mapToObj(HOY::minusDays);
    }

    @Test
    @DisplayName("Si la fecha de caducidad del medicamento es hoy, el medicamento no está caducado")
    void estaCaducado_si_fechaCaducidad_hoy_false() {
        try (MockedStatic<LocalDate> localDateMockStatic = mockStatic(LocalDate.class)) {
            LocalDate hoy = LocalDate.of(2021,1,21);
            localDateMockStatic.when(LocalDate::now).thenReturn(hoy);
            Medicamento medicamento = new Medicamento();
            medicamento.setFechaCaducidad(hoy);
            assertFalse(medicamento.estaCaducado());
        }
    }

    @Test
    @DisplayName("Si no tiene fecha de caducidad, el medicamento no está caducado")
    void estaCaducado_si_fechaCaducidad_no_hay_false() {
        Medicamento medicamento = new Medicamento();
        assertFalse(medicamento.estaCaducado());
    }
}