package eu.arima.mejorarTesting.farmacia.medicamentos;

import org.springframework.stereotype.Service;

@Service
public class MedicamentosService {

    private final MedicamentosRepository medicamentosRepository;

    public MedicamentosService(MedicamentosRepository medicamentosRepository) {
        this.medicamentosRepository = medicamentosRepository;
    }

    public Medicamento getMedicamento(long idMedicamento) throws MedicamentoCaducadoException {
        Medicamento medicamento = medicamentosRepository.findById(idMedicamento).orElseThrow();
        if (medicamento.estaCaducado()){
            throw new MedicamentoCaducadoException();
        }
        return medicamento;
    }

    public void actualizarStock(long idMedicamento, int unidadesStock) {
        Medicamento medicamento = medicamentosRepository.findById(idMedicamento).orElseThrow();
        medicamento.setUnidadesStock(unidadesStock);
        medicamentosRepository.save(medicamento);
    }
}
