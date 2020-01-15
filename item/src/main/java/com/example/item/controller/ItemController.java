package com.example.item.controller;

import com.example.item.model.Item;
import com.example.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("items")
public class ItemController {
    @Autowired
    private ItemService itemservice;

    @PostMapping(value="addItem", produces = "application/json")
    public Item add(@RequestBody Item item){
        return this.itemservice.add(item);
    }

    @GetMapping(value="getAllItems", produces="application/json")
    public List<Item> getAll() {
        List<Item> all = itemservice.getAllItems();
        return all;
    }
    @GetMapping(value="getItemByName/{name}", produces= "application/json")
    public List<Item> getItemByName(@PathVariable("name") String name){
        return this.itemservice.getItemByName(name);
    }

}
