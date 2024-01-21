package com.example.mintos.backend.controllers;

import com.example.mintos.backend.models.requests.ClientCreateRequestDto;
import com.example.mintos.backend.models.responses.ClientResponseDto;
import com.example.mintos.backend.services.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    @Operation(summary = "Returns all clients and all associated accounts. Optional params page and size. "
                         + "If page or size is null returns first page with 20 results.")
    //For an actual application filtering should be implemented
    ResponseEntity<Page<ClientResponseDto>> getClients(@RequestParam(required = false) @Min(0) Integer page,
                                                       @RequestParam(required = false) @Min(0) Integer size)
    {
        return ResponseEntity.ok(clientService.getClients(page, size));
    }

    @GetMapping("/id")
    @Operation(summary = "Returns client based on id.")
    ResponseEntity<ClientResponseDto> getClient(@RequestParam Long id)
    {
        return ResponseEntity.ok(clientService.getClient(id));
    }

    @PostMapping
    @Operation(summary = "Creates a new client. Only a name is required.")
    ResponseEntity<ClientResponseDto> registerClient(@RequestBody ClientCreateRequestDto clientRequest) {
        return ResponseEntity.ok(clientService.registerClient(clientRequest));
    }
}
