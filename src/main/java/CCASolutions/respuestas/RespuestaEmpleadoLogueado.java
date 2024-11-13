package CCASolutions.respuestas;

import CCASolutions.dtos.DTOEmpleadoLogueado;
import lombok.Data;

@Data
public class RespuestaEmpleadoLogueado 
{
	private DTOEmpleadoLogueado empleadoLogueado;
	private String respuesta;
	private String jwt;
}
