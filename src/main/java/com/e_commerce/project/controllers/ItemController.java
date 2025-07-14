package com.e_commerce.project.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.e_commerce.project.model.persistence.Item;
import com.e_commerce.project.service.ItemService;

@RestController
@RequestMapping("/api/item")
public class ItemController {

	private final ItemService itemService;

	public ItemController(ItemService itemService) {
		this.itemService = itemService;
	}

	@GetMapping
	public ResponseEntity<List<Item>> getAll() {
		return ResponseEntity.ok(itemService.getAllItems());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Item> getById(@PathVariable Long id) {
		return ResponseEntity.of(itemService.getById(id));
	}

	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getByName(@PathVariable String name) {
		List<Item> items = itemService.getByName(name);
		return items.isEmpty()
				? ResponseEntity.notFound().build()
				: ResponseEntity.ok(items);
	}

	@PostMapping
	public ResponseEntity<Item> create(@RequestBody Item item) {
		Item saved = itemService.createItem(item);
		return ResponseEntity.status(201).body(saved);
	}
}
