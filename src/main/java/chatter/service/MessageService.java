package com.chatter.service;

import com.chatter.model.Message;
import com.chatter.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getMessagesBetweenUsers(Long senderId, Long receiverId) {
        try {
            List<Message> messages = messageRepository.findAll(); // TODO: Implement actual query logic
            logger.info("Fetched {} messages between users {} and {}", messages.size(), senderId, receiverId);
            return messages;
        } catch (Exception e) {
            logger.error("Error retrieving messages between users {} and {}", senderId, receiverId, e);
            return List.of();
        }
    }


    public Message sendMessage(Message message) {
        try {
            Message savedMessage = messageRepository.save(message);
            logger.info("Message sent from {} to {} with ID {}", message.getSender().getId(), message.getReceiver().getId(), savedMessage.getId());
            return savedMessage;
        } catch (Exception e) {
            logger.error("Error sending message from {} to {}", message.getSender().getId(), message.getReceiver().getId(), e);
            return null;
        }
    }

    public void deleteMessage(Long id) {
        try {
            Optional<Message> message = messageRepository.findById(id);
            if (message.isPresent()) {
                messageRepository.deleteById(id);
                logger.info("Message with ID {} deleted successfully.", id);
            } else {
                logger.warn("Attempted to delete non-existing message with ID {}", id);
            }
        } catch (Exception e) {
            logger.error("Error deleting message with ID {}", id, e);
        }
    }
}
