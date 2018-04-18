package com.allroot.myMallSynGoods;

public class MyMallThread {

	
	public static void main(String[] args) {
		//下载未发货订单
		Thread myMallSynGoodsThread=new Thread(new Runnable() {
			public void run() {
				MyMallSynGoodsThread.synMyMallGoods();	
			}
		});
		myMallSynGoodsThread.start();
	/*	//同步售后单纠纷
		Thread JoomSynGoodsThread=new Thread(new Runnable() {
			public void run() {
				WishAfterSalesThread.synWishAfterSales();	
			}
		});
		JoomSynGoodsThread.start();*/
	}
	
	
	
	
	
	
}
