package com.eomcs.handler;

import java.util.List;
import com.eomcs.pms.dao.ProjectDao;
import com.eomcs.pms.domain.Member;
import com.eomcs.pms.domain.Project;
import com.eomcs.util.Prompt;

public class ProjectDetailHandler implements Command {

  ProjectDao projectDao;

  public ProjectDetailHandler(ProjectDao projectDao) {
    this.projectDao = projectDao;
  }

  @Override
  public void service() throws Exception {
    System.out.println("[프로젝트 상세보기]");

    int no = Prompt.inputInt("번호? ");

    Project p = projectDao.findByNo(no);

    System.out.printf("프로젝트명: %s\n", p.getTitle());
    System.out.printf("내용: %s\n", p.getContent());
    System.out.printf("시작일: %s\n", p.getStartDate());
    System.out.printf("종료일: %s\n", p.getEndDate());
    System.out.printf("관리자: %s\n", p.getOwner().getName());

    StringBuilder strBuilder = new StringBuilder();
    List<Member> members = p.getMembers();
    for (Member m : members) {
      if (strBuilder.length() > 0) {
        strBuilder.append("/");
      }
      strBuilder.append(m.getName());
    }
    System.out.printf("팀원: %s\n", strBuilder.toString());
  }
}








