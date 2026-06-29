package desi.DevNada.tp.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import desi.DevNada.tp.entidades.Propiedad;
import desi.DevNada.tp.servicios.CiudadService;
import desi.DevNada.tp.servicios.PersonaService;
import desi.DevNada.tp.servicios.PropiedadService;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class PropiedadController {

	@Autowired
	private PropiedadService servicio;

	@Autowired
	private CiudadService servicioC;

	@Autowired
	private PersonaService servicioP;

	@GetMapping("/listadoPropiedades")
	public String listarPropiedades(Model modelo) {
		modelo.addAttribute("listadoPropiedades", servicio.listarActivas());
		return "listadoPropiedades";
	}

	@GetMapping("/propiedades/nuevo")
	public String mostrarFormularioAlta(Model modelo) {
		Propiedad p = new Propiedad();
		modelo.addAttribute("propiedad", p);
		modelo.addAttribute("allCiudades", servicioC.listarTodas());
		modelo.addAttribute("allPersonas", servicioP.getAll());
		return "crearPropiedad";
	}

	@PostMapping("propiedades")
	public String altaPropiedad(@ModelAttribute("propiedad") Propiedad propiedad, Model modelo,
			RedirectAttributes redirectAttributes) {
		try {
			servicio.guardar(propiedad);
			redirectAttributes.addFlashAttribute("exito", "¡Propiedad guardada correctamente!");
			return "redirect:/listadoPropiedades";
		} catch (RuntimeException e) {
			modelo.addAttribute("error", e.getMessage());
			modelo.addAttribute("allCiudades", servicioC.listarTodas());
			modelo.addAttribute("allPersonas", servicioP.getAll());
			return "crearPropiedad";
		}
	}

	@GetMapping("/editar/{id}")
	public String mostrarFormEditarPropiedad(@PathVariable("id") Long id, Model modelo,
			RedirectAttributes redirectAttributes) {
		try {
			Propiedad propiedad = servicio.listarPorID(id);
			modelo.addAttribute("propiedad", propiedad);
			modelo.addAttribute("allCiudades", servicioC.listarTodas());
			modelo.addAttribute("allPersonas", servicioP.getAll());
			return "editarPropiedad";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/listadoPropiedades"; // Si hay error, lo devolvemos al listado
		}
	}

	@PostMapping("/editarPropiedades")
	public String editarPropiedad(@ModelAttribute("propiedad") Propiedad propiedad, Model modelo,
			RedirectAttributes redirectAttributes) {
		try {
			servicio.modificar(propiedad);
			redirectAttributes.addFlashAttribute("exito", "¡Propiedad editada correctamente!");
			return "redirect:/listadoPropiedades";
		} catch (RuntimeException e) {
			modelo.addAttribute("error", e.getMessage());
			modelo.addAttribute("allCiudades", servicioC.listarTodas());
			return "editarPropiedad";
		}
	}

	@GetMapping("/eliminar/{id}")
	public String eliminarPropiedad(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		try {
		servicio.eliminar(id);
		redirectAttributes.addFlashAttribute("exito", "¡Propiedad eliminada correctamente!");
		return "redirect:/listadoPropiedades";
		} catch (RuntimeException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/listadoPropiedades";
		}
	}
}
