package CCASolutions.dtos;

import CCASolutions.enums.EnumRoles;
import lombok.Data;

@Data
public class DTOEmpleadoLogueado 
{
	private Long empleadoId;
	private String nombre;
	private EnumRoles rol;
	private String codigoEmpleado;	
}
