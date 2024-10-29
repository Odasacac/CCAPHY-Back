package CCASolutions.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import CCASolutions.dao.IMensajesDao;

@Service
public class ServiciosMensajes implements IServiciosMensajes
{

	@Autowired
	private IMensajesDao mensajesDao;
}
