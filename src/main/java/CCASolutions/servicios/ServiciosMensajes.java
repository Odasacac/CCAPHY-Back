package CCASolutions.servicios;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import CCASolutions.dao.IEmpleadosDao;
import CCASolutions.dao.IMensajesDao;
import CCASolutions.modelos.ModeloEmpleados;
import CCASolutions.modelos.ModeloMensajes;
import CCASolutions.others.IFuncionesUtiles;
import CCASolutions.respuestas.RespuestaMensajes;

@Service
public class ServiciosMensajes implements IServiciosMensajes
{
	@Autowired
	private IFuncionesUtiles funcionesUtiles;
	
	@Autowired
	private IMensajesDao mensajesDao;
	
	@Autowired
	private IEmpleadosDao empleadosDao;
	

	
	
	@Override
	public ResponseEntity<RespuestaMensajes> mensajeParaRestablecerContrasenya(ModeloEmpleados empleado) 
	{
		ModeloMensajes mensajeParaAdmin = new ModeloMensajes();
		
		try
		{
			ModeloEmpleados empleadoBD = empleadosDao.getEmpleadoPorCodigo(empleado.getCodigoEmpleado());
			
			if (empleadoBD != null)
			{
				String contenido = "";
				
				if (!funcionesUtiles.verificarContrasenya(empleado.getContrasenya(), empleadoBD.getContrasenya()))
				{
					contenido = "Hola, "
							+ "\n\nEl empleado con código: " + empleado.getCodigoEmpleado().toUpperCase()
							+ "\nHa solicitado restablecer su contraseña actual por la siguiente: "
							+ "\n" + funcionesUtiles.encriptarContrasenya(empleado.getContrasenya())
							+ "\n\nPara cualquier duda puede escribir a este correo."
							+ "\n\nGracias.";
				}
				else
				{
					contenido = "Hola, "
							+ "\n\nEl empleado con código: " + empleado.getCodigoEmpleado().toUpperCase()
							+ "\nHa solicitado restablecer su contraseña actual."
							+ "\nSin embargo, ha ingresado la contraseña que está en uso ahora."
							+ "\n\nPor favor, póngase en contacto con dicho empleado."
							+ "\n\nGracias.";
				}
				mensajeParaAdmin.setAsunto("Solicitud de cambio de contraseña");
				
				mensajeParaAdmin.setContenido(contenido);
				
				ModeloEmpleados emisorYReceptorMensaje = new ModeloEmpleados();
				
				emisorYReceptorMensaje.setId(1L);
				
				mensajeParaAdmin.setEmisor(emisorYReceptorMensaje);
				
				mensajeParaAdmin.setReceptor(emisorYReceptorMensaje);

				return guardarNuevoMensaje(mensajeParaAdmin);
			}
			
		}
		catch (Exception e)
		{
			return new ResponseEntity<RespuestaMensajes> (HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return null;
	}
	

	@Override
	public ResponseEntity<RespuestaMensajes> guardarNuevoMensaje(ModeloMensajes mensaje) 
	{
		RespuestaMensajes respuesta = new RespuestaMensajes ();
		
		mensaje.setAsunto(funcionesUtiles.capitalizer(mensaje.getAsunto()));
		mensaje.setContenido(funcionesUtiles.capitalizer(mensaje.getContenido()));
		mensaje.setFechaEnvio(LocalDateTime.now());
		
		try
		{
			ModeloMensajes mensajeGuardado = mensajesDao.save(mensaje);
		
		if (mensajeGuardado != null)
		{
			List <ModeloMensajes> mensajes = new ArrayList<>();
			mensajes.add(mensajeGuardado);
			
			respuesta.setRespuesta("Mensaje enviado correctamente.");
			respuesta.setMensajes(null);

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
	

	
	
}
