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
        request.setFromPlateNumber("34ABC123");

        when(subthreadRepository.findById(subthreadId)).thenReturn(Optional.of(testSubthread));
        when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Message result = forumService.createMessage(subthreadId, request);

        // Then
        assertNotNull(result);
        assertEquals("test-user", result.getUserId());
        assertEquals("This is a test message", result.getBody());
        assertEquals("34ABC123", result.getFromPlateNumber());
        assertNotNull(result.getCreatedAt());
        assertEquals(0, result.getUpvoteCount());
        assertEquals(false, result.getDeleted());
        assertEquals(testSubthread, result.getSubthread());

        // Verify interactions
        verify(subthreadRepository, times(1)).findById(subthreadId);
        verify(messageRepository, times(1)).save(any(Message.class));

        // Verify the message saved has the correct fields
        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(messageRepository).save(messageCaptor.capture());
        Message savedMessage = messageCaptor.getValue();
        assertEquals("34ABC123", savedMessage.getFromPlateNumber());
    }

    @Test
    void testCreateMessage_SubthreadNotFound() {
        // Given
        UUID subthreadId = UUID.randomUUID();
        CreateMessageRequest request = new CreateMessageRequest();
        request.setUserId("test-user");
        request.setBody("This is a test message");
        request.setFromPlateNumber("34ABC123");

        when(subthreadRepository.findById(subthreadId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            forumService.createMessage(subthreadId, request);
        });

        verify(subthreadRepository, times(1)).findById(subthreadId);
        verify(messageRepository, never()).save(any(Message.class));
    }

    @Test
    void testCreateMessage_WithDifferentPlateNumbers() {
        // Given
        UUID subthreadId = testSubthread.getId();
        String[] plateNumbers = {"34ABC123", "06XYZ789", "35DEF456", "01GHI789"};

        when(subthreadRepository.findById(subthreadId)).thenReturn(Optional.of(testSubthread));
        when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When & Then - Test multiple plate numbers
        for (String plateNumber : plateNumbers) {
            CreateMessageRequest request = new CreateMessageRequest();
            request.setUserId("test-user");
            request.setBody("Message from " + plateNumber);
            request.setFromPlateNumber(plateNumber);

            Message result = forumService.createMessage(subthreadId, request);

            assertNotNull(result);
            assertEquals(plateNumber, result.getFromPlateNumber());
            assertEquals("Message from " + plateNumber, result.getBody());
        }

        // Verify save was called for each message
        verify(messageRepository, times(plateNumbers.length)).save(any(Message.class));
    }
}
