package CCASolutions.others;

import CCASolutions.modelos.ModeloEmpleados;
import CCASolutions.modelos.ModeloPacientes;

public interface IFuncionesUtiles
{
	public String encriptarContrasenya (String contrasenya);
	
	public  String capitalizer (String cadena);
	 
	public  String generateCode (ModeloEmpleados empleado);
	
	public String generateCorreo(ModeloEmpleados empleado);
	
	public boolean verificarContrasenya (String contrasenyaIngresada, String hashAlmacenado);
	
	public boolean comprobarEmpleadoExistePorNombreCompleto(ModeloEmpleados empleado);
	
	public boolean comprobarPacienteExistePorNombreCompleto(ModeloPacientes paciente);
	
	public String generateJWT(ModeloEmpleados empleado);
	
	public Long obtenerEdadPorFechaNacimiento(String fechaNacimiento);
}
