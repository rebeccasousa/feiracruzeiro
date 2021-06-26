package com.feira.cruzeiro.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.feira.cruzeiro.model.Evento;
import com.feira.cruzeiro.model.Loja;
import com.feira.cruzeiro.model.Pessoa;
import com.feira.cruzeiro.model.User;
import com.feira.cruzeiro.repository.ContatoRepository;
import com.feira.cruzeiro.repository.EventoRepository;
import com.feira.cruzeiro.repository.LojaRepository;
import com.feira.cruzeiro.repository.PessoaRepository;
import com.feira.cruzeiro.repository.UserRepository;
import com.feira.cruzeiro.repository.impl.LojaRepositoryImpl;
import com.feira.cruzeiro.utils.FileUploadUtil;

@Controller
public class LojaController {

	@Autowired
	private LojaRepository lojaRepository;
	
	@Autowired
	private LojaRepositoryImpl lojaImpl;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private EventoRepository eventoRepository;
	
	@Autowired
	private ContatoRepository contatoRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping(value = "/feiracruzeiro", method = RequestMethod.GET)
	public ModelAndView getLojas() {
		
		List<Loja> lojas = lojaImpl.getLojasByTipoLimit("LOJA", 3);
		List<Loja> servicos = lojaImpl.getLojasByTipoLimit("SERVICO", 3);
		List<Loja> restaurantes = lojaImpl.getLojasByTipoLimit("RESTAURANTE", 2);
		List<Evento> eventos = eventoRepository.findAll();
		
		ModelAndView mv = new ModelAndView("feiraCruzeiro.html");
		mv.addObject("lojas", lojas);
		mv.addObject("restaurantes", restaurantes);
		mv.addObject("servicos", servicos);
		mv.addObject("eventos", eventos);
		return mv;
	}
	
	@RequestMapping(value = "/lojas/{id}", method = RequestMethod.GET)
	public ModelAndView getLojaDetalhe(@PathVariable("id") long id) {
		Loja loja = lojaRepository.findById(id).get();
		ModelAndView mv = new ModelAndView("lojaDetalhe.html");
		mv.addObject("loja", loja);
		mv.addObject("titulo", "Detalhes");
		return mv;
	}
	
	@RequestMapping(value = "/lojas", method = RequestMethod.GET)
	public ModelAndView getListagemLojas() {
		List<Loja> lojas = lojaImpl.getLojasByTipo("LOJA");
		ModelAndView mv = new ModelAndView("lojas.html");
		mv.addObject("lojas", lojas);
		mv.addObject("titulo", "Lojas");
		return mv;
	}
	
	@RequestMapping(value = "/servicos", method = RequestMethod.GET)
	public ModelAndView getListagemServicos() {
		List<Loja> lojas = lojaImpl.getLojasByTipo("SERVICO");
		ModelAndView mv = new ModelAndView("lojas.html");
		mv.addObject("lojas", lojas);
		mv.addObject("titulo", "Serviços");
		return mv;
	}
	
	@RequestMapping(value = "/restaurantes", method = RequestMethod.GET)
	public ModelAndView getListagemRestaurantes() {
		List<Loja> lojas = lojaImpl.getLojasByTipo("RESTAURANTE");
		ModelAndView mv = new ModelAndView("lojas.html");
		mv.addObject("lojas", lojas);
		mv.addObject("titulo", "Restaurantes");
		return mv;
	}
	
	@RequestMapping(value = "/lojaform", method = RequestMethod.GET)
	public ModelAndView getLojaForm() {
		
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findById(userName).get();
		Pessoa pessoa = pessoaRepository.findByUser(userName);
		List<Loja> lojas = lojaRepository.findAll();
		List<Loja> lojasPessoa = lojaRepository.findByPessoa(pessoa.getId());
		List<Evento> eventos = eventoRepository.findEventosByUser(user.getUsername());

		ModelAndView mv = new ModelAndView("feiraForm.html");
		mv.addObject("lojas", lojas);
		mv.addObject("lojasPessoa", lojasPessoa);
		mv.addObject("pessoa", pessoa);
		mv.addObject("eventos", eventos);
		return mv;
	}
	
	@RequestMapping(value = "/newloja", method = RequestMethod.POST)
	public String saveLoja(@Valid Loja loja,  @RequestParam("imagem") MultipartFile multipartFile, 
							BindingResult result, RedirectAttributes redirect) throws IOException {
		if(result.hasErrors()) {
			redirect.addFlashAttribute("message", "Verifique os campos obrigatórios.");
			return "redirect:/lojaform";
		}
		
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		Pessoa pessoa = pessoaRepository.findByUser(userName);
		loja.setPessoa(pessoa);
		loja.setArquivo(multipartFile.getOriginalFilename());
		
		contatoRepository.save(loja.getContato());
		lojaRepository.save(loja);
		FileUploadUtil.createFile(loja, null, multipartFile);
		
		redirect.addFlashAttribute("messageSuccess", "Loja cadastrada com sucesso.");
		return "redirect:/lojaform"; 
	}
	
	@RequestMapping(value = "/newPessoa", method = RequestMethod.POST)
	public String savePessoa(@Valid @ModelAttribute Pessoa pessoa, BindingResult result, RedirectAttributes redirect, @RequestParam("username") String username) {
		if(result.hasErrors()) {
			redirect.addFlashAttribute("message", "Verifique os campos obrigatórios.");
			return "redirect:/lojaForm";
		} else {
			Optional<User> savedUser = userRepository.findById(username);
			if(savedUser.isPresent()) {
				redirect.addFlashAttribute("message", "Essa pessoa já foi cadastrada");
				return "redirect:/lojaform";
			}
		}
		
		User user = new User();
		user.setUsername(username);
		user.setPassword(pessoa.getSenha());
		user.setEnabled(true);
		userRepository.save(user);
		pessoa.setUser(user);
		pessoaRepository.save(pessoa);
		redirect.addFlashAttribute("messageSuccess", "Pessoa cadastrada com sucesso.");
		
		return "redirect:/lojaform"; 
	}
	
	@RequestMapping(value = "/formlojaupdate/{id}", method = RequestMethod.GET)
	public ModelAndView getLojaFormUpdt(@PathVariable("id") long id ) {
		Loja loja = lojaRepository.findById(id).get();
		ModelAndView mv = new ModelAndView("formLoja.html");
		mv.addObject("loja", loja);
		return mv;
	}
	
	@RequestMapping(value = "/atualizaloja", method = RequestMethod.POST)
	public String atualizarLoja(@Valid Loja loja, @RequestParam("imagem") MultipartFile multipartFile, 
							BindingResult result, RedirectAttributes redirect) throws IOException {
		if(result.hasErrors()) {
			redirect.addFlashAttribute("message", "Verifique os campos obrigatórios.");
			return "redirect:/lojaform";
		}
		
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		Pessoa pessoa = pessoaRepository.findByUser(userName);
		loja.setPessoa(pessoa);
		
		if(!multipartFile.getOriginalFilename().isEmpty()) {
			FileUploadUtil.removerFile(loja);
			loja.setArquivo(multipartFile.getOriginalFilename());
			FileUploadUtil.createFile(loja, null, multipartFile);
		}

		contatoRepository.save(loja.getContato());
		lojaRepository.save(loja);
		
		redirect.addFlashAttribute("messageSuccess", "Loja alterada com sucesso.");
		return "redirect:/lojaform"; 
	}
	
	@RequestMapping(value = "/removerloja/{id}", method = RequestMethod.GET)
	public String removerLoja(@PathVariable("id") long id,  RedirectAttributes redirect) {
		Loja loja = lojaRepository.findById(id).get();
		FileUploadUtil.removerFile(loja);
		contatoRepository.delete(loja.getContato());
		lojaRepository.delete(loja);
		redirect.addFlashAttribute("messageSuccess", "Loja removida com sucesso.");
		
		return "redirect:/lojaform";
	}
	
	@RequestMapping(value = "/buscalojas", method = RequestMethod.GET)
	public ModelAndView buscalojas(@RequestParam("param") String param, RedirectAttributes redirect) {
		
		if(param.isEmpty()) {
			redirect.addFlashAttribute("message", "Busca sem resultados.");
			return getLojas();
		}
			
		List<Loja> lojas = lojaImpl.getLojasByParam(param);
		if(lojas.isEmpty()) {
			redirect.addFlashAttribute("message", "Busca sem resultados.");
			return getLojas();
		}
		
		ModelAndView mv = new ModelAndView("lojas.html");
		mv.addObject("lojas", lojas);
		
		return mv;
	}
}
