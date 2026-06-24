package desi.DevNada.tp.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import desi.DevNada.tp.entidades.Propiedad;
import desi.DevNada.tp.servicios.CiudadService;
import desi.DevNada.tp.servicios.PropiedadService;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class PropiedadController {

	@Autowired
	private PropiedadService servicio;
	
	@Autowired
	private CiudadService servicioC;

	@GetMapping("/listadoPropiedades")
	public String listarPropiedades(Model modelo) {
		modelo.addAttribute("listadoPropiedades", servicio.listarActivas());
		return "listadoPropiedades";
	}
	@GetMapping("/propiedades/nuevo")
	public String mostrarFormulario(Model modelo) {
	    Propiedad p = new Propiedad();
	    modelo.addAttribute("propiedad", p);
	    modelo.addAttribute("allCiudades", servicioC.listarTodas());
	    return "crearPropiedad";
	}
	
	@PostMapping("/propiedades")
	public String altaPropiedad(@ModelAttribute("propiedad") Propiedad propiedad) {
		servicio.guardar(propiedad);
		return "redirect:/listadoPropiedades";
	}

}
