package CCASolutions.servicios;

import org.springframework.http.ResponseEntity;

import CCASolutions.modelos.ModeloEmpleados;
import CCASolutions.respuestas.RespuestaEmpleadoLogueado;
import CCASolutions.respuestas.RespuestaEmpleados;

public interface IServiciosEmpleados 
{
	public ResponseEntity<RespuestaEmpleados> guardarNuevoEmpleado (ModeloEmpleados empleado);
	public ResponseEntity<RespuestaEmpleadoLogueado> hacerLogin (ModeloEmpleados empleado);
	public ResponseEntity<RespuestaEmpleados> restablecerContrasenya (ModeloEmpleados empleado);
}
