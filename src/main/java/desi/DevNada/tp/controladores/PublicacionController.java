package desi.DevNada.tp.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import desi.DevNada.tp.entidades.Publicacion;
import desi.DevNada.tp.servicios.PublicacionService;

@Controller
@RequestMapping("/publicaciones")
public class PublicacionController {

    @Autowired
    private PublicacionService publicacionService;
    
    
    @GetMapping
    public String listarPublicaciones(Model model) {          
        model.addAttribute("publications", publicacionService.obtenerTodas());
        return "publicaciones/listado";      
    }

    @GetMapping("/nueva")
    public String mostrarFormularioAlta(Model model) {
        model.addAttribute("publicacion", new Publicacion());
        
     
        return "publicaciones/formulario";
    }

    @PostMapping("/guardar")
    public String guardarPublicacion(@ModelAttribute("publicacion") Publicacion publicacion,
                                     RedirectAttributes redirectAttributes) {
        try {            
            publicacionService.guardarPublicacion(publicacion);
            redirectAttributes.addFlashAttribute("exito", "¡Publicación creada correctamente!");    
        } catch (Exception e) {                  
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/publicaciones/nueva";          
        }          
        return "redirect:/publicaciones";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Long id, Model model,
                                          RedirectAttributes redirectAttributes) {
        try {            
            Publicacion publicacion = publicacionService.obtenerPorId(id);
            model.addAttribute("publicacion", publicacion);
           
            return "publicaciones/formulario";              
        } catch (Exception e) {              
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/publicaciones";
        }     
    }
}