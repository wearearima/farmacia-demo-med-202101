package eu.arima.mejorarTesting.farmacia.reservas;

import eu.arima.mejorarTesting.farmacia.medicamentos.Medicamento;
import eu.arima.mejorarTesting.farmacia.medicamentos.MedicamentosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservasServiceTest {

    public static final long ID_MED = 1L;
    public static final int UNIDADES = 5;
    public static final long ID_RES = 55L;


    @Mock
    private ReservasRepository reservasRepository;
    @Mock
    private MedicamentosRepository medicamentosRepository;
    @Mock
    private PedidosAlmacenService pedidosAlmacenService;

    private ReservasService reservasService;

    private Medicamento medicamento;

    @BeforeEach
    void setUp() {
        reservasService = spy(new ReservasService(reservasRepository, medicamentosRepository, pedidosAlmacenService));

        medicamento = new Medicamento();
        when(medicamentosRepository.findById(ID_MED)).thenReturn(Optional.of(medicamento));
    }

    @Nested
    @DisplayName("reservarMedicamento si en la farmacia hay stock suficiente del medicamento")
    class SiHayStock {

        public static final int STOCK = 10;
        private Reserva reserva;

        @BeforeEach
        void setUp() {
            medicamento.setUnidadesStock(STOCK);

            reserva = new Reserva(ID_MED, UNIDADES);
            when(reservasRepository
                    .save(argThat(r -> r.getIdMedicamento() == ID_MED && r.getUnidades() == UNIDADES)))
                    .thenReturn(reserva);
        }

        @Test
        @DisplayName("se genera una reserva con el total de las unidades solicitadas")
        void reserva_con_todas_unidades() {
            reservasService.reservarMedicamento(ID_MED, UNIDADES);

            verify(reservasRepository)
                    .save(argThat(r -> r.getIdMedicamento() == ID_MED && r.getUnidades() == UNIDADES));
        }

        @Test
        @DisplayName("se genera la información de recogida a partir de la reserva en la farmacia")
        void genera_informacion_recogida_reserva_farmacia() {
            reserva.setId(ID_RES);
            InfoRecogidaReserva resultado = reservasService.reservarMedicamento(ID_MED, UNIDADES);

            assertEquals(new InfoRecogidaReserva(Optional.of(ID_RES), UNIDADES, 0), resultado);
        }

        @Test
        @DisplayName("se actualiza la cantidad de stock del medicamento")
        void actualiza_stock_medicamento() {
            reservasService.reservarMedicamento(ID_MED, UNIDADES);
            assertAll("Actualización deseada del medicamento",
                    () -> assertEquals(STOCK - UNIDADES, medicamento.getUnidadesStock()),
                    () -> verify(medicamentosRepository).save(medicamento));
        }

        @Test
        @DisplayName("no se hacen pedidos al almacen")
        void no_se_hacen_pedidos_al_almacen(){
            reservasService.reservarMedicamento(ID_MED, UNIDADES);

            verify(pedidosAlmacenService, never()).realizarPedido(anyLong(), anyInt());
        }

    }

    @Nested
    @DisplayName("reservarMedicamento si en la farmacia no hay stock suficiente del medicamento")
    class SiNoHayStockSuficiente {


        @Nested
        @DisplayName("y hay unidades en stock")
        class HayUnidadesStock {

            public static final int STOCK = 2;
            private Reserva reserva;


            @BeforeEach
            void setUp() {
                medicamento.setUnidadesStock(STOCK);

                reserva = new Reserva(ID_MED, STOCK);
                reserva.setId(ID_RES);
                when(reservasRepository
                        .save(argThat(r -> r.getIdMedicamento() == ID_MED && r.getUnidades() == STOCK)))
                        .thenReturn(reserva);
            }

            @Test
            @DisplayName("se genera una reserva con las unidades disponibles")
            void reserva_con_unidades_disponibles() {
                reservasService.reservarMedicamento(ID_MED, UNIDADES);

                verify(reservasRepository)
                        .save(argThat(r -> r.getIdMedicamento() == ID_MED && r.getUnidades() == STOCK));
            }

            @DisplayName("se genera la información de recogida a partir de la reserva")
            void genera_informacion_recogida_reserva_farmacia_y_almacen() {
                InfoRecogidaReserva resultado = reservasService.reservarMedicamento(ID_MED, UNIDADES);
                assertEquals(new InfoRecogidaReserva(Optional
                        .of(ID_RES), STOCK, UNIDADES - STOCK), resultado);
            }

            @Test
            @DisplayName("se actualiza el stock del medicamento a 0")
            void actualiza_stock_medicamento() {
                reservasService.reservarMedicamento(ID_MED, UNIDADES);
                assertAll("Actualización deseada del medicamento",
                        () -> assertEquals(0, medicamento.getUnidadesStock()),
                        () -> verify(medicamentosRepository).save(medicamento));
            }

            @Test
            @DisplayName("se hace un pedido al almacen con las unidades restantes mas un 50%")
            void reserva_almacen_con_unidades_restantes() {
                reservasService.reservarMedicamento(ID_MED, UNIDADES);

                int unidadesAlmacen = (int)((UNIDADES - STOCK) * 1.5);
                verify(pedidosAlmacenService).realizarPedido(ID_MED, unidadesAlmacen);
            }

        }

        @Nested
        @DisplayName("y no hay nada de stock")
        class NoHayStock {
            public static final int STOCK_MEDICAMENTO = 0;

            @BeforeEach
            void setUp() {
                medicamento.setUnidadesStock(STOCK_MEDICAMENTO);
            }

            @Test
            @DisplayName("no se generan reservas")
            void reserva_sin_stock_no_reserva() {
                reservasService.reservarMedicamento(ID_MED, UNIDADES);

                verify(reservasRepository, never()).save(any(Reserva.class));
            }

            @Test
            @DisplayName("la informacion de recogida refleja que todas las unidades están pendientes")
            void reserva_sin_stock_devuelve_empty() {
                InfoRecogidaReserva resultado = reservasService.reservarMedicamento(ID_MED, UNIDADES);

                assertEquals(new InfoRecogidaReserva(Optional.empty(),0,UNIDADES), resultado);
            }

            @Test
            @DisplayName("se hace un pedido al almacen con las unidades solicitadas mas un 50%")
            void reserva_almacen_con_unidades_restantes() {
                reservasService.reservarMedicamento(ID_MED, UNIDADES);

                int unidadesAlmacen = (int)(UNIDADES * 1.5);
                verify(pedidosAlmacenService).realizarPedido(ID_MED, unidadesAlmacen);
            }
        }

    }

}