package CCASolutions.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import CCASolutions.modelos.ModeloPacientes;
import CCASolutions.respuestas.RespuestaPacientes;
import CCASolutions.servicios.IServiciosPacientes;

@CrossOrigin (origins= {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class ControladorPacientes 
{
	@Autowired
	private IServiciosPacientes serviciosPacientes;
	
	@PostMapping("/pacientes")
	public ResponseEntity<RespuestaPacientes> guardarNuevoPaciente(@RequestBody ModeloPacientes paciente)
	{
		return serviciosPacientes.guardarNuevoPaciente(paciente);
	}
	
	@PutMapping ("/pacientes/activdesac")
	public ResponseEntity<RespuestaPacientes> activarDesactivarPaciente (@RequestBody ModeloPacientes paciente)
	{
		return serviciosPacientes.activarDesactivarPaciente(paciente);
	}

}
