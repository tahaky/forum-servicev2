package com.forum.controller;

import com.forum.dto.*;
import com.forum.service.TopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @GetMapping
    public ResponseEntity<List<TopicResponse>> getAllTopics() {
        return ResponseEntity.ok(topicService.getAllTopics());
    }

    @PostMapping
    public ResponseEntity<TopicResponse> createTopic(@Valid @RequestBody CreateTopicRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(topicService.createTopic(request));
    }

    @GetMapping("/{topicId}/attributes")
    public ResponseEntity<List<AttributeResponse>> getAttributes(@PathVariable UUID topicId) {
        return ResponseEntity.ok(topicService.getAttributesByTopic(topicId));
    }

    @PostMapping("/{topicId}/attributes")
    public ResponseEntity<AttributeResponse> createAttribute(
            @PathVariable UUID topicId,
            @Valid @RequestBody CreateAttributeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(topicService.createAttribute(topicId, request));
    }

    @GetMapping("/{topicId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable UUID topicId) {
        return ResponseEntity.ok(topicService.getCommentsByTopic(topicId));
    }

    @PostMapping("/{topicId}/comments")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable UUID topicId,
            @Valid @RequestBody CreateCommentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(topicService.createComment(topicId, request));
    }
}
