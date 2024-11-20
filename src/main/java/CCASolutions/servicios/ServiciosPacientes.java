package CCASolutions.servicios;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import CCASolutions.dao.IPacientesDao;
import CCASolutions.enums.EnumEstados;
import CCASolutions.modelos.ModeloEmpleados;
import CCASolutions.modelos.ModeloMensajes;
import CCASolutions.modelos.ModeloPacientes;
import CCASolutions.others.IFuncionesUtiles;
import CCASolutions.respuestas.RespuestaPacientes;

@Service
public class ServiciosPacientes implements IServiciosPacientes
{
	
	@Autowired
	private IFuncionesUtiles funcionesUtiles;
	
	@Autowired
	private IPacientesDao pacientesDao;

	@Autowired
	private IServiciosMensajes serviciosMensajes;

	
	
	
	
	@Override
	@Transactional
	public ResponseEntity<RespuestaPacientes> activarDesactivarPaciente(ModeloPacientes paciente) 
	{
		RespuestaPacientes respuesta = new RespuestaPacientes();
		
		try
		{
			Optional <ModeloPacientes> optionalPacienteBD = pacientesDao.findById(paciente.getId());
			
			if (optionalPacienteBD.isPresent())
			{
				ModeloPacientes pacienteBD=optionalPacienteBD.get();
				
				if (pacienteBD.getEstado()==EnumEstados.ACTIVO)
				{
					pacienteBD.setEstado(EnumEstados.INACTIVO);
					pacientesDao.save(pacienteBD);
				}
				else if (pacienteBD.getEstado()==EnumEstados.INACTIVO)
				{
					pacienteBD.setEstado(EnumEstados.ACTIVO);
					pacientesDao.save(pacienteBD);
				}
				
				respuesta.setRespuesta("Paciente " + pacienteBD.getNombre() + " ahora está " + pacienteBD.getEstado().toString().toLowerCase() + ".");
			}
			else
			{
				respuesta.setRespuesta("El empleado no existe");
				respuesta.setPacientes(null);
				return new ResponseEntity<RespuestaPacientes>(respuesta, HttpStatus.BAD_REQUEST);
			}
			
			
		}
		catch(Exception e)
		{
			respuesta.setRespuesta("Error activar o desactivar el empleado: " + e);
			respuesta.setPacientes(null);
			return new ResponseEntity<RespuestaPacientes>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<RespuestaPacientes> (respuesta, HttpStatus.OK);
	}
	
	
	
	
	
	@Override
	public ResponseEntity<RespuestaPacientes> guardarNuevoPaciente(ModeloPacientes paciente) 
	{
		RespuestaPacientes respuesta = new RespuestaPacientes();
		
		paciente.setNombre(funcionesUtiles.capitalizer(paciente.getNombre()));
		paciente.setPrimerApellido(funcionesUtiles.capitalizer(paciente.getPrimerApellido()));
		paciente.setSegundoApellido(funcionesUtiles.capitalizer(paciente.getSegundoApellido()));
		paciente.setCorreo(paciente.getCorreo().toUpperCase());
		paciente.setDireccion(funcionesUtiles.capitalizer(paciente.getDireccion()));
		paciente.setEdad(funcionesUtiles.obtenerEdadPorFechaNacimiento(paciente.getFechaNacimiento()));
		paciente.setEstado(EnumEstados.valueOf("ACTIVO"));
		paciente.setFechaAlta(LocalDateTime.now());
		paciente.setMotivoConsulta(funcionesUtiles.capitalizer(paciente.getMotivoConsulta()));
	
		
		if (paciente.getResponsable() != null)
		{
			try
			{		
				if (funcionesUtiles.comprobarPacienteExistePorNombreCompleto(paciente))
				{
					respuesta.setRespuesta("Paciente ya existente.");
					respuesta.setPacientes(null);
					return new ResponseEntity<RespuestaPacientes>(respuesta, HttpStatus.BAD_REQUEST);
				}
				else
				{
					ModeloPacientes pacienteGuardado = pacientesDao.save(paciente);
				
					if (pacienteGuardado != null)
					{
						respuesta.setRespuesta("Paciente guardado correctamente");
						respuesta.setPacientes(null);
						
						ModeloMensajes mensajeNuevoPaciente = new ModeloMensajes();
						mensajeNuevoPaciente.setAsunto("Nuevo paciente a su cargo");
						
						String contenido = "Hola,"
										+ "\n\nLe informamos que tiene un nuevo paciente a su cargo."
										+ "\n\nPuede consultar más detalles en la sección Mis empleados del portal."
										+ "\n\nPara cualquier duda puede escribir a este correo."
										+ "\n\nGracias.";
									
						
						mensajeNuevoPaciente.setContenido(contenido);
						
						ModeloEmpleados emisorMensaje = new ModeloEmpleados();
						
						emisorMensaje.setId(1L);
						
						mensajeNuevoPaciente.setEmisor(emisorMensaje);
						
						mensajeNuevoPaciente.setReceptor(pacienteGuardado.getResponsable());
						
						serviciosMensajes.guardarNuevoMensaje(mensajeNuevoPaciente);
						
					}
					else
					{
						respuesta.setRespuesta("Error al almacenar.");
						respuesta.setPacientes(null);
						return new ResponseEntity<RespuestaPacientes>(respuesta, HttpStatus.BAD_REQUEST);
					}
				}
				
			}
			catch (Exception e)
			{
				respuesta.setRespuesta("Error al almacenar: " + e);
				respuesta.setPacientes(null);
				return new ResponseEntity<RespuestaPacientes>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		}
		
		else
		{
			respuesta.setRespuesta("Paciente sin responsable asignado.");
			respuesta.setPacientes(null);
			return new ResponseEntity<RespuestaPacientes>(respuesta, HttpStatus.BAD_REQUEST);
		}
		
		
		return new ResponseEntity<RespuestaPacientes> (respuesta, HttpStatus.OK);
	}

}
