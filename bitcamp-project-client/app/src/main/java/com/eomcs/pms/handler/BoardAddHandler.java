package com.eomcs.pms.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.eomcs.pms.domain.Board;
import com.eomcs.pms.domain.Member;
import com.eomcs.util.Prompt;

public class BoardAddHandler implements Command {

  MemberValidator memberValidator;

  public BoardAddHandler(MemberValidator memberValidator) {
    this.memberValidator = memberValidator;
  }


  @Override
  public void service() throws Exception {
    System.out.println("[게시글 등록]");

    Board b = new Board();

    try (Connection con = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/studydb?user=study&password=1111");
        PreparedStatement stmt = con.prepareStatement(
            "insert into pms_board(title, content, writer) values(?,?,?)");
        PreparedStatement stmt2 = con.prepareStatement(
            "select no,name from pms_member order by no asc");
        ResultSet rs = stmt2.executeQuery()) {

      List<Member> members = new ArrayList<>();

      b.setTitle(Prompt.inputString("제목? "));
      b.setContent(Prompt.inputString("내용? "));
      System.out.println("[회원 리스트]");
      while (rs.next()) {
        Member m = new Member();
        m.setNo(rs.getInt("no"));
        m.setName(rs.getString("name"));
        members.add(m);
      }
      for (Member m : members) {
        System.out.printf("%d, %s\n", m.getNo(), m.getName());
      }

      b.setWriter(memberValidator.inputMember("작성자? "));


      stmt.setString(1, b.getTitle());
      stmt.setString(2, b.getContent());
      stmt.setInt(3, b.getWriter().getNo());

      stmt.executeUpdate();

      System.out.println("게시글을 등록하였습니다.");
    }
  }
}






