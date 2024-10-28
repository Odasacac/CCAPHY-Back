package CCASolutions.enums;

public enum EnumSexo 
{
	XY,
	XX;
	
	 public String getDescripcion() 
	 {
		 switch (this) 
		 {
	     	case XY: return "Se trata de un hombre biológico.";
	     	case XX: return "Se trata de una mujer biológica.";
	     	default: return "Sexo desconocido";
		 }
	  }
	
}
