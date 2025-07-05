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
import com.ingressos.api.model.EventCategoryMapModel;
import com.ingressos.api.service.EventCategoryMapService;
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
@RequestMapping(value = "/event-category-map", produces = { "application/json" })
@Tag(name = "Event Category Map API", description = "Event Category Map Controller")
public class EventCategoryMapController {

    @Autowired
    private EventCategoryMapService service;
    @Autowired
    private InternationalizationUtil messageInternationalization;

    @GetMapping("/all")
    @Operation(summary = "Find all Event Categories Map", description = "Find all Event Categories Map")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RestMessage.class)) }),
            @ApiResponse(responseCode = "404", description = "Event Category Map not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RestMessage.class)) })
    })
    public ResponseEntity<List<EventCategoryMapModel>> findAll() {
        try {
            List<EventCategoryMapModel> EquipamentModels = service.findAll();
            for (EventCategoryMapModel uModel : EquipamentModels) {
                Integer id = uModel.getId();
                GeneralDto dtoId = new GeneralDto(id);
                uModel.add(linkTo(methodOn(EventCategoryMapController.class).getById(dtoId)).withSelfRel());
            }
            return ResponseEntity.status(HttpStatus.OK).body(EquipamentModels);
        } catch (InternalServerError e) {
            throw new InternalServer(e.getMessage());
        } catch (Exception e) {
            throw new InternalServer(e.getMessage());
        }
    }

    @PostMapping("/id")
    @Operation(summary = "Find event category by map id", description = "Find event category map by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RestMessage.class)) }),
            @ApiResponse(responseCode = "204", description = "Event Category Map not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RestMessage.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RestMessage.class)) })
    })
    public ResponseEntity<EventCategoryMapModel> getById(@Valid @RequestBody GeneralDto id) {
        try {
            EventCategoryMapModel uModel = service.findById(id);
            uModel.add(linkTo(methodOn(EventCategoryMapController.class).findAll()).withRel("findAll"));
            return ResponseEntity.status(HttpStatus.OK).body(uModel);
        } catch (InternalServerError e) {
            throw new InternalServer(e.getMessage());
        } catch (Exception e) {
            throw new InternalServer(e.getMessage());
        }
    }
}
