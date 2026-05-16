package com.forum.service;

import com.forum.dto.*;
import com.forum.entity.AttributeList;
import com.forum.entity.Comment;
import com.forum.entity.Topic;
import com.forum.repository.AttributeListRepository;
import com.forum.repository.CommentRepository;
import com.forum.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final AttributeListRepository attributeListRepository;
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public List<TopicResponse> getAllTopics() {
        return topicRepository.findAll().stream()
                .map(this::toTopicResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public TopicResponse createTopic(CreateTopicRequest request) {
        Topic topic = new Topic();
        topic.setUserId(request.getUserId());
        topic.setTopicName(request.getTopicName());
        topic.setInitialMessage(request.getInitialMessage());
        return toTopicResponse(topicRepository.save(topic));
    }

    @Transactional(readOnly = true)
    public List<AttributeResponse> getAttributesByTopic(UUID topicId) {
        if (!topicRepository.existsById(topicId)) {
            throw new RuntimeException("Topic not found: " + topicId);
        }
        return attributeListRepository.findByTopicId(topicId).stream()
                .map(this::toAttributeResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AttributeResponse createAttribute(UUID topicId, CreateAttributeRequest request) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Topic not found: " + topicId));

        AttributeList attribute = new AttributeList();
        attribute.setTopic(topic);
        attribute.setAttr(request.getAttr());
        attribute.setValue(request.getValue());
        return toAttributeResponse(attributeListRepository.save(attribute));
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByTopic(UUID topicId) {
        if (!topicRepository.existsById(topicId)) {
            throw new RuntimeException("Topic not found: " + topicId);
        }
        return commentRepository.findByTopicId(topicId).stream()
                .map(this::toCommentResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponse createComment(UUID topicId, CreateCommentRequest request) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Topic not found: " + topicId));

        Comment comment = new Comment();
        comment.setTopic(topic);
        comment.setUserId(request.getUserId());
        comment.setComment(request.getComment());
        return toCommentResponse(commentRepository.save(comment));
    }

    private TopicResponse toTopicResponse(Topic topic) {
        return new TopicResponse(
                topic.getId(),
                topic.getUserId(),
                topic.getTopicName(),
                topic.getInitialMessage()
        );
    }

    private AttributeResponse toAttributeResponse(AttributeList attribute) {
        return new AttributeResponse(
                attribute.getId(),
                attribute.getTopicId(),
                attribute.getAttr(),
                attribute.getValue()
        );
    }

    private CommentResponse toCommentResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getTopicId(),
                comment.getUserId(),
                comment.getComment()
        );
    }
}
