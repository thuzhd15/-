package demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class MyClass {

    public static void main(String[]args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        boolean isExit = false;

        ClientManager.startServer();
        while (!isExit){
            line = br.readLine();
            if (line.startsWith("exit")){
                System.out.println("�˳�����");
                break;
            }

            if (line.startsWith("send")){
                sendMessage(line);
            }else if (line.startsWith("list")){
                printTotal();
            }else if (line.startsWith("all")){
                allSendMsg(line);
            }else {
                System.out.println("������� ����������");
            }

        }
        // �ر� ���
        ClientManager.shutDown();

    }

    private static void allSendMsg(String line) {
        String[] field = line.split("//");
        if (field.length == 2){
            ClientManager.sendMsgAll(field[1]);
            System.out.println("���ͽ��Ϊ��" + ClientManager.sendMsgAll(field[1]) );
        }else {
            System.out.println("��ʽ����ȷ ����all//message");
        }
    }

    private static void printTotal() {
        List<String> totalClients = ClientManager.getTotalClients();
        System.out.println("��������Ϊ��" + totalClients.size());
        for (String totalClient : totalClients) {
            System.out.println(totalClient);
        }
    }

    private static void sendMessage(String line) {
        String[] field = line.split("//");
        if (field.length == 3){
            // ��ʽ��ȷ
            ClientManager.sendMessage(field[1],field[2]);
            System.out.println("send���Ϊ:" + ClientManager.sendMessage(field[1],field[2]));
        }else {
            System.out.println("�����ȷ�����ӣ�send//name//msg");
        }
    }

}