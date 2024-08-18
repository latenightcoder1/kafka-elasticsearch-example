package org.example.service;

import java.util.List;
import org.example.model.MessageDetails;

public interface MessageService {

    List<MessageDetails> sendMessage(List<MessageDetails> messages);
}
