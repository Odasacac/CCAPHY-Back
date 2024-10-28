package CCASolutions.dao;

import org.springframework.data.repository.CrudRepository;

import CCASolutions.modelos.ModeloNotasPaciente;

public interface INotasPacienteDao extends CrudRepository<ModeloNotasPaciente, Long>
{

}
