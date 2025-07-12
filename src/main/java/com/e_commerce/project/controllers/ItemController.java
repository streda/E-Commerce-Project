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

// package com.e_commerce.project.controllers;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.e_commerce.project.model.persistence.Item;
// import com.e_commerce.project.model.persistence.repositories.ItemRepository;

// @RestController
// @RequestMapping("/api/item")
// public class ItemController {

// @Autowired
// private ItemRepository itemRepository;

// @GetMapping
// public ResponseEntity<List<Item>> getItems() {
// return ResponseEntity.ok(itemRepository.findAll());
// }

// @GetMapping("/{id}")
// public ResponseEntity<Item> getItemById(@PathVariable Long id) {
// return ResponseEntity.of(itemRepository.findById(id));
// }

// @GetMapping("/name/{name}")
// public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
// List<Item> items = itemRepository.findByName(name);
// return items == null || items.isEmpty() ? ResponseEntity.notFound().build()
// : ResponseEntity.ok(items);

// }

// @PostMapping
// public ResponseEntity<Item> createItem(@RequestBody Item item) {
// Item saved = itemRepository.save(item);
// return ResponseEntity.status(HttpStatus.CREATED).body(saved);
// }

// }
