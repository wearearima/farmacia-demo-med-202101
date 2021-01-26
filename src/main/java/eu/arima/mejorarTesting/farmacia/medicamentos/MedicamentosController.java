package eu.arima.mejorarTesting.farmacia.medicamentos;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/medicamentos")
public class MedicamentosController {

    private MedicamentosService medicamentosService;

    public MedicamentosController(MedicamentosService medicamentosService) {
        this.medicamentosService = medicamentosService;
    }

    @GetMapping("/{idMedicamento}")
    Medicamento getMedicamento(@PathVariable Long idMedicamento) throws MedicamentoCaducadoException {
       return medicamentosService.getMedicamento(idMedicamento);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    void actualizarStock(@RequestBody MedicamentoStockDTO medicamentoStockDTO){
        medicamentosService.actualizarStock(medicamentoStockDTO.getId(), medicamentoStockDTO.getUnidades());
    }

    @ExceptionHandler(value ={MedicamentoCaducadoException.class, NoSuchElementException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    void handleExceptions(){

    }
}
