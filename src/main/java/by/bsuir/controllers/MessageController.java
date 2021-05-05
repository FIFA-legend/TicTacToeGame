package by.bsuir.controllers;

import by.bsuir.entity.Game;
import by.bsuir.entity.Status;
import by.bsuir.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class MessageController {

    private final GameService gameService;

    private final SimpMessagingTemplate messaging;

    @Autowired
    public MessageController(GameService gameService, SimpMessagingTemplate messaging) {
        this.gameService = gameService;
        this.messaging = messaging;
    }
    
    @MessageMapping("/game/recover")
    public void recoverGame(Principal principal) {
        Game game = gameService.connectToInterruptedGame(principal.getName());
        if (game == null) {
            messaging.convertAndSendToUser(principal.getName(), "/queue/opponent/recover", "NONE");
        } else {
            messaging.convertAndSendToUser(principal.getName(), "/queue/opponent/recover", game);
        }
    }
    
    @MessageMapping("/game/connect")
    public void connectToGame(Principal principal) {
        Game game = gameService.connectToGame(principal.getName());
        if (game == null) {
            messaging.convertAndSendToUser(principal.getName(), "/queue/opponent/connect", "NONE");
        } else {
            messaging.convertAndSendToUser(principal.getName(), "/queue/opponent/connect", game);
        }
    }

    @MessageMapping("/game/create")
    public void createGame(Principal principal) {
        messaging.convertAndSendToUser(principal.getName(), "/queue/opponent/create",
                gameService.createGame(principal.getName()));
    }

    @MessageMapping("/game/opponent/update")
    public void sendUsername(@Payload Game game) {
        game.setStatus(Status.MOVE);
        messaging.convertAndSendToUser(game.getCreatorUsername(), "/queue/opponent/name", game);
    }

    @MessageMapping("/game/move")
    public void makeMove(Principal principal, @Payload Game game) {
        String moveUsername = principal.getName();
        String opponentUsername = getOpponentUsername(moveUsername, game);
        game = gameService.makeMove(game);
        if (game.getStatus() == Status.WIN) {
            messaging.convertAndSendToUser(moveUsername, "/queue/opponent/move", game);
            game.setStatus(Status.LOSE);
            messaging.convertAndSendToUser(opponentUsername, "/queue/opponent/move", game);
            gameService.deleteGame(game);
        } else if (game.getStatus() == Status.DRAW) {
            messaging.convertAndSendToUser(moveUsername, "/queue/opponent/move", game);
            messaging.convertAndSendToUser(opponentUsername, "/queue/opponent/move", game);
            gameService.deleteGame(game);
        } else {
            messaging.convertAndSendToUser(opponentUsername, "/queue/opponent/move", game);
        }
    }

    private String getOpponentUsername(String username, Game game) {
        if (username.equals(game.getCreatorUsername())) return game.getOpponentUsername();
        else return game.getCreatorUsername();
    }
}