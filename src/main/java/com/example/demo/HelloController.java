package com.example.demo;

import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public HelloResponse hello(){
        return new HelloResponse("Hello , World");
    }

    @GetMapping("/hello/{name}")
    public HelloResponse helloWithName(@PathVariable  String name){
        return new HelloResponse("Hello , World " + name);
    }

    @PostMapping("/hello")
    public HelloResponse helloPost(@RequestBody String name){
        return new HelloResponse("Hello ,  " + name + "!");
    }

}
