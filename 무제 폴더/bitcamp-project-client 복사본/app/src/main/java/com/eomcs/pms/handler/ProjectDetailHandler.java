package com.eomcs.pms.handler;

import java.util.Iterator;
import com.eomcs.driver.Statement;
import com.eomcs.util.Prompt;

public class ProjectDetailHandler implements Command {

  @Override
  public void service(Statement stmt) throws Exception {
    System.out.println("[프로젝트 상세보기]");

    int no = Prompt.inputInt("번호? ");

    Iterator<String> results = stmt.executeQuery("project/select", Integer.toString(no));

    String[] fields = results.next().split(",");

    System.out.printf("프로젝트명: %s\n", fields[1]);
    System.out.printf("내용: %s\n", fields[2]);
    System.out.printf("시작일: %s\n", fields[3]);
    System.out.printf("종료일: %s\n", fields[4]);
    System.out.printf("관리자: %s\n", fields[5]);
    System.out.printf("팀원: %s\n", fields[6]);

  }
}








