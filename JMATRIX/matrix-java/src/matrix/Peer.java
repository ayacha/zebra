package matrix;

import java.net.Socket;
import java.util.ArrayList;

import matrix.protocol.Metatask.TaskMsg;

public interface Peer {
	
	
	Boolean initZhtClient(String something, String something2);

	void waitAllScheduler();
	void waitAllTaskRecv();

	void setId(String id);

	String getId();

	void setIndex(Integer index);

	int getIndex();

	void increZHTMsgCount(long count);

	void insertWrap(String key, String value);

	void lookupWrap(String key);

	void sendBatchTasks(ArrayList<TaskMsg> taskMsg, Socket socket, String peer);
	void recvBatchTasks(ArrayList<TaskMsg> taskMsg, int batchNum);

}
