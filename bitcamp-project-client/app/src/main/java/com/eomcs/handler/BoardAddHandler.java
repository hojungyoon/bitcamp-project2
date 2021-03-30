package com.eomcs.handler;

import com.eomcs.pms.dao.BoardDao;
import com.eomcs.pms.domain.Board;
import com.eomcs.pms.domain.Member;
import com.eomcs.util.Prompt;

public class BoardAddHandler implements Command {

  BoardDao boardDao;

  public BoardAddHandler(BoardDao boardDao) {
    this.boardDao = boardDao;
  }

  @Override
  public void service() throws Exception {
    System.out.println("[게시글 등록]");

    Board b = new Board();

    b.setTitle(Prompt.inputString("제목? "));
    b.setContent(Prompt.inputString("내용? "));

    Member writer = new Member();
    writer.setNo(Prompt.inputInt("작성자 번호? "));
    b.setWriter(writer);

    boardDao.insert(b);
    System.out.println("게시글을 등록하였습니다.");
  }
}





