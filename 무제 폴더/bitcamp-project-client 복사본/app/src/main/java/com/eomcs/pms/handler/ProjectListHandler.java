package com.eomcs.pms.handler;

import java.util.Iterator;
import com.eomcs.driver.Statement;

public class ProjectListHandler implements Command {

  @Override
  public void service(Statement stmt) throws Exception {

    System.out.println("[프로젝트 목록]");

    Iterator<String> results = stmt.executeQuery("project/selectall");

    while (results.hasNext()) {
      String[] fields = results.next().split(",");

      System.out.printf("%d, %s, %s, %s, %s, [%s]\n",
          fields[0],
          fields[1],
          fields[2],
          fields[3],
          fields[4],
          fields[5]);
    }
  }
}








