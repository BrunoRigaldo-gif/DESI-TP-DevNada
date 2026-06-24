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

    // Epic 2.4 - Mostrar el listado de publicaciones
    @GetMapping
    public String listarPublicaciones(Model model) {
        // Buscamos todas las publicaciones y se las mandamos a la vista HTML
        model.addAttribute("publicaciones", publicacionService.obtenerTodas());
        return "publicaciones/listado"; // Esto buscará el archivo listado.html
    }

    // Epic 2.1 - Mostrar el formulario vacío para crear una nueva
    @GetMapping("/nueva")
    public String mostrarFormularioAlta(Model model) {
        model.addAttribute("publicacion", new Publicacion());
        // TODO: Más adelante vamos a tener que mandarle también la lista de Inmuebles a la vista 
        // para que el usuario pueda elegir uno del menú desplegable.
        return "publicaciones/formulario"; // Esto buscará el archivo formulario.html
    }

    // Epic 2.1 - Recibir los datos del formulario y guardarlos
    @PostMapping("/guardar")
    public String guardarPublicacion(@ModelAttribute("publicacion") Publicacion publicacion, RedirectAttributes redirectAttributes) {
        try {
            // Intentamos guardarla usando la lógica estricta que creamos en el Service
            publicacionService.guardarPublicacion(publicacion);
            redirectAttributes.addFlashAttribute("exito", "¡Publicación creada correctamente!");
            
        } catch (Exception e) {
            // Si salta el error de "No cumple normas" o "Ya tiene publicación activa", 
            // lo capturamos y se lo mostramos al usuario en la pantalla.
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/publicaciones/nueva"; // Lo devolvemos al formulario
        }
        
        return "redirect:/publicaciones"; // Si sale bien, lo mandamos a ver el listado
    }
 // Epic 2.2 - Mostrar el formulario cargado con los datos existentes
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            // Buscamos la publicación y la mandamos al formulario
            Publicacion publicacion = publicacionService.obtenerPorId(id);
            model.addAttribute("publicacion", publicacion);
            return "publicaciones/formulario"; // ¡Reutilizamos el mismo HTML que ya tenés!
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/publicaciones"; // Si hay error, lo devolvemos al listado
        }
    }

    // Epic 2.3 - Eliminar la publicación al hacer clic en el botón
    @GetMapping("/eliminar/{id}")
    public String eliminarPublicacion(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        publicacionService.eliminar(id);
        redirectAttributes.addFlashAttribute("exito", "¡Publicación eliminada correctamente!");
        return "redirect:/publicaciones";
    }
}