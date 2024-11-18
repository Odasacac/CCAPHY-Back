package CCASolutions.servicios;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import CCASolutions.dao.IEmpleadosDao;
import CCASolutions.dao.IPacientesDao;
import CCASolutions.dtos.DTOCambiosResponsabilidad;
import CCASolutions.dtos.DTOEmpleadoLogueado;
import CCASolutions.enums.EnumEstados;
import CCASolutions.enums.EnumRoles;
import CCASolutions.modelos.ModeloEmpleados;
import CCASolutions.modelos.ModeloMensajes;
import CCASolutions.others.IFuncionesUtiles;
import CCASolutions.respuestas.RespuestaEmpleadoLogueado;
import CCASolutions.respuestas.RespuestaEmpleados;

@Service
public class ServiciosEmpleados implements IServiciosEmpleados
{
	@Autowired
	private IFuncionesUtiles funcionesUtiles;
	
	@Autowired
	private IEmpleadosDao empleadosDao;

	@Autowired
	private IServiciosMensajes serviciosMensajes;
	
	@Autowired
	private IPacientesDao pacientesDao;

	
	@Override
	public ResponseEntity<RespuestaEmpleados> cambiarResponsableDeEmpleado(DTOCambiosResponsabilidad ids) 
	{
		RespuestaEmpleados respuesta = new RespuestaEmpleados();
		
		try
		{
			Optional <ModeloEmpleados> nuevoEmpleadoOptional = empleadosDao.findById(ids.getResponsabilizadoId());
			
			if (nuevoEmpleadoOptional.isPresent())
			{
				ModeloEmpleados nuevoEmpleado = nuevoEmpleadoOptional.get();
				
				nuevoEmpleado.setResponsableId(ids.getNuevoResponsableId());
				
				empleadosDao.save(nuevoEmpleado);
				
				respuesta.setRespuesta("Responsable del empleado " + nuevoEmpleado.getCodigoEmpleado() + " cambiado con éxito.");
				respuesta.setEmpleados(null);
			}
			
			else
			{
				respuesta.setRespuesta("El empleado no existe");
				respuesta.setEmpleados(null);
				return new ResponseEntity<RespuestaEmpleados>(respuesta, HttpStatus.BAD_REQUEST);
			}
			
		
		}
		catch (Exception e)
		{
			respuesta.setRespuesta("Error al cambiar responsabilidades: " + e);
			respuesta.setEmpleados(null);
			return new ResponseEntity<RespuestaEmpleados>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<RespuestaEmpleados> (respuesta, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<RespuestaEmpleados> hacerDeshacerResponsable(ModeloEmpleados empleado) 
	{
		
		RespuestaEmpleados respuesta = new RespuestaEmpleados();
		
		try
		{
			ModeloEmpleados empleadoBD = empleadosDao.getEmpleadoPorCodigo(empleado.getCodigoEmpleado());
			
			if (empleadoBD != null)
			{
				
				if (empleadoBD.getRol()==EnumRoles.RESPONSABLE)
				{
					if (!pacientesDao.existsByResponsableId(empleadoBD.getId()))
					{
						empleadoBD.setRol(EnumRoles.EMPLEADO);
						empleadosDao.save(empleadoBD);
					}
					else
					{
						respuesta.setRespuesta("El responsable aun tiene personal a su cargo.");
						respuesta.setEmpleados(null);
						return new ResponseEntity<RespuestaEmpleados>(respuesta, HttpStatus.BAD_REQUEST);
					}
				}
				else if (empleadoBD.getRol()==EnumRoles.EMPLEADO)
				{
					empleadoBD.setRol(EnumRoles.RESPONSABLE);
					empleadosDao.save(empleadoBD);
				}
				
				respuesta.setRespuesta("Empleado " + empleado.getCodigoEmpleado() + " ahora es un " + empleadoBD.getRol().toString().toLowerCase() + ".");
			}
			else
			{
				respuesta.setRespuesta("El empleado no existe");
				respuesta.setEmpleados(null);
				return new ResponseEntity<RespuestaEmpleados>(respuesta, HttpStatus.BAD_REQUEST);
			}
			
			
		}
		catch(Exception e)
		{
			respuesta.setRespuesta("Error hacer o deshacer responsable: " + e);
			respuesta.setEmpleados(null);
			return new ResponseEntity<RespuestaEmpleados>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<RespuestaEmpleados> (respuesta, HttpStatus.OK);
	}


	@Override
	@Transactional
	public ResponseEntity<RespuestaEmpleados> activarDesactivarEmpleado(ModeloEmpleados empleado) 
	{
		RespuestaEmpleados respuesta = new RespuestaEmpleados();
		
		try
		{
			ModeloEmpleados empleadoBD = empleadosDao.getEmpleadoPorCodigo(empleado.getCodigoEmpleado());
			
			if (empleadoBD != null)
			{
				
				if (empleadoBD.getEstado()==EnumEstados.ACTIVO)
				{
					empleadoBD.setEstado(EnumEstados.INACTIVO);
					empleadosDao.save(empleadoBD);
				}
				else if (empleadoBD.getEstado()==EnumEstados.INACTIVO)
				{
					empleadoBD.setEstado(EnumEstados.ACTIVO);
					empleadosDao.save(empleadoBD);
				}
				
				respuesta.setRespuesta("Empleado " + empleado.getCodigoEmpleado() + " ahora está " + empleadoBD.getEstado().toString().toLowerCase() + ".");
			}
			else
			{
				respuesta.setRespuesta("El empleado no existe");
				respuesta.setEmpleados(null);
				return new ResponseEntity<RespuestaEmpleados>(respuesta, HttpStatus.BAD_REQUEST);
			}
			
			
		}
		catch(Exception e)
		{
			respuesta.setRespuesta("Error activar o desactivar el empleado: " + e);
			respuesta.setEmpleados(null);
			return new ResponseEntity<RespuestaEmpleados>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<RespuestaEmpleados> (respuesta, HttpStatus.OK);
	}
	
	
	
	@Override
	@Transactional
	public ResponseEntity<RespuestaEmpleados> restablecerContrasenya(ModeloEmpleados empleadoSolicitaContrasenya)
	{
		RespuestaEmpleados respuesta = new RespuestaEmpleados();
		try
		{
			ModeloEmpleados empleadoEncontrado = empleadosDao.getEmpleadoPorCodigo(empleadoSolicitaContrasenya.getCodigoEmpleado());
			
			if (empleadoEncontrado != null)
  			{
				if (empleadoEncontrado.getEstado() == EnumEstados.ACTIVO)
				{
					empleadoEncontrado.setContrasenya(empleadoSolicitaContrasenya.getContrasenya());
		  			empleadoEncontrado.setUltimaModificacionContrasenya(LocalDateTime.now());
		  			ModeloEmpleados empleadoActualizado = empleadosDao.save(empleadoEncontrado);
		  				
		  			
					respuesta.setRespuesta("Contraseña actualizada.");
					respuesta.setEmpleados(null);
							
					ModeloMensajes mensajeSignUp = new ModeloMensajes();
					mensajeSignUp.setAsunto("Cambio de contraseña");
							
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy 'a las' HH:mm:ss");
					String fechaHoraFormateada = LocalDateTime.now().format(formatter);
							
					String contenido = "Hola, " + empleadoActualizado.getNombre()
											+ "\n\nLe informamos que su contraseña ha sido actualizada el día "
											+ fechaHoraFormateada + "."
											+ "\n\nPara cualquier duda puede escribir a este correo."
											+ "\n\nGracias.";
										
							
					mensajeSignUp.setContenido(contenido);
						
					ModeloEmpleados emisorMensaje = new ModeloEmpleados();
							
					emisorMensaje.setId(1L);
							
					mensajeSignUp.setEmisor(emisorMensaje);
							
					mensajeSignUp.setReceptor(empleadoActualizado);
							
					serviciosMensajes.guardarNuevoMensaje(mensajeSignUp);				
				}
				else
				{
					respuesta.setRespuesta("Empleado inactivo.");
					respuesta.setEmpleados(null);
					return new ResponseEntity<RespuestaEmpleados>(respuesta, HttpStatus.BAD_REQUEST);
				}					
			}					
		}
		catch (Exception e)
		{
			respuesta.setRespuesta("Error al restablecer la contraseña: " + e);
			respuesta.setEmpleados(null);
			return new ResponseEntity<RespuestaEmpleados>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
		return new ResponseEntity<RespuestaEmpleados> (respuesta, HttpStatus.OK);
	}
	
	
	

	@Override
	@Transactional
	public ResponseEntity<RespuestaEmpleados> restablecerContrasenyaPorEmpleado(ModeloEmpleados empleadoSolicitaContrasenya) 
	{
		RespuestaEmpleados respuesta = new RespuestaEmpleados();
		try
		{
			ModeloEmpleados empleadoEncontrado = empleadosDao.getEmpleadoPorCodigo(empleadoSolicitaContrasenya.getCodigoEmpleado());
			
			if (!funcionesUtiles.verificarContrasenya(empleadoSolicitaContrasenya.getContrasenya(), empleadoEncontrado.getContrasenya()))
			{
				empleadoSolicitaContrasenya.setContrasenya(funcionesUtiles.encriptarContrasenya(empleadoSolicitaContrasenya.getContrasenya()));
			}
			else
			{
				respuesta.setRespuesta("Contraseña anterior.");
				respuesta.setEmpleados(null);
				return new ResponseEntity<RespuestaEmpleados>(respuesta, HttpStatus.BAD_REQUEST);
			}

			
		}
		catch (Exception e)
		{
			respuesta.setRespuesta("Error al restablecer la contraseña: "+e);
			respuesta.setEmpleados(null);
			return new ResponseEntity<RespuestaEmpleados>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
		
		
		return restablecerContrasenya(empleadoSolicitaContrasenya);
		

	}
	
	
	
	@Override
	@Transactional
	public ResponseEntity<RespuestaEmpleadoLogueado> hacerLogin(ModeloEmpleados empleado) 
	{
	    RespuestaEmpleadoLogueado respuesta = new RespuestaEmpleadoLogueado();
	    try 
	    {
	        String nombreCorreoCodigo = empleado.getNombre().toUpperCase();
	        String contrasenyaLogin = empleado.getContrasenya();

	        ModeloEmpleados empleadoBD = empleadosDao.getEmpleadoPorCorreo(nombreCorreoCodigo);
	        
	        if (empleadoBD == null) 
	        {
	            empleadoBD = empleadosDao.getEmpleadoPorCodigo(nombreCorreoCodigo);
	        }


	        if (empleadoBD == null) 
	        {
	            return generarRespuestaError("Error. Usuario o contraseña incorrectas.");
	        }

	        if (empleadoBD.getEstado()== EnumEstados.ACTIVO)
	        {
	            String contrasenyaBD = empleadoBD.getContrasenya();
		        LocalDateTime fechaExpir = empleadoBD.getUltimaModificacionContrasenya();
		        int numeroMesesCaducidad = 1;
		        LocalDateTime haceUnMes = LocalDateTime.now().minusMonths(numeroMesesCaducidad);

		       
		            if (funcionesUtiles.verificarContrasenya(contrasenyaLogin, contrasenyaBD)) 
		            {
		            	if (fechaExpir == null || !fechaExpir.isBefore(haceUnMes)) 
		 		        {
		            		DTOEmpleadoLogueado empleadoParaFront = new DTOEmpleadoLogueado();
		                
		            		empleadoParaFront.setNombre(empleadoBD.getNombre());
		            		empleadoParaFront.setEmpleadoId(empleadoBD.getId());
		            		empleadoParaFront.setRol(empleadoBD.getRol());
		               		empleadoParaFront.setCodigoEmpleado(empleadoBD.getCodigoEmpleado());

		               		respuesta.setRespuesta("Login correcto.");
		               		respuesta.setEmpleadoLogueado(empleadoParaFront);
		               		respuesta.setJwt(funcionesUtiles.generateJWT(empleadoBD));

		 		        } 
		            	 else 
		 		        {
		 		        	respuesta.setRespuesta("Contraseña caducada.");
		 		        	
		 		            respuesta.setEmpleadoLogueado(null);
		 	    			respuesta.setJwt(null);
		 	    			return new ResponseEntity<RespuestaEmpleadoLogueado>(respuesta, HttpStatus.BAD_REQUEST);
		 		        }
		            }
		            else 
		            {
		             	respuesta.setRespuesta("Error. Usuario o contraseña incorrectas.");
		    			respuesta.setEmpleadoLogueado(null);
		    			respuesta.setJwt(null);
		    			return new ResponseEntity<RespuestaEmpleadoLogueado>(respuesta, HttpStatus.BAD_REQUEST);
		            }
		        
		       
	        }
	        else
	        {
	        	respuesta.setRespuesta("Usuario no activo.");
				respuesta.setEmpleadoLogueado(null);
				respuesta.setJwt(null);
				return new ResponseEntity<RespuestaEmpleadoLogueado>(respuesta, HttpStatus.BAD_REQUEST);
	        }
	

	    } 
	    catch (Exception e) 
	    {
	    	respuesta.setRespuesta("Error al hacer login.");
			respuesta.setEmpleadoLogueado(null);
			respuesta.setJwt(null);
			return new ResponseEntity<RespuestaEmpleadoLogueado>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	    
	    return new ResponseEntity<RespuestaEmpleadoLogueado> (respuesta, HttpStatus.OK);
	}

	private ResponseEntity<RespuestaEmpleadoLogueado> generarRespuestaError(String mensaje) 
	{
	    RespuestaEmpleadoLogueado respuesta = new RespuestaEmpleadoLogueado();
	    respuesta.setRespuesta(mensaje);
	    respuesta.setEmpleadoLogueado(null);
	    respuesta.setJwt(null);
	    return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
	}

	
	
	
	
	@Override
	@Transactional
	public ResponseEntity<RespuestaEmpleados> guardarNuevoEmpleado (ModeloEmpleados empleado)
	{
		RespuestaEmpleados respuesta = new RespuestaEmpleados();
		
		empleado.setNombre(funcionesUtiles.capitalizer(empleado.getNombre()));
		empleado.setPrimerApellido(funcionesUtiles.capitalizer(empleado.getPrimerApellido()));
		empleado.setSegundoApellido(funcionesUtiles.capitalizer(empleado.getSegundoApellido()));
		empleado.setCodigoEmpleado(funcionesUtiles.generateCode(empleado));
		empleado.setCorreo(funcionesUtiles.generateCorreo(empleado));
		empleado.setContrasenya(funcionesUtiles.encriptarContrasenya(empleado.getContrasenya()));
		empleado.setFechaCreacion(LocalDateTime.now());
		empleado.setUltimaModificacionContrasenya(LocalDateTime.now());
		empleado.setEstado(EnumEstados.valueOf("ACTIVO"));
		
		if (empleado.getResponsableId() != null && empleado.getRol() != EnumRoles.ADMIN)
		{
			try
			{		
				if (funcionesUtiles.comprobarEmpleadoExistePorNombreCompleto(empleado))
				{
					respuesta.setRespuesta("Empleado ya existente.");
					respuesta.setEmpleados(null);
					return new ResponseEntity<RespuestaEmpleados>(respuesta, HttpStatus.BAD_REQUEST);
				}
				else
				{
					ModeloEmpleados empleadoGuardado = empleadosDao.save(empleado);
				
					if (empleadoGuardado != null)
					{
						respuesta.setRespuesta(empleadoGuardado.getCorreo());
						respuesta.setEmpleados(null);
						
						ModeloMensajes mensajeSignUp = new ModeloMensajes();
						mensajeSignUp.setAsunto("Bienvenid@ a CCAPHY, " + empleadoGuardado.getNombre());
						
						String contenido = "Le damos la bienvenida a CCAPHY,"
										+ "\n\nSu dirección de correo electrónico es: " + empleadoGuardado.getCorreo()
										+ "\nSu código de empleado es: " + empleadoGuardado.getCodigoEmpleado()
										+ "\n\nPara cualquier duda puede escribir a este correo."
										+ "\n\nGracias.";
									
						
						mensajeSignUp.setContenido(contenido);
						
						ModeloEmpleados emisorMensaje = new ModeloEmpleados();
						
						emisorMensaje.setId(1L);
						
						mensajeSignUp.setEmisor(emisorMensaje);
						
						mensajeSignUp.setReceptor(empleadoGuardado);
						
						serviciosMensajes.guardarNuevoMensaje(mensajeSignUp);
						
					}
					else
					{
						respuesta.setRespuesta("Error al almacenar.");
						respuesta.setEmpleados(null);
						return new ResponseEntity<RespuestaEmpleados>(respuesta, HttpStatus.BAD_REQUEST);
					}
				}
				
			}
			catch (Exception e)
			{
				respuesta.setRespuesta("Error al almacenar: " + e);
				respuesta.setEmpleados(null);
				return new ResponseEntity<RespuestaEmpleados>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		}
		
		else
		{
			respuesta.setRespuesta("Empleado sin responsable asignado.");
			respuesta.setEmpleados(null);
			return new ResponseEntity<RespuestaEmpleados>(respuesta, HttpStatus.BAD_REQUEST);
		}
		
		
		return new ResponseEntity<RespuestaEmpleados> (respuesta, HttpStatus.OK);
	}

	
	}
