package CCASolutions.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import CCASolutions.modelos.ModeloMensajes;
import CCASolutions.respuestas.RespuestaMensajes;
import CCASolutions.servicios.IServiciosMensajes;


@CrossOrigin (origins= {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class ControladorMensajes 
{
	@Autowired
	private IServiciosMensajes serviciosMensajes;
	
	@PostMapping("/mensajes")
	public ResponseEntity<RespuestaMensajes> guardarNuevoMensaje (@RequestBody ModeloMensajes mensaje)
	{
		return serviciosMensajes.guardarNuevoMensaje(mensaje);
	}

}
