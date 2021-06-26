package com.feira.cruzeiro.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.feira.cruzeiro.model.Loja;
import com.feira.cruzeiro.repository.LojaRepository;

@Service
public class LojaResource {

	@Autowired
	private LojaRepository repository;
	
	@RequestMapping(value = "/lojas", method = RequestMethod.GET)
	public ModelAndView getLojas() {
		List<Loja> lojas = repository.findAll();

		ModelAndView mv = new ModelAndView("lojas.html");
		mv.addObject(lojas);
		return mv;
	}
}
