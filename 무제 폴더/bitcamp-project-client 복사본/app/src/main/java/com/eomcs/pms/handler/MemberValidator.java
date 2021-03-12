package com.eomcs.pms.handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import com.eomcs.util.Prompt;

public class MemberValidator {

  public static String inputMember(String promptTitle, DataInputStream in, DataOutputStream out) throws Exception{
    while (true) {
      String name = Prompt.inputString(promptTitle);
      out.writeUTF("member/selectByName");
      out.writeInt(1);
      out.writeUTF(name);
      out.flush();

      String status = in.readUTF();
      in.readInt();

      if (status.equals("success")) {
        String[] fields = in.readUTF().split(",");
        return fields[1];
      }
      System.out.println("등록된 회원이 아닙니다.");
    }
  }

  public static String inputMembers(String promptTitle, DataInputStream in, DataOutputStream out) throws Exception {
    String members = "";
    while (true) {
      String name = inputMember(promptTitle, in, out);
      if (name == null) {
        return members;
      } else {
        if (!members.isEmpty()) {
          members += "/";
        }
        members += name;
      }
    }
  }

}






