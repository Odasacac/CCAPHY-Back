package CCASolutions.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import CCASolutions.modelos.ModeloEmpleados;
import CCASolutions.respuestas.RespuestaEmpleados;
import CCASolutions.servicios.IServiciosEmpleados;
import CCASolutions.respuestas.RespuestaEmpleadoLogueado;


@CrossOrigin (origins= {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class ControladorEmpleados 
{
	@Autowired
	private IServiciosEmpleados serviciosEmpleados;
	
	@PostMapping("/empleados")
	public ResponseEntity<RespuestaEmpleados> guardarNuevoEmpleado (@RequestBody ModeloEmpleados empleado)
	{
		return serviciosEmpleados.guardarNuevoEmpleado(empleado);
	}
	
	@PostMapping("/empleados/login")
	public ResponseEntity<RespuestaEmpleadoLogueado> hacerLogin (@RequestBody ModeloEmpleados empleado)
	{
		return serviciosEmpleados.hacerLogin(empleado);
	}
	
	@PutMapping ("/empleados/restableceradm")
	public ResponseEntity<RespuestaEmpleados> restablecerContrasenyaPorAdmin (@RequestBody ModeloEmpleados empleado)
	{
		return serviciosEmpleados.restablecerContrasenya(empleado);
	}
	
	@PutMapping ("/empleados/restableceremp")
	public ResponseEntity<RespuestaEmpleados> restablecerContrasenyaPorEmple (@RequestBody ModeloEmpleados empleado)
	{
		return serviciosEmpleados.restablecerContrasenyaPorEmpleado(empleado);
	}
	
}
