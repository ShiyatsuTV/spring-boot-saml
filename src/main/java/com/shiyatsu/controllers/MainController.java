package com.shiyatsu.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
public class MainController {

	@RequestMapping("/home")
	public String index() {
		return "home";
	}
	
	@RequestMapping("/secured/hello")
	public String hello(@AuthenticationPrincipal Saml2AuthenticatedPrincipal principal, Model model) {
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String user = auth.getName();
		model.addAttribute("name", user);
		return "hello";
	}
	
}
 