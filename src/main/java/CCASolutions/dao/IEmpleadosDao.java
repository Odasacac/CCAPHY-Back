package CCASolutions.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import CCASolutions.modelos.ModeloEmpleados;

public interface IEmpleadosDao extends CrudRepository<ModeloEmpleados, Long>
{
	@Query("SELECT e.codigoEmpleado FROM ModeloEmpleados e")
    List<String> findAllCodigoUsuario();
	
	@Query("SELECT e.correo FROM ModeloEmpleados e")
    List<String> findAllCorreos();
	
	@Query("SELECT e.contrasenya FROM ModeloEmpleados e WHERE e.correo = :correo")
	String getContrasenyaDeEmpleadoPorCorreo (@Param ("correo") String correo);
	
	@Query("SELECT e.contrasenya FROM ModeloEmpleados e WHERE e.codigoEmpleado = :codigo")
	String getContrasenyaDeEmpleadoPorCodigo (@Param ("codigo") String codigo);
	
	@Query ("SELECT e FROM ModeloEmpleados e Where e.correo = :correo")
	ModeloEmpleados getEmpleadoPorCorreo (@Param ("correo") String correo);
	
	@Query ("SELECT e FROM ModeloEmpleados e Where e.codigoEmpleado = :codigo")
	ModeloEmpleados getEmpleadoPorCodigo (@Param ("codigo") String codigo);
	
	 boolean existsByNombreAndPrimerApellidoAndSegundoApellido(String nombre, String primerApellido, String segundoApellido);
}
