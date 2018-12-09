package demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// �������ӵ��������е��ֻ���
public class ClientManager {

    private static ServerThread serverThread = null;
    private static int sum = 0;
    private static Map<String, Socket> clientMap = new HashMap<>();
    private static List<String> clientList = new ArrayList<>();

    private static class ServerThread implements Runnable {

        private ServerSocket server;
        private int port = 10086;
        private boolean isExit = false;// һ��boolean���͵��ж� Ĭ�����˳�״̬false

        // ���췽����ʼ��
        public ServerThread() {
            try {
                server = new ServerSocket(port);
                System.out.println("����server���˿ںţ�" + port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 1.���Զ�̷�������IP ��ַ.
         * InetAddress inetAddress = socket.getInetAddress();
         * 2.���Զ�̷������Ķ˿�.
         * int port = socket.getPort();
         * 3. ��ÿͻ����ص�IP ��ַ.
         * InetAddress localAddress = socket.getLocalAddress();
         * 4.��ÿͻ����صĶ˿�.
         * int localPort = socket.getLocalPort();
         * 5.��ȡ���صĵ�ַ�Ͷ˿ں�
         * SocketAddress localSocketAddress = socket.getLocalSocketAddress();
         * 6.���Զ�̵ĵ�ַ�Ͷ˿ں�
         * SocketAddress remoteSocketAddress = socket.getRemoteSocketAddress();
         */
        @Override
        public void run() {
            try {
                while (!isExit) {
                    // �ȴ�����
                    System.out.println("�ȴ��ֻ���������... ...");

                    final Socket socket = server.accept();
                    System.out.println(socket.getLocalSocketAddress().toString());
                    System.out.println("��ȡ���ֻ�IP��ַ���˿ںţ�" + socket.getRemoteSocketAddress().toString());
                    /**
                     * ��Ϊ���ǵ����ֻ����ӵ���� ���Լ����߳��� ֻ�����̹߳���
                     */
                    new Thread(new Runnable() {

                        private String text;

                        @Override
                        public void run() {
                            try {
                                synchronized (this) {
                                    // �����￼�ǵ��߳������ļ��� Ҳ�����������ֻ�������
                                    ++sum;
                                    // ���뵽���Ϻ�Map��ΪȺ���͵���������׼��
                                    String string = socket.getRemoteSocketAddress().toString();
                                    clientList.add(string);
                                    clientMap.put(string, socket);
                                }

                                // �������������
                                InputStream is = socket.getInputStream();
                                OutputStream os = socket.getOutputStream();

                                // �����������������Ķ�ȡ��ʾ��PC�˺ͷ����Ƿ��յ�
                                byte[] buffer = new byte[1024];
                                int len;
                                while ((len = is.read(buffer)) != -1) {
                                    text = new String(buffer, 0, len);

                                    System.out.println("�յ�������Ϊ��" + text);
                                    os.write("���յ���Ϣ".getBytes("utf-8"));

                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                System.out.println("�ر����ӣ�" + socket.getRemoteSocketAddress().toString());
                                synchronized (this) {
                                    --sum;
                                    String string = socket.getRemoteSocketAddress().toString();
                                    clientMap.remove(string);
                                    clientList.remove(string);
                                }
                            }
                        }
                    }).start();

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // �ر�server
        public void stop() {
            isExit = true;
            if (server != null) {
                try {
                    server.close();
                    System.out.println("�ѹر�server");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    // ����server
    public static ServerThread startServer() {
        System.out.println("����server");
        if (serverThread != null) {
            System.out.println("server��Ϊnull��������server");
            // ����Ϊ�ر�server��socket
            shutDown();
        }
        // ��ʼ��
        serverThread = new ServerThread();
        new Thread(serverThread).start();
        System.out.println("����server�ɹ�");
        return serverThread;
    }


    // ������Ϣ�ķ���
    public static boolean sendMessage(String name, String mag) {
        try {
            Socket socket = clientMap.get(name);
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(mag.getBytes("utf-8"));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Ⱥ���ķ���
    public static boolean sendMsgAll(String msg){
        try {
            for (Socket socket : clientMap.values()) {
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(msg.getBytes("utf-8"));
            }
                return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    // ��ȡ�߳������ķ�����Ҳ��ͬ��<��ȡ�������˶���̨�ֻ�>�ķ���+
    public static int sumTotal() {
        return sum;
    }

    // һ����ȡlist���ϵķ�����ȡ����������server���ֻ���ip�Ͷ˿ںŵļ���
    public static List<String> getTotalClients() {
        return clientList;
    }

    public static void shutDown() {
        for (Socket socket : clientMap.values()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        serverThread.stop();
        clientMap.clear();
        clientList.clear();
    }


}