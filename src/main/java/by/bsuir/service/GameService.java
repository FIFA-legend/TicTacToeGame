package by.bsuir.service;

import by.bsuir.entity.Game;
import by.bsuir.entity.Move;
import by.bsuir.entity.Status;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class GameService {

    private static final List<Game> games = new LinkedList<>();

    public Game connectToInterruptedGame(String username) {
        Move move = null;
        Game game = null;
        for (Game g : games) {
            if (g.getCreatorUsername() != null && g.getCreatorUsername().equals(username)) {
                game = g;
                move = Move.X;
            } else if (g.getOpponentUsername() != null && g.getOpponentUsername().equals(username)) {
                game = g;
                move = Move.O;
            }
        }
        if (game == null) return null;
        selectRightMove(game, move);
        return game;
    }

    private void selectRightMove(Game game, Move move) {
        int userMoves = 0;
        int opponentMoves = 0;
        for (Move m : game.getMoves()) {
            if (m == move) {
                userMoves++;
                continue;
            }
            if (m != null) {
                opponentMoves++;
            }
        }
        if (userMoves == opponentMoves) {
            if (move == Move.X) game.setStatus(Status.MOVE);
            else game.setStatus(Status.WAIT);
        } else {
            if (move == Move.X) game.setStatus(Status.WAIT);
            else game.setStatus(Status.MOVE);
        }
    }

    public Game connectToGame(String username) {
        for (Game game : games) {
            if (game.getOpponentUsername() == null) {
                game.setOpponentUsername(username);
                return game;
            }
        }
        return null;
    }

    public Game createGame(String username) {
        Game game = new Game(username);
        games.add(game);
        return game;
    }

    public Game makeMove(Game game) {
        if (isGameWon(game.getMoves())) {
            game.setStatus(Status.WIN);
            return game;
        }
        if (isGameDraw(game.getMoves())) {
            game.setStatus(Status.DRAW);
            return game;
        }
        game.setStatus(Status.MOVE);
        updateGame(game);
        return game;
    }

    private void updateGame(Game game) {
        for (Game g : games) {
            if (g.getId() == game.getId()) {
                g.setMoves(game.getMoves());
                break;
            }
        }
    }

    public void deleteGame(Game game) {
        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).getId() == game.getId()) {
                games.remove(i);
                break;
            }
        }
    }

    private boolean isGameWon(Move[] moves) {
        if (moves[0] == moves[1] && moves[0] == moves[2] && moves[0] != null) {
            return true;
        }
        if (moves[3] == moves[4] && moves[3] == moves[5] && moves[3] != null) {
            return true;
        }
        if (moves[6] == moves[7] && moves[6] == moves[8] && moves[6] != null) {
            return true;
        }
        if (moves[0] == moves[3] && moves[0] == moves[6] && moves[0] != null) {
            return true;
        }
        if (moves[1] == moves[4] && moves[1] == moves[7] && moves[1] != null) {
            return true;
        }
        if (moves[2] == moves[5] && moves[2] == moves[8] && moves[2] != null) {
            return true;
        }
        if (moves[0] == moves[4] && moves[0] == moves[8] && moves[0] != null) {
            return true;
        }
        return moves[2] == moves[4] && moves[2] == moves[6] && moves[2] != null;
    }

    private boolean isGameDraw(Move[] moves) {
        for (Move move : moves) {
            if (move == null) return false;
        }
        return true;
    }

}