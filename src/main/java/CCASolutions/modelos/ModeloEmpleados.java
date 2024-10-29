package CCASolutions.modelos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import CCASolutions.enums.EnumEspecialidades;
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
public class ModeloEmpleados implements Serializable
{

	private static final long serialVersionUID = 7556690529710051236L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nombre;
	private String primerApellido;
	private String segundoApellido;
	private String codigoEmpleado;
	private String correo;
	
	private Long responsableId;
	
	@Enumerated(EnumType.STRING)
	private EnumRoles rol;
	
	@Enumerated(EnumType.STRING)
	private EnumEspecialidades especialidad;
	
	private String contrasenyaEncriptada;
	private LocalDateTime fechaCreacion;
	
    @OneToMany(mappedBy = "responsable", cascade = CascadeType.ALL)
    private List<ModeloPacientes> pacientes;
    
    @OneToMany(mappedBy = "responsable", cascade = CascadeType.ALL)
    private List<ModeloNotasPaciente> notas;
    
    @OneToMany(mappedBy = "emisor")
    private List<ModeloMensajes> mensajesEnviados;

    @OneToMany(mappedBy = "receptor")
    private List<ModeloMensajes> mensajesRecibidos;
	
    

}
