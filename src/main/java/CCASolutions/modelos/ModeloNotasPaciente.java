package CCASolutions.modelos;

import java.io.Serializable;
import java.util.Date;
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
@Table(name="notas")
public class ModeloNotasPaciente implements Serializable
{

	private static final long serialVersionUID = 3866825263503845200L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
    @JoinColumn(name = "pacienteId", referencedColumnName = "id")
    private ModeloPacientes paciente;
	
	@ManyToOne
	@JoinColumn(name = "responsableId", referencedColumnName = "id")
	private ModeloEmpleados responsable;
	 
	private Date fechaNota;
	
	private String titulo;
	private String contenido;
	
}
