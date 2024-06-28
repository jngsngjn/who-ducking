package hello.dto.board;

import hello.entity.board.Board;
import lombok.Data;

@Data
public class BoardListMainDTO {

    private Board board;
    private int commentCount;

    public BoardListMainDTO(Board board, int commentCount) {
        this.board = board;
        this.commentCount = commentCount;
    }

}
