package com.forum.service;

import com.forum.dto.MessageResponse;
import com.forum.dto.SubthreadResponse;
import com.forum.entity.Message;
import com.forum.entity.Subthread;
import com.forum.entity.Thread;
import com.forum.repository.SubthreadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ForumServiceSubthreadWithMessagesTest {

    @Mock
    private SubthreadRepository subthreadRepository;

    @InjectMocks
    private ForumService forumService;

    private Thread testThread;
    private Subthread subthread1;
    private Subthread subthread2;
    private Message message1;
    private Message message2;
    private Message message3;

    @BeforeEach
    void setUp() {
        testThread = new Thread();
        testThread.setId(UUID.randomUUID());
        testThread.setUserId("user123");
        testThread.setType("discussion");
        testThread.setModelId("model456");
        testThread.setTitle("Test Thread");
        testThread.setCreatedAt(LocalDateTime.now());

        subthread1 = new Subthread();
        subthread1.setId(UUID.randomUUID());
        subthread1.setUserId("user456");
        subthread1.setTitle("Subthread 1");
        subthread1.setCreatedAt(LocalDateTime.now().minusHours(2));
        subthread1.setThread(testThread);
        subthread1.setMessages(new ArrayList<>());

        subthread2 = new Subthread();
        subthread2.setId(UUID.randomUUID());
        subthread2.setUserId("user789");
        subthread2.setTitle("Subthread 2");
        subthread2.setCreatedAt(LocalDateTime.now().minusHours(1));
        subthread2.setThread(testThread);
        subthread2.setMessages(new ArrayList<>());

        // Create messages for subthread1
        message1 = new Message();
        message1.setId(UUID.randomUUID());
        message1.setUserId("user001");
        message1.setBody("This is message 1");
        message1.setCreatedAt(LocalDateTime.now().minusMinutes(30));
        message1.setUpvoteCount(5);
        message1.setDeleted(false);
        message1.setSubthread(subthread1);

        message2 = new Message();
        message2.setId(UUID.randomUUID());
        message2.setUserId("user002");
        message2.setBody("This is message 2");
        message2.setCreatedAt(LocalDateTime.now().minusMinutes(20));
        message2.setUpvoteCount(3);
        message2.setDeleted(false);
        message2.setSubthread(subthread1);

        // Create message for subthread2
        message3 = new Message();
        message3.setId(UUID.randomUUID());
        message3.setUserId("user003");
        message3.setBody("This is message 3");
        message3.setCreatedAt(LocalDateTime.now().minusMinutes(10));
        message3.setUpvoteCount(7);
        message3.setDeleted(false);
        message3.setSubthread(subthread2);

        // Add messages to subthreads
        subthread1.getMessages().addAll(Arrays.asList(message1, message2));
        subthread2.getMessages().add(message3);
    }

    @Test
    void testGetSubthreadsByThreadWithMessages() {
        // Given
        UUID threadId = testThread.getId();
        List<Subthread> subthreads = Arrays.asList(subthread1, subthread2);
        when(subthreadRepository.findByThreadId(threadId)).thenReturn(subthreads);

        // When
        List<SubthreadResponse> result = forumService.getSubthreadsByThreadWithMessages(threadId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        
        // Verify first subthread
        SubthreadResponse response1 = result.get(0);
        assertEquals("Subthread 1", response1.getTitle());
        assertEquals(threadId, response1.getThreadId());
        assertNotNull(response1.getMessages());
        assertEquals(2, response1.getMessages().size());
        
        // Verify messages in first subthread
        List<MessageResponse> messages1 = response1.getMessages();
        assertEquals("This is message 1", messages1.get(0).getBody());
        assertEquals(5, messages1.get(0).getUpvoteCount());
        assertEquals("This is message 2", messages1.get(1).getBody());
        assertEquals(3, messages1.get(1).getUpvoteCount());
        
        // Verify second subthread
        SubthreadResponse response2 = result.get(1);
        assertEquals("Subthread 2", response2.getTitle());
        assertEquals(threadId, response2.getThreadId());
        assertNotNull(response2.getMessages());
        assertEquals(1, response2.getMessages().size());
        
        // Verify message in second subthread
        MessageResponse message = response2.getMessages().get(0);
        assertEquals("This is message 3", message.getBody());
        assertEquals(7, message.getUpvoteCount());
        
        verify(subthreadRepository, times(1)).findByThreadId(threadId);
    }

    @Test
    void testGetSubthreadsByThreadWithMessages_EmptyMessages() {
        // Given
        UUID threadId = testThread.getId();
        Subthread emptySubthread = new Subthread();
        emptySubthread.setId(UUID.randomUUID());
        emptySubthread.setUserId("user999");
        emptySubthread.setTitle("Empty Subthread");
        emptySubthread.setCreatedAt(LocalDateTime.now());
        emptySubthread.setThread(testThread);
        emptySubthread.setMessages(new ArrayList<>());
        
        List<Subthread> subthreads = Arrays.asList(emptySubthread);
        when(subthreadRepository.findByThreadId(threadId)).thenReturn(subthreads);

        // When
        List<SubthreadResponse> result = forumService.getSubthreadsByThreadWithMessages(threadId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        SubthreadResponse response = result.get(0);
        assertEquals("Empty Subthread", response.getTitle());
        assertNotNull(response.getMessages());
        assertEquals(0, response.getMessages().size());
        
        verify(subthreadRepository, times(1)).findByThreadId(threadId);
    }

    @Test
    void testGetSubthreadsByThread_WithoutMessages() {
        // Given
        UUID threadId = testThread.getId();
        List<Subthread> subthreads = Arrays.asList(subthread1, subthread2);
        when(subthreadRepository.findByThreadId(threadId)).thenReturn(subthreads);

        // When
        List<SubthreadResponse> result = forumService.getSubthreadsByThread(threadId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        
        // Verify messages field is null (not included)
        SubthreadResponse response1 = result.get(0);
        assertEquals("Subthread 1", response1.getTitle());
        assertNull(response1.getMessages());
        
        SubthreadResponse response2 = result.get(1);
        assertEquals("Subthread 2", response2.getTitle());
        assertNull(response2.getMessages());
        
        verify(subthreadRepository, times(1)).findByThreadId(threadId);
    }
}
