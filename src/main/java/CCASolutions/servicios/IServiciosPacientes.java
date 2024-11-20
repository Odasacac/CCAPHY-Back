package CCASolutions.servicios;

import org.springframework.http.ResponseEntity;


import CCASolutions.modelos.ModeloPacientes;
import CCASolutions.respuestas.RespuestaPacientes;

public interface IServiciosPacientes 
{
	public ResponseEntity<RespuestaPacientes> guardarNuevoPaciente (ModeloPacientes paciente);
	
	public ResponseEntity<RespuestaPacientes> activarDesactivarPaciente(ModeloPacientes paciente);
}
