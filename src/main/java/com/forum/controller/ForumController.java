package com.forum.controller;

import com.forum.dto.CreateThreadRequest;
import com.forum.dto.CreateSubthreadRequest;
import com.forum.dto.CreateMessageRequest;
import com.forum.dto.VoteRequest;
import com.forum.dto.MessageResponse;
import com.forum.dto.ThreadResponse;
import com.forum.entity.Message;
import com.forum.entity.MessageVote;
import com.forum.entity.Subthread;
import com.forum.service.ForumService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class ForumController {
    
    private final ForumService forumService;
    
    @PostMapping("/threads")
    public ResponseEntity<com.forum.entity.Thread> createThread(@RequestBody CreateThreadRequest request) {
        com.forum.entity.Thread thread = forumService.createThread(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(thread);
    }
    
    @PostMapping("/threads/{threadId}/subthreads")
    public ResponseEntity<Subthread> createSubthread(
            @PathVariable UUID threadId,
            @RequestBody CreateSubthreadRequest request) {
        Subthread subthread = forumService.createSubthread(threadId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(subthread);
    }
    
    @PostMapping("/subthreads/{subthreadId}/messages")
    public ResponseEntity<Message> createMessage(
            @PathVariable UUID subthreadId,
            @RequestBody CreateMessageRequest request) {
        Message message = forumService.createMessage(subthreadId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }
    
    @PostMapping("/messages/{messageId}/vote")
    public ResponseEntity<MessageVote> voteMessage(
            @PathVariable UUID messageId,
            @RequestBody VoteRequest request) {
        MessageVote vote = forumService.voteMessage(messageId, request);
        return ResponseEntity.ok(vote);
    }
    
    @GetMapping("/subthreads/{subthreadId}/messages")
    public ResponseEntity<List<MessageResponse>> getMessagesBySubthread(
            @PathVariable UUID subthreadId) {
        List<MessageResponse> messages = forumService.getMessagesBySubthread(subthreadId);
        return ResponseEntity.ok(messages);
    }
    
    @GetMapping("/models/{modelId}/messages")
    public ResponseEntity<List<MessageResponse>> getMessagesByModel(
            @PathVariable String modelId) {
        List<MessageResponse> messages = forumService.getMessagesByModelId(modelId);
        return ResponseEntity.ok(messages);
    }
    
    @GetMapping("/threads")
    public ResponseEntity<Page<ThreadResponse>> getAllThreads(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        Page<ThreadResponse> threads = forumService.getAllThreads(page, size);
        return ResponseEntity.ok(threads);
    }
    
    @GetMapping("/threads/recent")
    public ResponseEntity<List<ThreadResponse>> getRecentThreads(
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int limit) {
        List<ThreadResponse> threads = forumService.getRecentThreads(limit);
        return ResponseEntity.ok(threads);
    }
}
