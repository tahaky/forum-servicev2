package com.forum.service;

import com.forum.dto.SubthreadResponse;
import com.forum.entity.Subthread;
import com.forum.entity.Thread;
import com.forum.repository.SubthreadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ForumServiceSubthreadListTest {

    @Mock
    private SubthreadRepository subthreadRepository;

    @InjectMocks
    private ForumService forumService;

    private Thread testThread;
    private Subthread subthread1;
    private Subthread subthread2;

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

        subthread2 = new Subthread();
        subthread2.setId(UUID.randomUUID());
        subthread2.setUserId("user789");
        subthread2.setTitle("Subthread 2");
        subthread2.setCreatedAt(LocalDateTime.now().minusHours(1));
        subthread2.setThread(testThread);
    }

    @Test
    void testGetAllSubthreads() {
        // Given
        List<Subthread> subthreads = Arrays.asList(subthread1, subthread2);
        Page<Subthread> subthreadPage = new PageImpl<>(subthreads);
        when(subthreadRepository.findAll(any(Pageable.class))).thenReturn(subthreadPage);

        // When
        Page<SubthreadResponse> result = forumService.getAllSubthreads(0, 10);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("Subthread 1", result.getContent().get(0).getTitle());
        assertEquals("Subthread 2", result.getContent().get(1).getTitle());
        verify(subthreadRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testGetRecentSubthreads() {
        // Given
        List<Subthread> subthreads = Arrays.asList(subthread2, subthread1);
        when(subthreadRepository.findAllByOrderByCreatedAtDesc(any(Pageable.class))).thenReturn(subthreads);

        // When
        List<SubthreadResponse> result = forumService.getRecentSubthreads(10);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Subthread 2", result.get(0).getTitle());
        assertEquals("Subthread 1", result.get(1).getTitle());
        verify(subthreadRepository, times(1)).findAllByOrderByCreatedAtDesc(any(Pageable.class));
    }

    @Test
    void testGetSubthreadsByThread() {
        // Given
        UUID threadId = testThread.getId();
        List<Subthread> subthreads = Arrays.asList(subthread1, subthread2);
        when(subthreadRepository.findByThreadId(threadId)).thenReturn(subthreads);

        // When
        List<SubthreadResponse> result = forumService.getSubthreadsByThread(threadId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Subthread 1", result.get(0).getTitle());
        assertEquals("Subthread 2", result.get(1).getTitle());
        assertEquals(threadId, result.get(0).getThreadId());
        assertEquals(threadId, result.get(1).getThreadId());
        verify(subthreadRepository, times(1)).findByThreadId(threadId);
    }

    @Test
    void testGetRecentSubthreadsWithLimit() {
        // Given
        List<Subthread> subthreads = Arrays.asList(subthread2, subthread1);
        when(subthreadRepository.findAllByOrderByCreatedAtDesc(any(Pageable.class))).thenReturn(subthreads);

        // When
        List<SubthreadResponse> result = forumService.getRecentSubthreads(5);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(subthreadRepository, times(1)).findAllByOrderByCreatedAtDesc(PageRequest.of(0, 5));
    }
}
