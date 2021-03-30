package com.eomcs.pms.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.eomcs.pms.domain.Member;
import com.eomcs.pms.domain.Project;

public class ProjectDao {

  Connection con;

  public ProjectDao() throws Exception {
    this.con = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/studydb?user=study&password=1111");
  }

  public int insert(Project project) throws Exception {
    try (PreparedStatement stmt = con.prepareStatement(
        "insert into pms_project(title,content,sdt,edt,owner) values(?,?,?,?,?)",
        Statement.RETURN_GENERATED_KEYS)) {

      con.setAutoCommit(false);

      stmt.setString(1, project.getTitle());
      stmt.setString(2, project.getContent());
      stmt.setDate(3, project.getStartDate());
      stmt.setDate(4, project.getEndDate());
      stmt.setInt(5, project.getOwner().getNo());
      int count = stmt.executeUpdate();

      try (ResultSet keyRs = stmt.getGeneratedKeys()) {
        keyRs.next();
        project.setNo(keyRs.getInt(1));
      }

      for (Member member : project.getMembers()) {
        insertMember(project.getNo(), member.getNo());
      }

      con.commit();

      return count;

    } finally {
      con.setAutoCommit(true);
    }
  }

  public int insertMember(int projectNo, int memberNo) throws Exception {
    try (PreparedStatement stmt2 = con.prepareStatement(
        "insert into pms_member_project(member_no,project_no) values(?,?)")) {
      stmt2.setInt(1, memberNo);
      stmt2.setInt(2, projectNo);
      return stmt2.executeUpdate();
    }
  }

  public List<Project> findAll() throws Exception {
    ArrayList<Project> list = new ArrayList<>();

    try (PreparedStatement stmt = con.prepareStatement(
        "select" 
            + "    p.no,"
            + "    p.title,"
            + "    p.sdt,"
            + "    p.edt,"
            + "    m.no as owner_no,"
            + "    m.name as owner_name"
            + "  from pms_project p"
            + "    inner join pms_member m on p.owner=m.no"
            + "  order by title asc");
        ResultSet rs = stmt.executeQuery()) {

      while (rs.next()) {
        Project p = new Project();
        p.setNo(rs.getInt("no"));
        p.setTitle(rs.getString("title"));
        p.setStartDate(rs.getDate("sdt"));
        p.setEndDate(rs.getDate("edt"));

        Member owner = new Member();
        owner.setNo(rs.getInt("owner_no"));
        owner.setName(rs.getString("owner_name"));

        p.setOwner(owner);
        p.setMembers(findAllMembers(p.getNo()));

        list.add(p);
      }
    }
    return list;
  }

  public List<Member> findAllMembers(int projectNo) throws Exception {
    ArrayList<Member> list = new ArrayList<>();

    try (PreparedStatement stmt = con.prepareStatement(
        "select" 
            + "    m.no,"
            + "    m.name"
            + " from pms_member_project mp"
            + "     inner join pms_member m on mp.member_no=m.no"
            + " where "
            + "     mp.project_no=?")) {

      stmt.setInt(1, projectNo);

      try (ResultSet memberRs = stmt.executeQuery()) {
        while(memberRs.next()) {
          Member m = new Member();
          m.setNo(memberRs.getInt("no"));
          m.setName(memberRs.getString("name"));
          list.add(m);
        }
      }
    }
    return list;
  }

  public Project findByNo(int no) throws Exception {
    Project p = new Project();
    try (Connection con = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/studydb?user=study&password=1111");
        PreparedStatement stmt = con.prepareStatement(
            "select"
                + "    p.no,"
                + "    p.title,"
                + "    p.content,"
                + "    p.sdt,"
                + "    p.edt,"
                + "    m.no as owner_no,"
                + "    m.name as owner_name"
                + "  from pms_project p"
                + "    inner join pms_member m on p.owner=m.no"
                + " where p.no=?")) {

      stmt.setInt(1, no);

      try (ResultSet rs = stmt.executeQuery()) {
        if (!rs.next()) {
        }

        p.setNo(no);
        p.setTitle(rs.getString("title"));
        p.setContent(rs.getString("content"));
        p.setStartDate(rs.getDate("sdt"));
        p.setEndDate(rs.getDate("edt"));

        Member owner = new Member();
        owner.setNo(rs.getInt("owner_no"));
        owner.setName(rs.getString("owner_name"));

        p.setOwner(owner);
        p.setMembers(findAllMembers(no));
      }
    }
    return p;
  }

  public int update(Project project) throws Exception {
    try (PreparedStatement stmt = con.prepareStatement(
        "update pms_project set"
            + " title=?,"
            + " content=?,"
            + " sdt=?,"
            + " edt=?,"
            + " owner=?"
            + " where no=?")) {

      con.setAutoCommit(false);

      stmt.setString(1, project.getTitle());
      stmt.setString(2, project.getContent());
      stmt.setDate(3, project.getStartDate());
      stmt.setDate(4, project.getEndDate());
      stmt.setInt(5, project.getOwner().getNo());
      stmt.setInt(6, project.getNo());
      int count = stmt.executeUpdate();

      deleteMembers(project.getNo());

      for (Member member : project.getMembers()) {
        insertMember(project.getNo(), member.getNo());
      }

      con.commit();

      return count;

    } finally {
      con.setAutoCommit(true);
    }
  }

  public int deleteMembers(int projectNo) throws Exception {
    try (PreparedStatement stmt = con.prepareStatement( 
        "delete from pms_member_project where project_no=?")) {
      stmt.setInt(1, projectNo);
      return stmt.executeUpdate();
    }
  }

  public int delete(int no) throws Exception {
    try (PreparedStatement stmt = con.prepareStatement(
        "delete from pms_member_project where project_no=?")) {
      con.setAutoCommit(false);

      deleteMembers(no);

      stmt.setInt(1, no);
      int count = stmt.executeUpdate();
      con.commit();

      return count;

    } finally {
      con.setAutoCommit(true);
    }
  }

}
