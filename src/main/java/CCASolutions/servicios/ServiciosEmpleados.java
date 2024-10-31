package CCASolutions.servicios;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import CCASolutions.dao.IEmpleadosDao;
import CCASolutions.modelos.ModeloEmpleadoLogueado;
import CCASolutions.modelos.ModeloEmpleados;
import CCASolutions.respuestas.RespuestaEmpleadoLogueado;
import CCASolutions.respuestas.RespuestaEmpleados;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class ServiciosEmpleados implements IServiciosEmpleados
{
	@Autowired
	private IEmpleadosDao empleadosDao;

	@Value("${jwt.secret-key}")
	private String secretKey;
	
	@Override
	@Transactional
	public ResponseEntity<RespuestaEmpleadoLogueado> hacerLogin (ModeloEmpleados empleado)
	{
		RespuestaEmpleadoLogueado respuesta = new RespuestaEmpleadoLogueado();
	
		try
		{
			Iterable <ModeloEmpleados> allEmpleados = empleadosDao.findAll();
			
			String nombreCorreoCodigo = empleado.getNombre().toUpperCase();
			String contrasenyaLogin = empleado.getContrasenya();
			
			boolean existeCorreo = false;
			boolean existeCodigo = false;
			
			for (ModeloEmpleados empleadoExistente : allEmpleados)
			{

				
				 if (empleadoExistente.getCorreo() != null && empleadoExistente.getCorreo().equals(nombreCorreoCodigo)) 
				 {
		                existeCorreo = true;
		                break;
		         }
				 if (empleadoExistente.getCodigoEmpleado() != null && empleadoExistente.getCodigoEmpleado().equals(nombreCorreoCodigo)) 
				 {
		                existeCodigo = true;
		                break;
		         }
			}
			
			
			if (existeCorreo || existeCodigo)
			{
				String contrasenyaBD = "";
				
				if (existeCorreo)
				{
					contrasenyaBD = empleadosDao.getContrasenyaDeEmpleadoPorCorreo(nombreCorreoCodigo);
				}
				else if (existeCodigo)
				{
					contrasenyaBD = empleadosDao.getContrasenyaDeEmpleadoPorCodigo(nombreCorreoCodigo);
				}
				
				
				
				
				if (verificarContrasenya(contrasenyaLogin, contrasenyaBD))
				{
					ModeloEmpleados empleadoLogueado = new ModeloEmpleados();
					
					if (existeCorreo)
					{
						empleadoLogueado = empleadosDao.getEmpleadoPorCorreo(nombreCorreoCodigo);
					}
					else if (existeCodigo)
					{
						empleadoLogueado = empleadosDao.getEmpleadoPorCodigo(nombreCorreoCodigo);
					}
			
					 ModeloEmpleadoLogueado empleadoParaFront = new ModeloEmpleadoLogueado();
		             empleadoParaFront.setNombre(empleadoLogueado.getNombre());
		             empleadoParaFront.setEmpleadoId(empleadoLogueado.getId());
		             empleadoParaFront.setRol(empleadoLogueado.getRol());
		                
					respuesta.setRespuesta("Login correcto.");
					respuesta.setEmpleadoLogueado(empleadoParaFront);
					
					//Generamos el JWT
					String jwt = generateJWT(empleadoLogueado);
					
					respuesta.setJwt(jwt);
					
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
				respuesta.setRespuesta("Error. Usuario o contraseña incorrectas.");
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
	
	
	
	
	
	
	@Override
	@Transactional
	public ResponseEntity<RespuestaEmpleados> guardarNuevoEmpleado (ModeloEmpleados empleado)
	{
		RespuestaEmpleados respuesta = new RespuestaEmpleados();
		
		empleado.setNombre(capitalizer(empleado.getNombre()));
		empleado.setPrimerApellido(capitalizer(empleado.getPrimerApellido()));
		empleado.setSegundoApellido(capitalizer(empleado.getSegundoApellido()));
		empleado.setCodigoEmpleado(generateCode(empleado));
		empleado.setCorreo(generateCorreo(empleado));
		empleado.setContrasenya(encriptarContrasenya(empleado.getContrasenya()));
		empleado.setFechaCreacion(LocalDateTime.now());
		
		try
		{		
			if (comprobarEmpleadoExistePorNombreCompleto(empleado))
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
					
					// Aqui podria implementarse enviar al correo un mensaje al correo dando la bienvenida
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
		
		
		
		return new ResponseEntity<RespuestaEmpleados> (respuesta, HttpStatus.OK);
	}
	
	
	
	
	
	
	//METODOS APARTE
	
	
	
	private boolean verificarContrasenya (String contrasenyaIngresada, String hashAlmacenado)
	{
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		return encoder.matches(contrasenyaIngresada, hashAlmacenado);

	}
	
	
	private String generateJWT(ModeloEmpleados empleado)
	{
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + 86400000);
		
		SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

		String token = Jwts.builder()
        	.setSubject(empleado.getCodigoEmpleado())
        	.claim("role", empleado.getRol()) 
        	.setIssuedAt(now)
        	.setExpiration(expiryDate) 
        	.signWith(key, SignatureAlgorithm.HS256)
        	.compact();

			return token;
	}
	
	

	
	private boolean comprobarEmpleadoExistePorNombreCompleto(ModeloEmpleados empleado)
	{
		Iterable <ModeloEmpleados> allEmpleados = empleadosDao.findAll();
		boolean empleadoExiste = false;
		
		for (ModeloEmpleados empleadoExistente : allEmpleados)
		{
			if (empleadoExistente.getNombre().equals(empleado.getNombre()) && empleadoExistente.getPrimerApellido().equals(empleado.getPrimerApellido())&& empleadoExistente.getSegundoApellido().equals(empleado.getSegundoApellido())) 
			{		        
			        empleadoExiste = true;
			        break; //
			}
		}
		
		return empleadoExiste;
	}

	
	
	private String generateCorreo(ModeloEmpleados empleado) 
	{
	    String correo;
	    Random random = new Random();
	    String caracteres = "1234567890";

	    try 
	    {
	        List<String> allMails = empleadosDao.findAllCorreos();

	        char primeraParte = empleado.getNombre().charAt(0);
	        String segundaParte = empleado.getPrimerApellido();
	        String terceraParte = empleado.getSegundoApellido();
	        correo = ("" + primeraParte + segundaParte + terceraParte + "@CCAPHY.COM").toUpperCase();

	        
	        if (!allMails.contains(correo)) 
	        {
	            return correo;
	        }

	       
	        do 
	        {
	            StringBuilder randomStr = new StringBuilder();
	            for (int i = 0; i < 3; i++) 
	            {
	                int indiceAleatorio = random.nextInt(caracteres.length());
	                char caracterAleatorio = caracteres.charAt(indiceAleatorio);
	                randomStr.append(caracterAleatorio);
	            }
	            correo = "CORREOPROVISIONAL" + randomStr + "@CCAPHY.COM".toUpperCase();
	        } 
	        while (allMails.contains(correo));

	    } 
	    catch (Exception e) 
	    {
	       
	        return "ERRORCORREOPROVISIONAL@CCAPHY.COM";
	    }

	    return correo;
	}

	
	private static String encriptarContrasenya (String contrasenya)
	{
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		return encoder.encode(contrasenya);

	}
	
	
	
	 private static String capitalizer (String cadena) 
	 {
		 if (cadena == null || cadena.isEmpty()) 
		 {
			 return cadena; 
		 }
		 return cadena.substring(0, 1).toUpperCase() + cadena.substring(1);
	 }
	 
	 private String generateCode (ModeloEmpleados empleado)
	 {
		 String codigo = "";
		 boolean valido = false;
		 boolean coincide = false;
		 int caracter = 0;
		 
		 try
		 {
			 List<String> allCodes = empleadosDao.findAllCodigoUsuario();
		 
			 
			 while (!valido)
			 {
				 if (caracter >= empleado.getNombre().length() || caracter >= empleado.getPrimerApellido().length() || caracter >= empleado.getSegundoApellido().length()) 
				 {
					 throw new IllegalStateException("No se pudo generar un código único. No hay suficientes caracteres disponibles.");
			     }
				 
				 char primerCaracter = empleado.getNombre().charAt(caracter);
				 char segundoCaracter = empleado.getPrimerApellido().charAt(caracter);
				 char tercerCaracter = empleado.getSegundoApellido().charAt(caracter);
			 
				 codigo = ("" + primerCaracter + segundoCaracter + tercerCaracter).toUpperCase();
				 coincide = false;
				 
				 for (int i = 0; i < allCodes.size(); i ++)
				 {
					 if (codigo.equals(allCodes.get(i)))
					 {
						coincide = true;
					 	break;
					 }
				 }
			 
				 if (!coincide)
				 {
					 valido = true;
				 }
				 else
				 {
					 caracter = caracter +1;
				 }
			 
			 }

			 return codigo;
		 }
		 catch (Exception e)
		 {
			 System.out.println("Error: " + e);
			 Random random = new Random();
			 StringBuilder  codigoGeneradoAleatoriamente = new StringBuilder();
		     String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		     for (int i = 0; i < 3; i++) 
		     {
		    	 int indiceAleatorio = random.nextInt(caracteres.length()); 
		    	 char caracterAleatorio = caracteres.charAt(indiceAleatorio);
		    	 codigoGeneradoAleatoriamente.append(caracterAleatorio); // Agregar el carácter al código
		     }
	
			 	
			 return codigoGeneradoAleatoriamente.toString();
		 }
		 }
}
