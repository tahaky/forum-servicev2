package com.forum.service;

import com.forum.dto.CreateThreadRequest;
import com.forum.dto.CreateSubthreadRequest;
import com.forum.dto.CreateMessageRequest;
import com.forum.dto.VoteRequest;
import com.forum.dto.MessageResponse;
import com.forum.dto.ThreadResponse;
import com.forum.entity.Message;
import com.forum.entity.MessageVote;
import com.forum.entity.MessageVoteId;
import com.forum.entity.Subthread;
import com.forum.repository.ThreadRepository;
import com.forum.repository.SubthreadRepository;
import com.forum.repository.MessageRepository;
import com.forum.repository.MessageVoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ForumService {
    
    private final ThreadRepository threadRepository;
    private final SubthreadRepository subthreadRepository;
    private final MessageRepository messageRepository;
    private final MessageVoteRepository messageVoteRepository;
    
    @Transactional
    public com.forum.entity.Thread createThread(CreateThreadRequest request) {
        com.forum.entity.Thread thread = new com.forum.entity.Thread();
        thread.setUserId(request.getUserId());
        thread.setType(request.getType());
        thread.setModelId(request.getModelId());
        thread.setTitle(request.getTitle());
        thread.setCreatedAt(LocalDateTime.now());
        return threadRepository.save(thread);
    }
    
    @Transactional
    public Subthread createSubthread(UUID threadId, CreateSubthreadRequest request) {
        com.forum.entity.Thread thread = threadRepository.findById(threadId)
            .orElseThrow(() -> new RuntimeException("Thread not found"));
        
        Subthread subthread = new Subthread();
        subthread.setUserId(request.getUserId());
        subthread.setTitle(request.getTitle());
        subthread.setCreatedAt(LocalDateTime.now());
        subthread.setThread(thread);
        return subthreadRepository.save(subthread);
    }
    
    @Transactional
    public Message createMessage(UUID subthreadId, CreateMessageRequest request) {
        Subthread subthread = subthreadRepository.findById(subthreadId)
            .orElseThrow(() -> new RuntimeException("Subthread not found"));
        
        Message message = new Message();
        message.setUserId(request.getUserId());
        message.setBody(request.getBody());
        message.setCreatedAt(LocalDateTime.now());
        message.setUpvoteCount(0);
        message.setDeleted(false);
        message.setSubthread(subthread);
        return messageRepository.save(message);
    }
    
    @Transactional
    public MessageVote voteMessage(UUID messageId, VoteRequest request) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new RuntimeException("Message not found"));
        
        MessageVoteId voteId = new MessageVoteId(messageId, request.getUserId());
        MessageVote vote = messageVoteRepository.findById(voteId)
            .orElse(null);
        
        boolean isNewVote = (vote == null);
        Boolean oldUpvoted = isNewVote ? null : vote.getUpvoted();
        
        if (isNewVote) {
            vote = new MessageVote();
            vote.setId(voteId);
            vote.setCreatedAt(LocalDateTime.now());
        } else {
            vote.setUpdatedAt(LocalDateTime.now());
        }
        
        vote.setUpvoted(request.getUpvoted());
        vote.setMessage(message);
        
        // Update message upvote count
        if (isNewVote && request.getUpvoted()) {
            message.setUpvoteCount(message.getUpvoteCount() + 1);
        } else if (!isNewVote && !oldUpvoted.equals(request.getUpvoted())) {
            if (request.getUpvoted()) {
                message.setUpvoteCount(message.getUpvoteCount() + 1);
            } else {
                message.setUpvoteCount(message.getUpvoteCount() - 1);
            }
        }
        
        messageRepository.save(message);
        return messageVoteRepository.save(vote);
    }
    
    @Transactional(readOnly = true)
    public List<MessageResponse> getMessagesBySubthread(UUID subthreadId) {
        List<Message> messages = messageRepository.findBySubthreadId(subthreadId);
        return messages.stream()
            .map(this::toMessageResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<MessageResponse> getMessagesByModelId(String modelId) {
        com.forum.entity.Thread thread = threadRepository.findByModelId(modelId)
            .orElseThrow(() -> new RuntimeException("Thread not found for modelId: " + modelId));
        
        List<Message> messages = messageRepository.findByThreadId(thread.getId());
        return messages.stream()
            .map(this::toMessageResponse)
            .collect(Collectors.toList());
    }
    
    private MessageResponse toMessageResponse(Message message) {
        return new MessageResponse(
            message.getId(),
            message.getUserId(),
            message.getBody(),
            message.getCreatedAt(),
            message.getUpvoteCount(),
            message.getDeleted(),
            message.getUpdatedAt(),
            message.getSubthread().getId()
        );
    }
    
    private ThreadResponse toThreadResponse(com.forum.entity.Thread thread) {
        return new ThreadResponse(
            thread.getId(),
            thread.getUserId(),
            thread.getType(),
            thread.getModelId(),
            thread.getTitle(),
            thread.getCreatedAt()
        );
    }
    
    @Transactional(readOnly = true)
    public Page<ThreadResponse> getAllThreads(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return threadRepository.findAll(pageable)
            .map(this::toThreadResponse);
    }
    
    @Transactional(readOnly = true)
    public List<ThreadResponse> getRecentThreads(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return threadRepository.findAllByOrderByCreatedAtDesc(pageable).stream()
            .map(this::toThreadResponse)
            .collect(Collectors.toList());
    }
}
