package com.e_commerce.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.e_commerce.project.model.persistence.Item;
import com.e_commerce.project.model.persistence.repositories.ItemRepository;

@Service
public class ItemService {

    private final ItemRepository itemRepo;

    public ItemService(ItemRepository itemRepo) {
        this.itemRepo = itemRepo;
    }

    public List<Item> getAllItems() {
        return itemRepo.findAll();
    }

    public Optional<Item> getById(Long id) {
        return itemRepo.findById(id);
    }

    public List<Item> getByName(String name) {
        return itemRepo.findByName(name);
    }

    public Item createItem(Item item) {
        return itemRepo.save(item);
    }
}