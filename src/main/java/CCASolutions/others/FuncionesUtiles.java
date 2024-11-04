package CCASolutions.others;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import CCASolutions.dao.IEmpleadosDao;
import CCASolutions.modelos.ModeloEmpleados;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class FuncionesUtiles 
{
	@Autowired
	private IEmpleadosDao empleadosDao;
	

	@Value("${jwt.secret-key}")
	private String secretKey;
	
	public String generateCorreo(ModeloEmpleados empleado) 
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

	
	public boolean verificarContrasenya (String contrasenyaIngresada, String hashAlmacenado)
	{
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		return encoder.matches(contrasenyaIngresada, hashAlmacenado);

	}
	
	public boolean comprobarEmpleadoExistePorNombreCompleto(ModeloEmpleados empleado) 
	{
	    boolean empleadoExiste = false;

	    try 
	    {

	        if (empleado.getNombre() == null || empleado.getPrimerApellido() == null || empleado.getSegundoApellido() == null) 
	        {
	            return false;
	        }


	        empleadoExiste = empleadosDao.existsByNombreAndPrimerApellidoAndSegundoApellido(empleado.getNombre(), empleado.getPrimerApellido(), empleado.getSegundoApellido());
	    } 
	    catch (Exception e) 
	    {
	        System.out.println("Error: " + e);
	    }

	    return empleadoExiste;
	}
	
	
	public String generateJWT(ModeloEmpleados empleado)
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
	
	
	
	public String encriptarContrasenya (String contrasenya)
	{
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		return encoder.encode(contrasenya);

	}
	
	
	
	public String capitalizer (String cadena) 
	 {
		 if (cadena == null || cadena.isEmpty()) 
		 {
			 return cadena; 
		 }
		 return cadena.substring(0, 1).toUpperCase() + cadena.substring(1);
	 }
	 
	public String generateCode (ModeloEmpleados empleado)
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
		    	 codigoGeneradoAleatoriamente.append(caracterAleatorio);
		     }
	
			 	
			 return codigoGeneradoAleatoriamente.toString();
		 }
		 }


}
