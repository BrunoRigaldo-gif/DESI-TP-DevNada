package desi.DevNada.tp.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import desi.DevNada.tp.accesoDatos.IPersonaRepo;
import desi.DevNada.tp.entidades.Contrato;
import desi.DevNada.tp.servicios.ContratoService;
import desi.DevNada.tp.servicios.PropiedadService;

@Controller
@RequestMapping("/contratos")
public class ContratoController {

	@Autowired
	private ContratoService contratoService;

	@Autowired
	private PropiedadService propiedadService;

	@Autowired
	private IPersonaRepo personaRepo;

	// Muestra el listado de contratos activos
	@GetMapping
	public String listarContratos(Model modelo) {
		modelo.addAttribute("contratos", contratoService.listarActivos());
		return "contratos/listado";
	}

	// Muestra el formulario para dar de alta un contrato
	@GetMapping("/nuevo")
	public String mostrarFormularioAlta(Model modelo) {
		modelo.addAttribute("contrato", new Contrato());
		modelo.addAttribute("propiedades", propiedadService.listarActivas());
		modelo.addAttribute("inquilinos", personaRepo.findAll());
		return "contratos/formulario";
	}

	// Muestra el formulario de edición cargado con los datos existentes
	@GetMapping("/editar/{id}")
	public String mostrarFormularioEditar(@PathVariable("id") Long id, Model modelo,
			RedirectAttributes redirectAttributes) {
		try {
			modelo.addAttribute("contrato", contratoService.obtenerPorId(id));
			modelo.addAttribute("propiedades", propiedadService.listarActivas());
			modelo.addAttribute("inquilinos", personaRepo.findAll());
			return "contratos/formulario";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/contratos";
		}
	}

	// Da de alta o modifica un contrato
	@PostMapping("/guardar")
	public String guardarContrato(@ModelAttribute("contrato") Contrato contrato, Model modelo,
			RedirectAttributes redirectAttributes) {
		try {
			contratoService.guardar(contrato);
			redirectAttributes.addFlashAttribute("exito", "¡Contrato guardado correctamente!");
			return "redirect:/contratos";
		} catch (RuntimeException e) {
			// Los errores de negocio se muestran en rojo reutilizando el formulario
			modelo.addAttribute("error", e.getMessage());
			modelo.addAttribute("propiedades", propiedadService.listarActivas());
			modelo.addAttribute("inquilinos", personaRepo.findAll());
			return "contratos/formulario";
		}
	}

	// Elimina un contrato de forma lógica
	@GetMapping("/eliminar/{id}")
	public String eliminarContrato(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		contratoService.eliminar(id);
		redirectAttributes.addFlashAttribute("exito", "¡Contrato eliminado correctamente!");
		return "redirect:/contratos";
	}
}
