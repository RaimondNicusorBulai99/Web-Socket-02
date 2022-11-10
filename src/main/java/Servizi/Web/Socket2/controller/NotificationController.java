package Servizi.Web.Socket2.controller;

import Servizi.Web.Socket2.entities.ClientMessageDTO;
import Servizi.Web.Socket2.entities.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class NotificationController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/broadcast-message")
    public ResponseEntity sendNotificationToClient(@RequestBody MessageDTO messageDTO) {
        try {
            simpMessagingTemplate.convertAndSend("/topic/broadcast", messageDTO);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @MessageMapping("/client-message")
    @SendTo("/topic/broadcast")
    public MessageDTO handleMessageFromWebSocket(ClientMessageDTO clientMessageDTO) {
        System.out.printf("Arrived something on app/hello : %s", clientMessageDTO);
        return new MessageDTO("message from client " + clientMessageDTO.getClientName(), " - message: "
                + clientMessageDTO.getClientAlert(), " alert: " + clientMessageDTO.getClientMsg());
    }
}