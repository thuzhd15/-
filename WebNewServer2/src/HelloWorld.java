import javax.swing.*;

import java.awt.*;

import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


class HelloWorld extends JPanel

{

	private static final long serialVersionUID = 1834511718758119719L;

	static final int WIDTH=800;

	static final int HEIGHT=500;

	private OutputStream output;
	private Socket clientSocket;
	
	JFrame loginframe;

	JButton start=new JButton("��������");

	JButton connect=new JButton("����");
	JButton send=new JButton("����");

	JLabel title=new JLabel("��ӭ����Java����");

	JLabel IP=new JLabel("IP");

	JLabel number=new JLabel("�˿ں�");

	JTextField IPinput=new JTextField(15);

	JTextField numberinput=new JTextField(15);
	JTextField datainput=new JTextField(20);
	JTextArea show=new JTextArea(15,40);

	public void add(Component c,GridBagConstraints constraints,int x,int y,int w,int h)

	{

		constraints.gridx=x;

		constraints.gridy=y;

		constraints.gridwidth=w;

		constraints.gridheight=h;

		add(c,constraints);  //����ط��ǵ��õĸ����add����

	}                                         //�˷���������ӿؼ���������

	///����һ������������

	/// loginframe�Ǿ���ָ�������Ŀ��

	/// setDefaultCloseOperation����һ��ʹ�ô�������Ĺرտؼ���Ч����ⷽ��

	/// lay��һ�������鲼�ֹ������Ķ���

	/// nameinput�����������û������ı���

	/// passwordinput����������������ı���

	/// title��������ʾ����ı�ǩ��

	/// name��������ʾ���������ı�ǩ��

	/// password��������ʾ�����롱�ı�ǩ��

	/// ok��һ����ť��ʹ����ϵͳ��

	/// cancel��һ����ť��ʹ�˳������ϵͳ��

	/// ok.addActionListener��һ������ϵͳ�����¼�����������

	/// cancel.addActionListener��һ���˳�ϵͳ�ͽ��涯���¼��ļ���������

	HelloWorld()

	{

		loginframe=new JFrame("WebServerDemo"); 

		loginframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		GridBagLayout lay=new GridBagLayout();  

		setLayout(lay);   //����panel�Ĳ���ʹ��grid                     

		loginframe.add(this, BorderLayout.WEST);//frame�Ĳ���ʹ��BorderLayout

		loginframe.setSize(WIDTH,HEIGHT);
		
//		JPanel panel = (JPanel)loginframe.getContentPane();
		
		Toolkit kit=Toolkit.getDefaultToolkit();

		Dimension screenSize=kit.getScreenSize();  //ϵͳ�����ȡ����

		int width=screenSize.width;

		int height=screenSize.height;

		int x=(width-WIDTH)/2;

		int y=(height-HEIGHT)/2;

		loginframe.setLocation(x,y);



		GridBagConstraints constraints=new GridBagConstraints();

		constraints.fill=GridBagConstraints.NONE;

		constraints.anchor=GridBagConstraints.EAST;

		constraints.weightx=3;

		constraints.weighty=4;

		//

		add(title,constraints,0,0,3,1);                 //ʹ�������鲼����ӿؼ�

		add(IP,constraints,0,1,1,1);

		add(number,constraints,0,2,1,1);
		

		add(IPinput,constraints,2,1,1,1);
		add(datainput,constraints,3,1,1,1);

		add(numberinput,constraints,2,2,1,1);
		IPinput.setText("");          //!!!
		numberinput.setText("20000");
		add(start,constraints,0,3,1,1);

		add(connect,constraints,2,3,1,1);
		connect.setEnabled(false);
		add(send,constraints,4,1,1,1);
		JScrollPane scroller = new JScrollPane(show);
//		scroller.setPreferredSize(new Dimension(200,200));	
//		add(show,constraints,3,2,3,3);
		add(scroller,constraints,3,2,3,3);
//		scroller.setViewportView(show);
		//loginframe.setResizable(false);

		loginframe.setVisible(true);  
		start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Start();
			}

		});
		connect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				connect();


			}

		});
		send.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				sent(datainput.getText().toString().getBytes());
			}

		});

	}
	
	public void Start() {
		int num=Integer.parseInt(numberinput.getText());
		try {
			ServerSocket serverSocket = new ServerSocket(num);
			//				while(true) {
			Socket socket = serverSocket.accept();
			
			new ServerThread(socket);
			//				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	class ServerThread extends Thread {
		 
	    private Socket socket;
	    InputStream in;

	 
	    public ServerThread(Socket socket) throws IOException {
	        this.socket = socket;
	        in=socket.getInputStream();
	        output=socket.getOutputStream();
	        System.out.println("Client(" + getName() + ") connected...");
	        show.append("Client(" + getName() + ") connected...\r\n");	 
	        start();
	    }
	 
	    @Override
	    public void run() {
	    	while(true) {
	    		byte[] buf=new byte[1024];
	    		try {
	    			in.read(buf);	
	    			System.out.println("Receive:"+new String(buf));
	    			show.append("Receive:"+new String(buf)+"\r\n");
//	    			socket.close();
	    		} catch (IOException e) {
	    			break;
	    		}
	    	}
	    }

	}
	
	public void sent(final byte[] hehe) {
		show.append("Start Send !!\r\n");
		Thread sent_thread=new Thread() {
			public void run() {
				try {
					output.write(hehe);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					show.append("Send Fail !!\r\n");
//					Toast.makeText(getApplicationContext(), "Fail!!!", Toast.LENGTH_SHORT).show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					show.append("Send Fail !!\r\n");
//					Toast.makeText(getApplicationContext(), "Fail!!!", Toast.LENGTH_SHORT).show();
				}			}
		};
		sent_thread.start();
//		sent_thread.interrupt();
	}
	public void connect() {
		show.append("Start Connect !!\r\n");
		int num=Integer.parseInt(numberinput.getText());
		String itip=IPinput.getText().toString();
		new Thread() {
			public void run() {
				try {
					clientSocket = new Socket(itip, num);
					output = clientSocket.getOutputStream();
//					output.write(12);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					show.append("Connect Fail !!\r\n");
//					Toast.makeText(getApplicationContext(), "Fail!!!", Toast.LENGTH_SHORT).show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					show.append("Connect Fail !!\r\n");
//					Toast.makeText(getApplicationContext(), "Fail!!!", Toast.LENGTH_SHORT).show();
				}
			}
		}.start();
	}
//	
	public static void main(String[] args){

		HelloWorld hello=new HelloWorld();}

}