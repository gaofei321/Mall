package joomSignSend;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.allroot.Dao.MyMallAbout;
import com.allroot.entity.JoomUser;
import com.allroot.myMallSynGoods.MyMallSynGoods;
import com.allroot.myMallSynGoods.MyMallSynGoodsThread;
import com.allroot.tool.Log;
import com.allroot.tool.Tools;

public class JoomSignSendThread implements Runnable{

	private List<JoomUser> synList;
	private static Boolean isDebug;
	private final static String jobName = "[同步Joom]";
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public synchronized void SetSynList(ArrayList<JoomUser> arrayList) {
		this.synList = Collections.synchronizedList(arrayList); // 创建同步的对象
	}

	public JoomSignSendThread() {
		super();
	}

	private List<JoomUser> getSynList() {
		return synList;
	}

	public synchronized JoomUser getSynUser() {
		// 线程安全同步
		if (getSynList() == null || getSynList().size() == 0) {
			return null;
		}
		JoomUser user = getSynList().get(0);
		getSynList().remove(0);
		return user;
	}

	// ebayMessage用户列表
	private static ArrayList<JoomUser> getAllSynAccountList() {
		ArrayList<JoomUser> uLists = null;
		try {
			uLists = MyMallAbout.GetAllOtherJoomUser();
		} catch (Exception e1) {
			Log.printLog(jobName + "取待同步的店列表出错:" + Tools.toString(e1.getMessage()));
			uLists = null;
		} 
		return uLists;
	}

	public static void synJoomGoods() {
		ArrayList<JoomUser> uLists = null;
		ArrayList<Thread> threadLists = new ArrayList<Thread>();
		Integer threadCount = 10;// 默认线程数量
		Integer threadRunPreiod = 3;// 线程启动时间间隔
		JoomSignSendThread getThread = new JoomSignSendThread();
		Boolean isFinish = true;

		isDebug = true;
		Integer synPreiod = 15*4*8;// 15分钟一个周期
		Integer runCount = 0;
		Integer finishCount = 0;
		Long finishTime=0l;
		Log.printLog(jobName + "启动成功,每隔" + synPreiod + "分钟下载一次");
		do {
			// runCount相当于等待多少秒，每加1，sleep 1秒
			// threadRunPreiod * threadCount 为线程启动间隔时间和
			if (isFinish && (System.currentTimeMillis()-finishTime>=synPreiod*60*1000)) {
				// 所有线程执行完毕后(isFinish),而且已到下个周期,才能进行下一个同步,等待周期减去线程之间运行的时间间隔
				isFinish = false;
				runCount = 1;
				synPreiod = synPreiod <= 0 ? 15 : synPreiod;
				uLists = getAllSynAccountList();
				if (uLists == null || uLists.size() == 0) {
					Log.printLog(jobName + "没有需要同步的店铺");
					continue;
				}
				getThread.SetSynList(uLists);// 设置线程共享数据
				threadCount = threadCount > 20 ? 20 : threadCount;
				// 配置线程数大于类目数量时,以类目数量为线程数,一个类目一个线程
				threadCount = threadCount > uLists.size() ? uLists.size() : threadCount;
				Log.printLog(jobName + "本次共有[" + uLists.size() + "]个店铺需下载未发货,启用[" + threadCount + "]个线程进行处理.");
				uLists = null;
				threadLists.clear();
				// 创建线程数组
				for (int i = 0; i < threadCount; i++) {
					Thread t = new Thread(getThread, "Active-" + i);
					threadLists.add(t);
					t = null;
				}
				// 运行线程
				// Log.printLog(jobName+"开始执行......");
				for (int j = 0; j < threadLists.size(); j++) {
					threadLists.get(j).start();
					try {
						Thread.sleep(threadRunPreiod * 1000);
					} catch (InterruptedException i1) {
						if (isDebug) {
							Log.printLog("系统不支持休眠!");
						}
						continue;
					}
				}
			}
			// 检查线程是否全部执行完成
			finishCount = 0;
			for (int k = 0; k < threadLists.size(); k++) {
				if (threadLists.get(k).isAlive()) {
					finishCount = 1;
					break;
				}
			}
			isFinish = finishCount > 0 ? false : true;
			// 全部线程完成时,清空线程,等待下一个执行周期
			if (isFinish && threadLists.size() > 0) {
				threadLists.clear();
				finishTime=System.currentTimeMillis();
				Log.printLog(jobName + "执行完毕,每隔" + synPreiod + "分钟下载一次。"
						+ "下次下载时间："
						+dateFormat.format(new Date(System.currentTimeMillis()+synPreiod*60*1000)));
			}
			runCount++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException i0) {
				if (isDebug) {
					Log.printLog("系统不支持休眠!");
				}
			}
		} while (true);

	}

	@Override
	public void run() {
		// 获取user
		JoomUser user = new JoomUser();
		System.out.println("开始同步下载->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		JoomSignSend pro = new JoomSignSend();
		while (true) {
			user = getSynUser();			
			if (user == null) {
				Log.printLog("没有获取到店铺");
				break;
			}
			pro.signSendGoods(user);
		}

	}

	public static void main(String[] args) {
		//同步商品
		synJoomGoods();
	}

	
	
	
	
	
	
	
	
	
	
	
}
