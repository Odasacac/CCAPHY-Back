package CCASolutions.enums;

public enum EnumRoles 
{
	 ADMIN,
	 RESPONSABLE,
	 EMPLEADO;
	 
	 public String getDescripcion() 
	 {
		 switch (this) 
		 {
	     	case ADMIN: return "Administrador del sistema";
	     	case RESPONSABLE: return "Jefe de departamento";
	     	case EMPLEADO: return "Empleado del sistema";
	     	default: return "Rol desconocido";
		 }
	  }
}
