package com.covid.minus.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController {
	
	@GetMapping("/categories")
	public String getAllCategories(){
		return "[{ \"id\": \"1\", \"title\": \"Ambulance\", \"icon\": \"ambulance\" }, { \"id\": \"2\", \"title\": \"Medicines\", \"icon\": \"medical-bag\" }, { \"id\": \"3\", \"title\": \"Oxygen\", \"icon\": \"gas-cylinder\" }, { \"id\": \"4\", \"title\": \"Corona Stab Test\", \"icon\": \"bug-check\" }, { \"id\": \"5\", \"title\": \"Hospital Beds\", \"icon\": \"bed-empty\" }, { \"id\": \"6\", \"title\": \"Vaccine\", \"icon\": \"shield-check\" }, { \"id\": \"7\", \"title\": \"Others\", \"icon\": \"comment-search-outline\" }]";
	}
}
