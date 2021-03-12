package com.eomcs.pms.table;

import java.io.File;
import java.sql.Date;
import java.util.List;
import com.eomcs.pms.domain.Task;
import com.eomcs.util.JsonFileHandler;
import com.eomcs.util.Request;
import com.eomcs.util.Response;

// 1) 간단한 동작 테스트를 위해 임의의 값을 리턴한다.
// 2) JSON 포맷의 파일을 로딩한다.
public class TaskTable implements DataTable {

  File jsonFile = new File("Tasks.json");
  List<Task> list;

  public TaskTable() {
    list = JsonFileHandler.loadObjects(jsonFile, Task.class);
  }

  @Override
  public void service(Request request, Response response) throws Exception {
    Task task = null;
    String[] fields = null;

    switch (request.getCommand()) {
      case "task/insert":

        fields = request.getData().get(0).split(",");

        task = new Task();

        if (list.size() > 0) {
          task.setNo(list.get(list.size() - 1).getNo() + 1);
        } else {
          task.setNo(1);
        }

        task.setContent(fields[0]);
        task.setDeadline(Date.valueOf(fields[1]));
        task.setStatus(Integer.parseInt(fields[2]));
        task.setOwner(fields[3]);

        list.add(task);

        JsonFileHandler.saveObjects(jsonFile, list);
        break;
      case "task/selectall":
        for (Task t : list) {
          response.appendData(String.format("%d,%s,%s,%s,%s", 
              t.getNo(),
              t.getContent(),
              t.getDeadline(),
              String.format(getStatusLabel(t.getStatus())),
              t.getOwner()));
        }
        break;
      case "task/select":
        int no = Integer.parseInt(request.getData().get(0));

        task = getTask(no);
        if (task != null) {
          response.appendData(String.format("%d,%s,%s,%s,%s,%s,%s", 
              task.getNo(), 
              task.getContent(), 
              task.getDeadline(),
              String.format(getStatusLabel(task.getStatus())),
              task.getOwner()));
        } else {
          throw new Exception("해당 번호의 게시글이 없습니다.");
        }
        break;
      case "task/update":
        fields = request.getData().get(0).split(",");

        task = getTask(Integer.parseInt(fields[0]));
        if (task == null) {
          throw new Exception("해당 번호의 게시글이 없습니다.");
        }

        // 해당 게시물의 제목과 내용을 변경한다.
        // - List 에 보관된 객체를 꺼낸 것이기 때문에 
        //   그냥 그 객체의 값을 변경하면 된다.
        task.setContent(fields[1]);
        task.setDeadline(Date.valueOf(fields[2]));
        task.setStatus(Integer.parseInt(fields[3]));
        task.setOwner(fields[4]);

        JsonFileHandler.saveObjects(jsonFile, list);
        break;
      case "task/delete":
        no = Integer.parseInt(request.getData().get(0));
        task = getTask(no);
        if (task == null) {
          throw new Exception("해당 번호의 게시글이 없습니다.");
        }

        list.remove(task);

        JsonFileHandler.saveObjects(jsonFile, list);
        break;
      default:
        throw new Exception("해당 명령을 처리할 수 없습니다.");
    }
  }

  private Task getTask(int memberNo) {
    for (Task t : list) {
      if (t.getNo() == memberNo) {
        return t;
      }
    }
    return null;
  }

  private String getStatusLabel(int status) {
    switch (status) {
      case 1:
        return "진행중";
      case 2:
        return "완료";
      default:
        return "신규";
    }
  }
}
