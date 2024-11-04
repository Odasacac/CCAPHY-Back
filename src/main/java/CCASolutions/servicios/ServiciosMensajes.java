package CCASolutions.servicios;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import CCASolutions.dao.IMensajesDao;
import CCASolutions.modelos.ModeloMensajes;
import CCASolutions.respuestas.RespuestaMensajes;

@Service
public class ServiciosMensajes implements IServiciosMensajes
{

	@Autowired
	private IMensajesDao mensajesDao;

	@Override
	public ResponseEntity<RespuestaMensajes> guardarNuevoMensaje(ModeloMensajes mensaje) 
	{
		RespuestaMensajes respuesta = new RespuestaMensajes ();
		
		mensaje.setAsunto(capitalizer(mensaje.getAsunto()));
		mensaje.setContenido(capitalizer(mensaje.getContenido()));
		mensaje.setFechaEnvio(LocalDateTime.now());
		
		try
		{
			ModeloMensajes mensajeGuardado = mensajesDao.save(mensaje);
		
		if (mensajeGuardado != null)
		{
			List <ModeloMensajes> mensajes = new ArrayList<>();
			mensajes.add(mensajeGuardado);
			
			respuesta.setRespuesta("Mensaje enviado correctamente.");
			respuesta.setMensajes(mensajes);

		}
		else
		{
			respuesta.setRespuesta("Error al enviar.");
			respuesta.setMensajes(null);
			return new ResponseEntity<RespuestaMensajes>(respuesta, HttpStatus.BAD_REQUEST);
		}
			
		}
		catch (Exception e)
		{
			respuesta.setRespuesta("Error al guardar el mensaje");
			respuesta.setMensajes(null);
			return new ResponseEntity <RespuestaMensajes> (respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity <RespuestaMensajes> (respuesta, HttpStatus.OK);
	}
	
	private static String capitalizer (String cadena) 
	 {
		 if (cadena == null || cadena.isEmpty()) 
		 {
			 return cadena; 
		 }
		 return cadena.substring(0, 1).toUpperCase() + cadena.substring(1);
	 }
	
	
	
}
