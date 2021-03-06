package matrix;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;

import matrix.zht.ZHTClient;
import matrix.protocol.Metamatrix.MatrixMsg;
import matrix.protocol.Metatask.TaskMsg;
import matrix.util.Configuration;
import matrix.util.MatrixTcpProxy;
import matrix.util.Tools;

public abstract class OverallPeer implements Peer{
	
	//Attributes from Interface Peer
		public ZHTClient zc;
		public Configuration config;
		public ArrayList<String> schedulerList;
		public Boolean running;
		public long numZHTMsg;
		
		public OverallPeer(String configFile) throws IOException{
			config = new Configuration(configFile);
			schedulerList = Tools.readFromFile(config.schedulerMemFile, Charset.defaultCharset());
			running = true;
			numZHTMsg = 0;
			System.loadLibrary("cpp_zhtclient");
			zc = new ZHTClient();
			if(initZhtClient(config.zhtConfigFile, config.zhtMemFile)){
				System.out.println("ZHT Client working!");
			} else{
				System.out.println("ZHT Client!");
			}
		}
		
		public void waitAllScheduler() {

			String key = new String("number of scheduler registered");
			String value;
			value = zc.lookup(key);
			int check = -1;
			while (true) {
				try{ check = Integer.parseInt(value); } catch (Exception e) { }
				if(check == schedulerList.size()) break;
				try{ Thread.sleep(10); } catch (Exception e) { }
				value = zc.lookup(key);
			}
		}

		public void waitAllTaskRecv() {

			String key = new String("num tasks recv");
			String value;
			value = zc.lookup(key);
			long check = -1;
			while (true) {
				try{ check = Long.parseLong(value); } catch (Exception e) { }
				//System.out.println("check and all = " + check + " " + config.numAllTask);
				//TODO:find out why is comming duplicate
				if(check/2 == config.numAllTask) break;
				try{ Thread.sleep(10); } catch(Exception e) { }
				value = zc.lookup(key);
			}
	
		}

		public void increZHTMsgCount(long increment) {
			numZHTMsg += increment;
		}
		
		public void sendBatchTasks(ArrayList<TaskMsg> taskList, Socket sockfd, String peer) {
			
			long numTaskLeft = taskList.size();
			int numTaskBeenSent = 0;
			long numTaskSendPerPkg = config.maxTaskPerPkg;

			MatrixMsg.Builder mmNumTask = MatrixMsg.newBuilder();
			mmNumTask.setMsgType(peer + " send tasks");
			mmNumTask.setCount(numTaskLeft);
			String strNumTask = Tools.mmToStr(mmNumTask.build());

			if (numTaskLeft <= 0) {
				MatrixTcpProxy.sendMul(sockfd, strNumTask, true);
			} else {
				Boolean end = false;
				MatrixTcpProxy.sendMul(sockfd, strNumTask, end);
				while (numTaskLeft > 0) {
					if (numTaskLeft <= config.maxTaskPerPkg) {
						numTaskSendPerPkg = numTaskLeft;
						end = true;
					}

					MatrixMsg.Builder mmTask = MatrixMsg.newBuilder();
					mmTask.setMsgType(peer + " send tasks");
					mmTask.setCount(numTaskSendPerPkg);

					for (int i = 0; i < numTaskSendPerPkg; i++) {
						mmTask.addTasks(Tools.taskMsgToStr(taskList.get(i + numTaskBeenSent)));
					}
					String strTasks = Tools.mmToStr(mmTask.build());
					MatrixTcpProxy.sendMul(sockfd, strTasks, end);
					numTaskLeft -= numTaskSendPerPkg;
					numTaskBeenSent += numTaskSendPerPkg;
				}
			}
		}
		
		public Boolean initZhtClient(String zhtCfgFile, String neighFile){
			if (zhtCfgFile.isEmpty() || neighFile.isEmpty()) {
				return false;
			} else {
				if (zc.init(zhtCfgFile, neighFile) != 0) {
					return false;
				} else {
					return true;
				}
			}
		}
		
		public void insertWrap(String key, String value){
			if (key.isEmpty()) {
				System.out.println("There is empty key!");
				return;
			}
			while (zc.insert(key, value) != 0) {
				try{ Thread.sleep(10); } catch(Exception e) { }
			}
		}

		public void lookupWrap(String key){
			if (key.isEmpty()) {
				return;
			}
			while (zc.lookup(key).isEmpty()) {
				System.out.println("the key is: " + key);
				try{ Thread.sleep(10); } catch(Exception e) { }
			}
		}
		
		public void recvBatchTasks(ArrayList<TaskMsg> taskMsg, int batchNum) {
			
		}

}
