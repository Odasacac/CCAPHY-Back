package CCASolutions.dao;

import org.springframework.data.repository.CrudRepository;

import CCASolutions.modelos.ModeloPacientes;

public interface IPacientesDao extends CrudRepository<ModeloPacientes, Long>
{
	boolean existsByResponsableId(Long responsableId);
}
