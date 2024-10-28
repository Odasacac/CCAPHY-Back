package CCASolutions.modelos;

import java.util.Date;
import java.util.List;

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
public class ModeloPacientes 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nombre;
	private String primerApellido;
	private String segundoApellido;
	private String correo;
	private String telefonoContacto;
	private String direccion;
	private Date fechaNacimiento;
	
	@Enumerated(EnumType.STRING)
	private EnumSexo sexo;
	
	private Date fechaAlta;
	private Date fechaBaja;
	
	private String sintomas;
	
	@ManyToOne
	@JoinColumn(name = "responsableId", referencedColumnName = "id")
	private ModeloEmpleados responsable;
	
	@OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL)
	private List<ModeloNotasPaciente> notas;
}
