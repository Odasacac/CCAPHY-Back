package CCASolutions.modelos;

import CCASolutions.enums.EnumRoles;
import lombok.Data;

@Data
public class ModeloEmpleadoLogueado 
{
	private String nombre;
	private String codigoEmpleado;
	private EnumRoles rol;
}
