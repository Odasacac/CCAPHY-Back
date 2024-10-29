package CCASolutions.respuestas;

import java.util.List;

import CCASolutions.modelos.ModeloPacientes;
import lombok.Data;

@Data
public class RespuestaPacientes 
{
	private String respuesta;
	private List <ModeloPacientes> pacientes;
}
