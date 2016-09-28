package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ericsson.cmpp.MessageFormat;
import com.ericsson.protocol.CMPP;


public class Login {

	public static int pktLen;
	public static int reqId;
	public static int seqId;
	public static byte[] source_Addr = new byte[6];
	public static byte[] authenticatorSource = new byte[16];
	public static int timeStamp;
	public static byte version;

	private String strtimeStamp;
	private String loginPwd = "100811";
	private String spId = "100811";
	private String spPwd;

	public Login(int i) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		pktLen = 39;
		reqId = 0x00000001;
		version = 48;
		seqId = i;
		source_Addr = spId.getBytes();
		SimpleDateFormat sf = new SimpleDateFormat("MMddHHmmss");
		strtimeStamp = sf.format(new Date());
		timeStamp = Integer.valueOf(strtimeStamp).intValue();
		byte[] testa = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
		spPwd = spId + new String(testa) + loginPwd + strtimeStamp;
		authenticatorSource = Login.Md5(spPwd);
	}
	
	public static String readString(ByteBuffer buffer, int length) {
		byte[] barray = new byte[length];
		buffer.get(barray);
		return new String(barray);
	}

	public static void main(String[] args) throws UnknownHostException, IOException, NoSuchAlgorithmException {
		Socket socket = null;
		socket = new Socket("150.236.58.58", 7890);
		new Login(1);
		OutputStream os = socket.getOutputStream();
		os.write(Login.toBytes());
		os.flush();
		InputStream inputStream = socket.getInputStream();

		byte[] buffer = new byte[1024];
		int len = -1;
		if ( (len=inputStream.read(buffer)) != -1) {
			ByteBuffer byteBuf = ByteBuffer.wrap(buffer);
			int length = byteBuf.getInt();
			int type = byteBuf.getInt();
			int seq = byteBuf.getInt();
			int status = byteBuf.getInt();
			String isgm = Login.readString(byteBuf, 16);
			int version = byteBuf.get();
			
			System.out.printf( "%d, %d,%d\n", length, type, seq);
			System.out.println( "status = " + status);
			System.out.println( "isgm = " + new String(isgm.getBytes(), "UTF8"));
			System.out.println( "version = " + version);
			
		}
                
                int count = 0;
                while ( count < 100) {

                    ByteBuffer message = PacketGenerator.createMessage("100811", MessageFormat.ASCII, "13929873498", new String[] {"13109329832"}, "Hello World Test Send Message");
		
                    os.write(message.array());
                    os.flush();
		
                    if ( (len = inputStream.read(buffer)) != -1) {
                        ByteBuffer buf = ByteBuffer.wrap(buffer);
                        int length = buf.getInt();
                        int cmdType = buf.getInt();
                        String cmdTypeStr = null;
                    
                        if (cmdType == CMPP.CMPP_SUBMIT_RESP) {
                            cmdTypeStr = "CMPP_SUBMIT_RESP";
                        }
                        int seq = buf.getInt();
                        long msgId = buf.getLong();
                        int result = buf.get();
                    
                        System.out.printf("%d %s %d\n", length, cmdTypeStr, seq);
                        System.out.println( "Result " + result);
                        count++;
                    
                    }
                }
	}

	private static byte[] Md5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(str.getBytes("UTF8"));
		byte[] temp;
		temp = md5.digest("".getBytes("UTF8"));
		return temp;
	}

	public static byte[] toBytes() {
		byte[] b = new byte[39];
		ByteBuffer bb = ByteBuffer.wrap(b, 0, 39);
		bb.order(ByteOrder.BIG_ENDIAN);
		bb.putInt(pktLen);
		bb.putInt(reqId);
		bb.putInt(seqId);
		bb.put(source_Addr);
		bb.put(authenticatorSource);
		bb.put(version);
		bb.putInt(timeStamp);

		return b;
	}
	
	public static String toFixLengthString( String str, int length) {
		String format = String.format("%%%ds", length);
		return String.format(format, str).replace(" ", "0");
	}

	public static byte[] messageToBytes() {
		int Total_Length = 207;
		int Command_Id = 4;
		int Sequence_Id = 2;
		byte[] Msg_id = new byte[8];
		byte PK_total = 1;
		byte PK_number = 1;
		byte Registered_Delivery = 1;
		String Service_Id = "9999";
		byte Msg_level = 1;
		byte Fee_UserType = 0;
		byte[] Fee_terminal_Id = new byte[32];
		byte TP_pId = 0;
		byte TP_udhi = 0;
		byte Msg_Fmt = 0;
		byte[] Msg_src = new byte[6];
		byte[] FeeType = new byte[2];
		byte[] FeeCode = new byte[6];
		String ValId_Time = "00000000000000000";
		String At_Time = "00000000000000000";
		byte[] Src_Id = new byte[21];
		byte DestUsr_tl;
		byte[] Dest_terminal_Id = new byte[32];
		byte Msg_Length;
		byte[] Msg_Content;
		byte[] Reserve = new byte[20];
		Msg_src = "200030".getBytes();
		FeeType = "01".getBytes();
		FeeCode = "000010".getBytes();
		Src_Id = "000000000001069034531".getBytes();
		DestUsr_tl = 1;
		Dest_terminal_Id = "00000000000000000000018511790624".getBytes();
		Msg_Length = 12;
		Msg_Content = "digitalchina".getBytes();
		byte[] b = new byte[207];
		ByteBuffer bb = ByteBuffer.wrap(b, 0, 207);
		bb.order(ByteOrder.BIG_ENDIAN);
		bb.putInt(Total_Length);
		bb.putInt(Command_Id);
		bb.putInt(Sequence_Id);
		bb.put(Msg_id);
		bb.put(PK_total);
		bb.put(PK_number);
		bb.put(Registered_Delivery);
		bb.put(Msg_level);
		bb.put(Service_Id.getBytes());
		bb.put(Fee_UserType);
		bb.put(Fee_terminal_Id);
		bb.put(TP_pId);
		bb.put(TP_udhi);
		bb.put(Msg_Fmt);
		bb.put(Msg_src);
		bb.put(FeeType);
		bb.put(FeeCode);
		bb.put(ValId_Time.getBytes());
		bb.put(At_Time.getBytes());
		bb.put(Src_Id);
		bb.put(DestUsr_tl);
		bb.put(Dest_terminal_Id);
		bb.put(Msg_Length);
		bb.put(Msg_Content);
		bb.put(Reserve);
		bb.position();
		return b;
	}
}