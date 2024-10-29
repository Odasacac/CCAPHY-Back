package CCASolutions.respuestas;

import java.util.List;

import CCASolutions.modelos.ModeloEmpleados;
import lombok.Data;

@Data
public class RespuestaEmpleados 
{
	private String respuesta;
	private List <ModeloEmpleados> empleados;
}
