package CCASolutions.modelos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import CCASolutions.enums.EnumEstados;
import CCASolutions.enums.EnumSexo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="pacientes")
public class ModeloPacientes implements Serializable
{

	private static final long serialVersionUID = -1353053810652553612L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nombre;
	private String primerApellido;
	private String segundoApellido;
	private String correo;
	private String telefonoContacto;
	private String direccion;
	private String fechaNacimiento;
	private Long edad;
	
	@Enumerated(EnumType.STRING)
	private EnumEstados estado;
	
	@Enumerated(EnumType.STRING)
	private EnumSexo sexo;
	
	private LocalDateTime fechaAlta;
	private LocalDateTime fechaBaja;
	
	private String motivoConsulta;
	
	@ManyToOne
	@JoinColumn(name = "responsableId", referencedColumnName = "id")
	private ModeloEmpleados responsable;
	
	@OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL)
	private List<ModeloNotasPaciente> notas;
}
