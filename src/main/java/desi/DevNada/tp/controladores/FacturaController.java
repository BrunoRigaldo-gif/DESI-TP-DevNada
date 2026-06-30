package desi.DevNada.tp.controladores;
import desi.DevNada.tp.entidades.Contrato;
import java.time.LocalDate;

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
    public String listar(
            @RequestParam(required = false) Long contratoId,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) LocalDate fechaDesde,
            @RequestParam(required = false) LocalDate fechaHasta,
            Model model) {

        model.addAttribute("facturas", facturaService.filtrarFacturas(
                contratoId,
                estado,
                fechaDesde,
                fechaHasta
        ));

        model.addAttribute("contratos", contratoRepo.findAll());

        return "Facturas/Listado";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        Factura factura = new Factura();
        factura.setContrato(new Contrato()); // evita null en th:field="*{contrato.id}"
        model.addAttribute("factura", factura);
        model.addAttribute("contratos", contratoRepo.findByEliminadoFalseAndEstadoContratoIgnoreCase("activo"));
        return "Facturas/Formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Factura factura, RedirectAttributes redirect) {
        try {
            if (factura.getContrato() == null || factura.getContrato().getId() == null) {
                throw new RuntimeException("Debe seleccionar un contrato");
            }

            Long idContrato = factura.getContrato().getId();

            Contrato contratoCompleto = contratoRepo.findById(idContrato)
                    .orElseThrow(() -> new RuntimeException("El contrato no existe"));

            factura.setContrato(contratoCompleto);

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
        model.addAttribute("contratos", contratoRepo.findByEliminadoFalseAndEstadoContratoIgnoreCase("activo"));

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
