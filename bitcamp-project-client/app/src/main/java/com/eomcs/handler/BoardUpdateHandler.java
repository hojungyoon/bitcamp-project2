package com.eomcs.handler;

import com.eomcs.pms.dao.BoardDao;
import com.eomcs.pms.domain.Board;
import com.eomcs.util.Prompt;

public class BoardUpdateHandler implements Command {

  BoardDao boardDao;

  public BoardUpdateHandler(BoardDao boardDao) {
    this.boardDao = boardDao;
  }

  @Override
  public void service() throws Exception {
    System.out.println("[게시글 변경]");

    int no = Prompt.inputInt("번호? ");
    Board board = boardDao.findByNo(no);

    if (board == null) {
      System.out.println("해당 번호의 게시글이 없습니다.");
      return;
    }

    // 2) 사용자에게서 변경할 데이터를 입력 받는다.
    board.setTitle(Prompt.inputString(String.format("제목(%s)? ", board.getTitle())));
    board.setContent(Prompt.inputString(String.format("내용(%s)? ", board.getContent())));

    String input = Prompt.inputString("정말 변경하시겠습니까?(y/N) ");
    if (!input.equalsIgnoreCase("Y")) {
      System.out.println("게시글 변경을 취소하였습니다.");
      return;
    }
    boardDao.update(board);
    System.out.println("게시글을 변경하였습니다.");
  }
}






