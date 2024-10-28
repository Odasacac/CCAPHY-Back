package CCASolutions.dao;

import org.springframework.data.repository.CrudRepository;

import CCASolutions.modelos.ModeloEmpleados;

public interface IEmpleadosDao extends CrudRepository<ModeloEmpleados, Long>
{

}
