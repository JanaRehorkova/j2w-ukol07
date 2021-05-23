package cz.czechitas.java2webapps.ukol7.controller;

import cz.czechitas.java2webapps.ukol7.entity.Vizitka;
import cz.czechitas.java2webapps.ukol7.repository.VizitkyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class VizitkyController {
    private final VizitkyRepository repositoryVizitky;

    @Autowired
    public VizitkyController(VizitkyRepository repositoryVizitky) {
        this.repositoryVizitky = repositoryVizitky;
    }
//
//    private final List<Vizitka> nepouzityList= List.of(
//            new Vizitka(1, "John Lawrence", "Eagle Fang Karate", "Jurkovičova 2", "Brno", "63800", "strikefirst@eaglefang.com", null, null)
//    );

    @InitBinder
    public void nullStringBinding(WebDataBinder binder) {
          binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/")
    public Object vizitkaMetoda() {
        return new ModelAndView("seznam")
                .addObject("seznamvizitek", repositoryVizitky.findAll());


    }

    @GetMapping("/{id}")
    public Object detailMetoda(@PathVariable int id) {
        Optional <Vizitka> detaily = repositoryVizitky.findById(id);
        if(detaily.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return new ModelAndView("vizitka")
                .addObject("detaily", detaily.get());
    }

    @PostMapping(value = "/{id:[0-9]+}", params = "akce=smazat")
    public Object smazat(@PathVariable int id) {
        repositoryVizitky.deleteById(id);
        return "redirect:/";
    }
    @GetMapping("/nova")
    public Object nova() {
        return new ModelAndView("formular")
                .addObject("vizitka", new Vizitka());
    }


    @PostMapping("/nova")
    public Object pridat(@ModelAttribute("vizitka") @Valid Vizitka novaVizitka, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "formular";
        }
        repositoryVizitky.save(novaVizitka);
        return "redirect:/";
    }

   @PostMapping("/{id:[0-9]+}")
   public Object ulozit(@PathVariable long id, @ModelAttribute("vizitka") @Valid Vizitka upravVizitka, BindingResult bindingResult) {
      if (bindingResult.hasErrors()) {
           return "formular";
       }
       repositoryVizitky.save(upravVizitka);
        return "redirect:/";
    }
}

