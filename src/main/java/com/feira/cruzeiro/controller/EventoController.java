package com.feira.cruzeiro.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.feira.cruzeiro.model.Evento;
import com.feira.cruzeiro.model.User;
import com.feira.cruzeiro.repository.EventoRepository;
import com.feira.cruzeiro.repository.UserRepository;
import com.feira.cruzeiro.utils.FileUploadUtil;

@Controller
public class EventoController {
	
	@Autowired
	private EventoRepository eventoRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping(value = "/eventos", method = RequestMethod.GET)
	public ModelAndView getListagemEventos() {
		List<Evento> eventos = eventoRepository.findAll();
		ModelAndView mv = new ModelAndView("eventos.html");
		mv.addObject("titulo", "Eventos");
		mv.addObject("eventos", eventos);
		return mv;
	}
	
	@RequestMapping(value = "/eventos/{id}", method = RequestMethod.GET)
	public ModelAndView getEventoDetalhe(@PathVariable("id") long id) {
		Evento evento = eventoRepository.findById(id).get();
		ModelAndView mv = new ModelAndView("eventoDetalhe.html");
		mv.addObject("evento", evento);
		mv.addObject("titulo", "Detalhes");
		return mv;
	}
	
	@RequestMapping(value = "/eventoform/{id}", method = RequestMethod.GET)
	public ModelAndView getEventoFormUpdt(@PathVariable("id") long id ) {
		Evento evento = eventoRepository.findById(id).get();
		ModelAndView mv = new ModelAndView("formEvento.html");
		mv.addObject("evento", evento);
		return mv;
	}
	
	@RequestMapping(value = "/newevento", method = RequestMethod.POST)
	public String saveEvento(@Valid Evento evento, BindingResult result, RedirectAttributes redirect,
			 						@RequestParam("imagem") MultipartFile multipartFile) throws IOException {
		if(result.hasErrors()) {
			redirect.addFlashAttribute("message", "Verifique os campos obrigatórios.");
			return "redirect:/lojaform";
		}
		
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findById(userName).get();
		evento.setUser(user);
		evento.setArquivo(multipartFile.getOriginalFilename());
		
		eventoRepository.save(evento);
		FileUploadUtil.createFile(null, evento, multipartFile);
		
		redirect.addFlashAttribute("messageSuccess", "Evento cadastrado com sucesso.");
		return "redirect:/lojaform"; 
	}
	
	@RequestMapping(value = "/atualizaevento", method = RequestMethod.POST)
	public String atualizarEvento(@Valid Evento evento,  @RequestParam("imagem") MultipartFile multipartFile, 
							BindingResult result, RedirectAttributes redirect) throws IOException {
		if(result.hasErrors()) {
			redirect.addFlashAttribute("message", "Verifique os campos obrigatórios.");
			return "redirect:/lojaform";
		}
		
		if(!multipartFile.getOriginalFilename().isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			FileUploadUtil.removerFile(evento);
			evento.setArquivo(fileName);
			FileUploadUtil.createFile(null, evento, multipartFile);
		}
		eventoRepository.save(evento);
		
		redirect.addFlashAttribute("messageSuccess", "Evento alterado com sucesso.");
		return "redirect:/lojaform"; 
	}
	
	@RequestMapping(value = "/removerEvento/{id}", method = RequestMethod.GET)
	public String removerEvento(@PathVariable("id") long id,  RedirectAttributes redirect) {
		Evento evento = eventoRepository.findById(id).get();
		FileUploadUtil.removerFile(evento);
		eventoRepository.delete(evento);
		redirect.addFlashAttribute("messageSuccess", "Evento removido com sucesso.");
		
		return "redirect:/lojaform";
	}

}
