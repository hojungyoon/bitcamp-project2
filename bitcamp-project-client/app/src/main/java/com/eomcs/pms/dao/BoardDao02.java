package com.eomcs.pms.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.eomcs.pms.domain.Board;
import com.eomcs.pms.domain.Member;

public class BoardDao02 {
  static Connection con;

  static {
    try {
      con = DriverManager.getConnection(
          "jdbc:mysql://localhost:3306/studydb?user=study&password=1111");
    } catch (Exception e) {
      System.out.println("");
    }
  }

  public static int insert(Board board) throws Exception {
    try (PreparedStatement stmt =
        con.prepareStatement("insert into pms_board(title, content, writer) values(?,?,?)");) {

      stmt.setString(1, board.getTitle());
      stmt.setString(2, board.getContent());
      stmt.setInt(3, board.getWriter().getNo());

      return stmt.executeUpdate();
    }
  }

  public static List<Board> findAll() throws Exception {
    ArrayList<Board> list = new ArrayList<>();

    try (PreparedStatement stmt = con.prepareStatement(
        "select"
            + " b.no,"
            + " b.title,"
            + " b.cdt,"
            + " b.vw_cnt,"
            + " b.like_cnt,"
            + " m.no as writer_no,"
            + " m.name as writer_name"
            + " from pms_board b"
            + "   inner join pms_member m on m.no=b.writer"
            + " order by b.no desc");
        ResultSet rs = stmt.executeQuery()) {

      while (rs.next()) {
        Board board = new Board();
        board.setNo(rs.getInt("no"));
        board.setTitle(rs.getString("title"));
        board.setRegisteredDate(rs.getDate("cdt"));
        board.setViewCount(rs.getInt("vw_cnt"));

        Member writer = new Member();
        writer.setNo(rs.getInt("writer_no"));
        writer.setName(rs.getString("writer_name"));
        board.setWriter(writer);

        list.add(board);
      }
    }
    return list;
  }

  public static Board findByNo(int no) throws Exception {
    try (PreparedStatement stmt = con.prepareStatement(
        "select"
            + " b.no,"
            + " b.title,"
            + " b.content,"
            + " b.cdt,"
            + " b.vw_cnt,"
            + " b.like_cnt,"
            + " m.no as writer_no,"
            + " m.name as writer_name"
            + " from pms_board b"
            + "   inner join pms_member m on m.no=b.writer"
            + " where b.no = ?")) {

      stmt.setInt(1, no);

      try (ResultSet rs = stmt.executeQuery()) {
        if (!rs.next()) {
          System.out.println("해당 번호의 게시글이 없습니다.");
          return null;
        }

        Board board = new Board();
        board.setNo(rs.getInt("no"));
        board.setTitle(rs.getString("title"));
        board.setContent(rs.getString("content"));
        board.setRegisteredDate(new Date(rs.getTimestamp("cdt").getTime()));
        board.setViewCount(rs.getInt("vw_cnt"));
        board.setLike(rs.getInt("like_cnt"));

        Member writer = new Member();
        writer.setNo(rs.getInt("writer_no"));
        writer.setName(rs.getString("writer_name"));

        board.setWriter(writer);

        return board;
      }
    }
  }

  public static int update(Board board) throws Exception {
    try (PreparedStatement stmt2 = con.prepareStatement(
        "update pms_board set title=?, content=? where no=?")) {

      stmt2.setString(1, board.getTitle());
      stmt2.setString(2, board.getContent());
      stmt2.setInt(3, board.getNo());
      return stmt2.executeUpdate();
    }
  }

  public static int updateViewCount(int no) throws Exception {
    try (PreparedStatement stmt2 = con.prepareStatement(
        "update pms_board set vw_cnt=vw_cnt + 1 where no=?")) {

      stmt2.setInt(1, no);
      return stmt2.executeUpdate();
    }
  }

  public static int delete(int no) throws Exception {

    try (PreparedStatement stmt = con.prepareStatement(
        "delete from pms_board where no=?")) {

      stmt.setInt(1, no);
      return stmt.executeUpdate();

    }
  }

  public static List<Board> findByKeyword(String keyword) throws Exception {
    ArrayList<Board> list = new ArrayList<>();

    try (PreparedStatement stmt = con.prepareStatement(
        "select"
            + " b.no,"
            + " b.title,"
            + " b.cdt,"
            + " b.vw_cnt,"
            + " b.like_cnt,"
            + " m.no as writer_no,"
            + " m.name as writer_name"
            + " from pms_board b"
            + "   inner join pms_member m on m.no=b.writer"
            + " where b.title like concat('%',?,'%')"
            + "  or b.content like concat('%',?,'%')"
            + "  or m.name like concat('%',?,'%')"
            + " order by b.no desc")) {

      stmt.setString(1, keyword);
      stmt.setString(2, keyword);
      stmt.setString(3, keyword);

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        Board board = new Board();
        board.setNo(rs.getInt("no"));
        board.setTitle(rs.getString("title"));
        board.setRegisteredDate(rs.getDate("cdt"));
        board.setViewCount(rs.getInt("vw_cnt"));

        Member writer = new Member();
        writer.setNo(rs.getInt("writer_no"));
        writer.setName(rs.getString("writer_name"));
        board.setWriter(writer);

        list.add(board);
      }
    }
    return list;
  }

}
