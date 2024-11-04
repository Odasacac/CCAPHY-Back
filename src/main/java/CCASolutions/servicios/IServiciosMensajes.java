package CCASolutions.servicios;

import org.springframework.http.ResponseEntity;

import CCASolutions.modelos.ModeloEmpleados;
import CCASolutions.modelos.ModeloMensajes;
import CCASolutions.respuestas.RespuestaMensajes;

public interface IServiciosMensajes 
{
	public ResponseEntity<RespuestaMensajes> guardarNuevoMensaje (ModeloMensajes mensaje);
	
	public ResponseEntity<RespuestaMensajes> mensajeParaRestablecerContrasenya (ModeloEmpleados empleado);

}
