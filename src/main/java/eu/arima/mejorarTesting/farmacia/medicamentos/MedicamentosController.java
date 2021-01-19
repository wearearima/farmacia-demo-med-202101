package eu.arima.mejorarTesting.farmacia.medicamentos;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @ExceptionHandler(MedicamentoCaducadoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    void handleExceptions(){

    }
}
