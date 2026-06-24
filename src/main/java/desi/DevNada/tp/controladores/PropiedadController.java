package desi.DevNada.tp.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import desi.DevNada.tp.servicios.PropiedadService;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class PropiedadController {

	@Autowired
	private PropiedadService servicio;

	@GetMapping("/listadoPropiedades")
	public String listarPropiedades(Model modelo) {
		modelo.addAttribute("listadoPropiedades", servicio.listarActivas());
		return "listadoPropiedades";
	}

}
