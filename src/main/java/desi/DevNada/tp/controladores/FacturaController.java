package desi.DevNada.tp.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import desi.DevNada.tp.accesoDatos.IContratoRepo;
import desi.DevNada.tp.entidades.Factura;
import desi.DevNada.tp.servicios.FacturaService;

@Controller
@RequestMapping("/facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private IContratoRepo contratoRepo;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("facturas", facturaService.listar());
        return "Facturas/Listado";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("factura", new Factura());

        // No tocamos Epic 3: usamos lo que ya existe
        model.addAttribute("contratos", contratoRepo.findAll());

        return "Facturas/Formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Factura factura, RedirectAttributes redirect) {
        try {
            facturaService.guardar(factura);
            redirect.addFlashAttribute("mensaje", "Factura guardada correctamente");
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/facturas/nueva";
        }

        return "redirect:/facturas";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        Factura factura = facturaService.buscarPorId(id);

        if (factura == null) {
            redirect.addFlashAttribute("error", "La factura no existe");
            return "redirect:/facturas";
        }

        model.addAttribute("factura", factura);
        model.addAttribute("contratos", contratoRepo.findAll());

        return "Facturas/Formulario";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable Long id, @ModelAttribute Factura factura, RedirectAttributes redirect) {
        try {
            facturaService.modificar(id, factura);
            redirect.addFlashAttribute("mensaje", "Factura modificada correctamente");
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/facturas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            facturaService.eliminar(id);
            redirect.addFlashAttribute("mensaje", "Factura eliminada correctamente");
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/facturas";
    }
}
