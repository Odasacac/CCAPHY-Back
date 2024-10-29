package CCASolutions.respuestas;

import java.util.List;

import CCASolutions.modelos.ModeloNotasPaciente;
import lombok.Data;


@Data
public class RespuestaNotasPaciente 
{
	private String respuesta;
	private List <ModeloNotasPaciente> notas;
}
