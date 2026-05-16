package com.forum.controller;

import com.forum.dto.CreateThreadRequest;
import com.forum.dto.CreateSubthreadRequest;
import com.forum.dto.CreateMessageRequest;
import com.forum.dto.VoteRequest;
import com.forum.dto.MessageResponse;
import com.forum.dto.ThreadResponse;
import com.forum.dto.SubthreadResponse;
import com.forum.entity.Message;
import com.forum.entity.MessageVote;
import com.forum.entity.ForumThread;
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
    public ResponseEntity<ForumThread> createThread(@RequestBody CreateThreadRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(forumService.createThread(request));
    }

    @PostMapping("/threads/{threadId}/subthreads")
    public ResponseEntity<Subthread> createSubthread(
            @PathVariable UUID threadId,
            @RequestBody CreateSubthreadRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(forumService.createSubthread(threadId, request));
    }

    @PostMapping("/subthreads/{subthreadId}/messages")
    public ResponseEntity<Message> createMessage(
            @PathVariable UUID subthreadId,
            @RequestBody CreateMessageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(forumService.createMessage(subthreadId, request));
    }

    @PostMapping("/messages/{messageId}/vote")
    public ResponseEntity<MessageVote> voteMessage(
            @PathVariable UUID messageId,
            @RequestBody VoteRequest request) {
        return ResponseEntity.ok(forumService.voteMessage(messageId, request));
    }

    @GetMapping("/subthreads/{subthreadId}/messages")
    public ResponseEntity<List<MessageResponse>> getMessagesBySubthread(
            @PathVariable UUID subthreadId) {
        return ResponseEntity.ok(forumService.getMessagesBySubthread(subthreadId));
    }

    @GetMapping("/threads")
    public ResponseEntity<Page<ThreadResponse>> getAllThreads(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        return ResponseEntity.ok(forumService.getAllThreads(page, size));
    }

    @GetMapping("/threads/recent")
    public ResponseEntity<List<ThreadResponse>> getRecentThreads(
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int limit) {
        return ResponseEntity.ok(forumService.getRecentThreads(limit));
    }

    @GetMapping("/subthreads")
    public ResponseEntity<Page<SubthreadResponse>> getAllSubthreads(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        return ResponseEntity.ok(forumService.getAllSubthreads(page, size));
    }

    @GetMapping("/subthreads/recent")
    public ResponseEntity<List<SubthreadResponse>> getRecentSubthreads(
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int limit) {
        return ResponseEntity.ok(forumService.getRecentSubthreads(limit));
    }

    @GetMapping("/threads/{threadId}/subthreads")
    public ResponseEntity<List<SubthreadResponse>> getSubthreadsByThread(
            @PathVariable UUID threadId,
            @RequestParam(defaultValue = "false") boolean includeMessages) {
        List<SubthreadResponse> subthreads = includeMessages
                ? forumService.getSubthreadsByThreadWithMessages(threadId)
                : forumService.getSubthreadsByThread(threadId);
        return ResponseEntity.ok(subthreads);
    }
}
