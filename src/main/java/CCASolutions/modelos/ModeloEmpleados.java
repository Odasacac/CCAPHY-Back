package CCASolutions.modelos;

import java.util.Date;
import java.util.List;

import CCASolutions.enums.EnumRoles;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="empleados")
public class ModeloEmpleados 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nombre;
	private String primerApellido;
	private String segundoApellido;
	private String codigoUsuario;
	private String correo;
	
	private Long responsableId;
	
	@Enumerated(EnumType.STRING)
	private EnumRoles rol;
	
	private String especialidad;
	
	private String contrasenyaEncriptada;
	private Date fechaCreacion;
	
    @OneToMany(mappedBy = "responsable", cascade = CascadeType.ALL)
    private List<ModeloPacientes> pacientes;
    
    @OneToMany(mappedBy = "responsable", cascade = CascadeType.ALL)
    private List<ModeloNotasPaciente> notas;
	
    

}
