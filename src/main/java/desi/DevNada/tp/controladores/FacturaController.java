package desi.DevNada.tp.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import desi.DevNada.tp.servicios.FacturaService;
import desi.DevNada.tp.accesoDatos.IContratoRepo;
import desi.DevNada.tp.entidades.Factura;

@Controller
@RequestMapping("/facturas")
public class FacturaController {

	@Autowired
	private IContratoRepo contratoRepo;
	
    @Autowired
    private FacturaService facturaService;


    // Muestra el listado de facturas
    @GetMapping
    public String listarFacturas(Model model) {
        model.addAttribute("facturas", facturaService.obtenerTodas());
        return "facturas/listado";
    }
    @GetMapping("/facturas/nueva")
    public String nuevaFactura(Model model) {
        model.addAttribute("factura", new Factura());
        model.addAttribute("contratos", contratoRepo.findAll());
        return "Facturas/Formulario";
    }

    // Muestra el formulario para crear una factura
    @GetMapping("/nueva")
    public String mostrarFormularioAlta(Model model) {
        model.addAttribute("factura", new Factura());
       //Falta Epic3 model.addAttribute("contratos", contratoRepo.findByEstadoContratoAndEliminadoFalse("activo"));
        return "facturas/formulario";
    }
    

    // Guarda una factura nueva
    @PostMapping("/guardar")
    public String guardarFactura(@ModelAttribute("factura") Factura factura,
                                 RedirectAttributes redirectAttributes) {
        try {
            facturaService.guardarFactura(factura);
            redirectAttributes.addFlashAttribute("exito", "Factura creada correctamente");
            return "redirect:/facturas";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/facturas/nueva";
        }
    }

    // Muestra el formulario de edición
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Long id,
                                          Model model,
                                          RedirectAttributes redirectAttributes) {
        try {
            Factura factura = facturaService.obtenerPorId(id);
            model.addAttribute("factura", factura);
           //Falta epic3  model.addAttribute("contratos", contratoRepo.findByEstadoContratoAndEliminadoFalse("activo"));
            return "facturas/formulario";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/facturas";
        }
    }
    // Modifica una factura
    @PostMapping("/modificar")
    public String modificarFactura(@ModelAttribute("factura") Factura factura,
                                   RedirectAttributes redirectAttributes) {
        try {
            facturaService.modificarFactura(factura);
            redirectAttributes.addFlashAttribute("exito", "Factura modificada correctamente");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/facturas";
    }

    // Elimina una factura de forma lógica
    @GetMapping("/eliminar/{id}")
    public String eliminarFactura(@PathVariable("id") Long id,
                                  RedirectAttributes redirectAttributes) {
        try {
            facturaService.eliminar(id);
            redirectAttributes.addFlashAttribute("exito", "Factura eliminada correctamente");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/facturas";
        
    }
}
