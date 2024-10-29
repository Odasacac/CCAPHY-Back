package CCASolutions.modelos;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="mensajes")
public class ModeloMensajes implements Serializable
{

	private static final long serialVersionUID = 6889681751390604949L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String asunto;
	private String contenido;
	private LocalDateTime fechaEnvio;
	private LocalDateTime fechaApertura;
	
	@ManyToOne
    @JoinColumn(name = "emisor_id", referencedColumnName = "id")
    private ModeloEmpleados emisor;

    @ManyToOne
    @JoinColumn(name = "receptor_id", referencedColumnName = "id")
    private ModeloEmpleados receptor;
	
	private boolean estaEnPapelera;

}
