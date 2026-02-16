package com.forum.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JsonSerializationTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testThreadSerialization() throws Exception {
        Thread thread = new Thread();
        thread.setId(UUID.randomUUID());
        thread.setUserId("user123");
        thread.setType("discussion");
        thread.setModelId("model456");
        thread.setTitle("Test Thread");
        thread.setCreatedAt(LocalDateTime.now());

        // Should not throw StackOverflowError
        String json = objectMapper.writeValueAsString(thread);
        assertNotNull(json);
        assertTrue(json.contains("Test Thread"));
    }

    @Test
    void testSubthreadSerialization() throws Exception {
        Thread thread = new Thread();
        thread.setId(UUID.randomUUID());
        thread.setUserId("user123");
        thread.setType("discussion");
        thread.setModelId("model456");
        thread.setTitle("Test Thread");
        thread.setCreatedAt(LocalDateTime.now());

        Subthread subthread = new Subthread();
        subthread.setId(UUID.randomUUID());
        subthread.setUserId("user456");
        subthread.setTitle("Test Subthread");
        subthread.setCreatedAt(LocalDateTime.now());
        subthread.setThread(thread);

        // Should not throw StackOverflowError
        String json = objectMapper.writeValueAsString(subthread);
        assertNotNull(json);
        assertTrue(json.contains("Test Subthread"));
        // Thread reference should be ignored
        assertFalse(json.contains("Test Thread"));
    }

    @Test
    void testMessageSerialization() throws Exception {
        Thread thread = new Thread();
        thread.setId(UUID.randomUUID());
        thread.setUserId("user123");
        thread.setType("discussion");
        thread.setModelId("model456");
        thread.setTitle("Test Thread");
        thread.setCreatedAt(LocalDateTime.now());

        Subthread subthread = new Subthread();
        subthread.setId(UUID.randomUUID());
        subthread.setUserId("user456");
        subthread.setTitle("Test Subthread");
        subthread.setCreatedAt(LocalDateTime.now());
        subthread.setThread(thread);

        Message message = new Message();
        message.setId(UUID.randomUUID());
        message.setUserId("user789");
        message.setBody("Test message body");
        message.setCreatedAt(LocalDateTime.now());
        message.setUpvoteCount(5);
        message.setDeleted(false);
        message.setSubthread(subthread);

        // Should not throw StackOverflowError
        String json = objectMapper.writeValueAsString(message);
        assertNotNull(json);
        assertTrue(json.contains("Test message body"));
        // Subthread reference should be ignored
        assertFalse(json.contains("Test Subthread"));
    }

    @Test
    void testMessageVoteSerialization() throws Exception {
        Thread thread = new Thread();
        thread.setId(UUID.randomUUID());
        thread.setUserId("user123");
        thread.setType("discussion");
        thread.setModelId("model456");
        thread.setTitle("Test Thread");
        thread.setCreatedAt(LocalDateTime.now());

        Subthread subthread = new Subthread();
        subthread.setId(UUID.randomUUID());
        subthread.setUserId("user456");
        subthread.setTitle("Test Subthread");
        subthread.setCreatedAt(LocalDateTime.now());
        subthread.setThread(thread);

        Message message = new Message();
        UUID messageId = UUID.randomUUID();
        message.setId(messageId);
        message.setUserId("user789");
        message.setBody("Test message body");
        message.setCreatedAt(LocalDateTime.now());
        message.setUpvoteCount(5);
        message.setDeleted(false);
        message.setSubthread(subthread);

        MessageVoteId voteId = new MessageVoteId(messageId, "voter123");
        MessageVote vote = new MessageVote();
        vote.setId(voteId);
        vote.setUpvoted(true);
        vote.setCreatedAt(LocalDateTime.now());
        vote.setMessage(message);

        // Should not throw StackOverflowError
        String json = objectMapper.writeValueAsString(vote);
        assertNotNull(json);
        assertTrue(json.contains("true"));
        // Message reference should be ignored
        assertFalse(json.contains("Test message body"));
    }
}
