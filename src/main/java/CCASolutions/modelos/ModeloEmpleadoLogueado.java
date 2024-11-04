package CCASolutions.modelos;

import CCASolutions.enums.EnumRoles;
import lombok.Data;

@Data
public class ModeloEmpleadoLogueado 
{
	private Long empleadoId;
	private String nombre;
	private EnumRoles rol;
	private String codigoEmpleado;
}
