package com.example.mintos.backend.controllers;

import com.example.mintos.backend.models.responses.ClientResponseDto;
import com.example.mintos.backend.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    ResponseEntity<Page<ClientResponseDto>> getClients(@RequestParam(required = false) Integer page,
                                                       @RequestParam(required = false) Integer size)
    {
        return ResponseEntity.ok(clientService.getClients(page, size));
    }

    @PostMapping
    ResponseEntity<ClientResponseDto> registerClient(@RequestBody Map<String, String> name) {
        return ResponseEntity.ok(clientService.registerClient(name));
    }
}
