package CCASolutions.respuestas;

import java.util.List;

import CCASolutions.modelos.ModeloMensajes;
import lombok.Data;

@Data
public class RespuestaMensajes 
{
	private String respuesta;
	private List <ModeloMensajes> mensajes;
}
