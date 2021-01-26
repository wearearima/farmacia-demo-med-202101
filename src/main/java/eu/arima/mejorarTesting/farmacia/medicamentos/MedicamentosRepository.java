package eu.arima.mejorarTesting.farmacia.medicamentos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicamentosRepository extends CrudRepository<Medicamento, Long> {
}
