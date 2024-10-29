package CCASolutions.dao;

import org.springframework.data.repository.CrudRepository;

import CCASolutions.modelos.ModeloMensajes;

public interface IMensajesDao extends CrudRepository<ModeloMensajes, Long>
{

}
