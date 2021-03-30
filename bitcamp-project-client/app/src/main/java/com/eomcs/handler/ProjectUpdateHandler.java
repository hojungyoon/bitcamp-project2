package com.eomcs.handler;

import java.util.List;
import com.eomcs.pms.dao.ProjectDao;
import com.eomcs.pms.domain.Member;
import com.eomcs.pms.domain.Project;
import com.eomcs.util.Prompt;

public class ProjectUpdateHandler implements Command {

  MemberValidator memberValidator;
  ProjectDao projectDao;

  public ProjectUpdateHandler(ProjectDao projectDao, MemberValidator memberValidator) {
    this.projectDao = projectDao;
    this.memberValidator = memberValidator;
  }

  @Override
  public void service() throws Exception {
    System.out.println("[프로젝트 변경]");

    int no = Prompt.inputInt("번호? ");

    Project project = projectDao.findByNo(no);

    if (project == null) {
      System.out.println("해당 번호의 프로젝트가 없습니다.");
      return;
    }

    project.setTitle(Prompt.inputString(
        String.format("프로젝트명(%s)? ", project.getTitle())));
    project.setContent(Prompt.inputString(
        String.format("내용(%s)? ", project.getContent())));
    project.setStartDate(Prompt.inputDate(
        String.format("시작일(%s)? ", project.getStartDate())));
    project.setEndDate(Prompt.inputDate(
        String.format("종료일(%s)? ", project.getEndDate())));
    project.setOwner(memberValidator.inputMember(
        String.format("만든이(%s)?(취소: 빈 문자열) ", project.getOwner().getName())));

    if (project.getOwner() == null) {
      System.out.println("프로젝트 변경을 취소합니다.");
      return;
    }

    StringBuilder strings = new StringBuilder();
    List<Member> members = project.getMembers();

    for (Member m : members) {
      if (strings.length() > 0) {
        strings.append("/");
      }
      strings.append(m.getName());
    }

    project.setMembers(memberValidator.inputMembers(
        String.format("팀원(%s)?(완료: 빈 문자열) ", strings)));

    String input = Prompt.inputString("정말 변경하시겠습니까?(y/N) ");
    if (!input.equalsIgnoreCase("Y")) {
      System.out.println("프로젝트 변경을 취소하였습니다.");
      return;
    }

    projectDao.update(project);

    System.out.println("프로젝트을 변경하였습니다.");
  }
}






