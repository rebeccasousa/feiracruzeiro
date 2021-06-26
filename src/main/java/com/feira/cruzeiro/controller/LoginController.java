package com.feira.cruzeiro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
	
	@GetMapping
	@RequestMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping
	@RequestMapping("/logout")
	public String logout() {
		return "feiracruzeiro";
	}

}
