package econnect.main;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;

public class Server extends ServerSocket{

    public static int PORT = Main.portNum2; 
    
    private ServerSocket server;
    private Socket client;
    private DataInputStream dis;
    private FileOutputStream fos;
    
    public Server() throws Exception{
        try {
            try {
                server = new ServerSocket(PORT);
                
                while(true){
                    client = server.accept();
                    
                    dis = new DataInputStream(client.getInputStream());
                    //文件名和长度
                    String fileName = dis.readUTF();
                    long fileLength = dis.readLong();
                    File tmpFile = new File(Constant.DEFAULT_PATH + fileName);
                    if (!tmpFile.exists()) {
						tmpFile.getParentFile().mkdirs();
					}
                    fos = new FileOutputStream(tmpFile);
                    byte[] sendBytes = new byte[1024];
                    int transLen = 0;
                    System.out.println("----开始接收文件<" + fileName + ">,文件大小为<" + fileLength + ">----");
                    while(true){
                        int read = 0;
                        read = dis.read(sendBytes);
                        if(read == -1)
                            break;
                        transLen += read;
                        System.out.println("接收文件进度" + 100 * transLen/fileLength + "%...");
                        fos.write(sendBytes, 0, read);
                        fos.flush();
                    }
                    System.out.println("----接收文件<" + fileName + ">成功-------");
                    fos.flush();
                    fos.close(); //修复已知问题，解决了文件接收文件在未关闭服务器的时候流占用而打不开的问题
                    client.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(dis != null)
                    dis.close();
                if(fos != null)
                    fos.close();
                server.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
