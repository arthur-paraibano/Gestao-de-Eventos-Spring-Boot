package com.ingressos.api.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import com.ingressos.api.dto.GeneralDto;
import com.ingressos.api.exceptions.InternalServer;
import com.ingressos.api.infra.exception.RestMessage;
import com.ingressos.api.model.TicketLotModel;
import com.ingressos.api.service.TicketLotService;
import com.ingressos.api.util.InternationalizationUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/ticket-lots", produces = { "application/json" })
@Tag(name = "Ticket Lot API", description = "Ticket Lot Controller")
public class TicketLotController {

    @Autowired
    private TicketLotService service;
    @Autowired
    private InternationalizationUtil messageInternationalization;

    @GetMapping("/all")
    @Operation(summary = "Find all Addresses", description = "Find all Addresses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RestMessage.class)) }),
            @ApiResponse(responseCode = "404", description = "Address not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RestMessage.class)) })
    })
    public ResponseEntity<List<TicketLotModel>> findAll() {
        try {
            List<TicketLotModel> EquipamentModels = service.findAll();
            for (TicketLotModel uModel : EquipamentModels) {
                Integer id = uModel.getId();
                GeneralDto dtoId = new GeneralDto(id);
                uModel.add(linkTo(methodOn(TicketLotController.class).getById(dtoId)).withSelfRel());
            }
            return ResponseEntity.status(HttpStatus.OK).body(EquipamentModels);
        } catch (InternalServerError e) {
            throw new InternalServer(e.getMessage());
        } catch (Exception e) {
            throw new InternalServer(e.getMessage());
        }
    }

    @PostMapping("/id")
    @Operation(summary = "Find address by id", description = "Find address by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RestMessage.class)) }),
            @ApiResponse(responseCode = "204", description = "Address not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RestMessage.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RestMessage.class)) })
    })
    public ResponseEntity<TicketLotModel> getById(@Valid @RequestBody GeneralDto id) {
        try {
            TicketLotModel uModel = service.findById(id);
            uModel.add(linkTo(methodOn(TicketLotController.class).findAll()).withRel("findAll"));
            return ResponseEntity.status(HttpStatus.OK).body(uModel);
        } catch (InternalServerError e) {
            throw new InternalServer(e.getMessage());
        } catch (Exception e) {
            throw new InternalServer(e.getMessage());
        }
    }
}
