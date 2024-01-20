package com.example.mintos.backend.controllers;

import com.example.mintos.backend.models.requests.ClientCreateRequestDto;
import com.example.mintos.backend.models.responses.ClientResponseDto;
import com.example.mintos.backend.services.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(ClientController.class)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @Test
    public void testGetClients() throws Exception {

        Page<ClientResponseDto> page = getClientResponsePage();

        Mockito.when(clientService.getClients(any(), any()))
               .thenReturn(page);

        mockMvc.perform(get("/client")
                                .param("page", "0")
                                .param("size", "5"))
               .andExpect(jsonPath("$.totalElements", is(1)))
               .andExpect(jsonPath("$.totalPages", is(1)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content", hasSize(page.getContent().size())))
               .andExpect(jsonPath("$.content[0].name", is("NAME")))
               .andExpect(jsonPath("$.content[0].id", is(1)))
               .andDo(print());

        verify(clientService, times(1)).getClients(0, 5);
    }

    @Test
    public void testGetClient() throws Exception {
        Long clientId = 1L;
        ClientResponseDto client = new ClientResponseDto() {{setId(clientId);}};
        Mockito.when(clientService.getClient(clientId)).thenReturn(client);

        mockMvc.perform(get("/client/id")
                                .param("id", clientId.toString()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is(1)))
               .andDo(print());
        verify(clientService, times(1)).getClient(any(Long.class));
    }

    @Test
    public void testRegisterClient() throws Exception {
        ClientCreateRequestDto requestDto = new ClientCreateRequestDto() {{ setName("NAME"); }};
        ClientResponseDto responseDto = new ClientResponseDto() {{ setName("NAME"); }};

        Mockito.when(clientService.registerClient(any(ClientCreateRequestDto.class)))
               .thenReturn(responseDto);

        mockMvc.perform(post("/client")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(requestDto)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name", is("NAME")))
               .andDo(print());

        verify(clientService, times(1)).registerClient(any(ClientCreateRequestDto.class));
    }

    private Page<ClientResponseDto> getClientResponsePage() {

        List<ClientResponseDto> list = new ArrayList<>();
        list.add(new ClientResponseDto(){{
            setId(1L);
            setName("NAME");
        }});
        return new SerializablePageMock<>(list);
    }

}
