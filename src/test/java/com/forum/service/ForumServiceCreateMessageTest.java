package com.forum.service;

import com.forum.dto.CreateMessageRequest;
import com.forum.entity.Message;
import com.forum.entity.Subthread;
import com.forum.entity.Thread;
import com.forum.repository.MessageRepository;
import com.forum.repository.SubthreadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ForumServiceCreateMessageTest {

    @Mock
    private SubthreadRepository subthreadRepository;

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private ForumService forumService;

    private Subthread testSubthread;
    private Thread testThread;

    @BeforeEach
    void setUp() {
        testThread = new Thread();
        testThread.setId(UUID.randomUUID());
        testThread.setUserId("user123");
        testThread.setType("car-discussion");
        testThread.setModelId("bmw-m3");
        testThread.setTitle("BMW M3 Discussion");
        testThread.setCreatedAt(LocalDateTime.now());

        testSubthread = new Subthread();
        testSubthread.setId(UUID.randomUUID());
        testSubthread.setUserId("user456");
        testSubthread.setTitle("Engine Performance");
        testSubthread.setCreatedAt(LocalDateTime.now());
        testSubthread.setThread(testThread);
    }

    @Test
    void testCreateMessage_Success() {
        // Given
        UUID subthreadId = testSubthread.getId();
        CreateMessageRequest request = new CreateMessageRequest();
        request.setUserId("test-user");
        request.setBody("This is a test message");

        when(subthreadRepository.findById(subthreadId)).thenReturn(Optional.of(testSubthread));
        when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Message result = forumService.createMessage(subthreadId, request);

        // Then
        assertNotNull(result);
        assertEquals("test-user", result.getUserId());
        assertEquals("This is a test message", result.getBody());
        assertNotNull(result.getCreatedAt());
        assertEquals(0, result.getUpvoteCount());
        assertEquals(false, result.getDeleted());
        assertEquals(testSubthread, result.getSubthread());

        // Verify interactions
        verify(subthreadRepository, times(1)).findById(subthreadId);
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void testCreateMessage_SubthreadNotFound() {
        // Given
        UUID subthreadId = UUID.randomUUID();
        CreateMessageRequest request = new CreateMessageRequest();
        request.setUserId("test-user");
        request.setBody("This is a test message");

        when(subthreadRepository.findById(subthreadId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            forumService.createMessage(subthreadId, request);
        });

        verify(subthreadRepository, times(1)).findById(subthreadId);
        verify(messageRepository, never()).save(any(Message.class));
    }

    @Test
    void testCreateMessage_WithMultipleMessages() {
        // Given
        UUID subthreadId = testSubthread.getId();
        int messageCount = 4;

        when(subthreadRepository.findById(subthreadId)).thenReturn(Optional.of(testSubthread));
        when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When & Then - Test multiple messages
        for (int i = 0; i < messageCount; i++) {
            CreateMessageRequest request = new CreateMessageRequest();
            request.setUserId("test-user");
            request.setBody("Test message " + (i + 1));

            Message result = forumService.createMessage(subthreadId, request);

            assertNotNull(result);
            assertEquals("Test message " + (i + 1), result.getBody());
        }

        // Verify save was called for each message
        verify(messageRepository, times(messageCount)).save(any(Message.class));
    }
}
