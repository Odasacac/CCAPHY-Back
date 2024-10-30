package CCASolutions.respuestas;

import CCASolutions.modelos.ModeloEmpleadoLogueado;
import lombok.Data;

@Data
public class RespuestaEmpleadoLogueado 
{
	private ModeloEmpleadoLogueado empleadoLogueado;
	private String respuesta;
	private String jwt;
}
