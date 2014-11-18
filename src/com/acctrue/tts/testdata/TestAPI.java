package com.acctrue.tts.testdata;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.acctrue.tts.dto.LoginResponse.UserInfo;
import com.acctrue.tts.enums.OrderFlagEnum;
import com.acctrue.tts.model.BizCorp;
import com.acctrue.tts.model.FarmLands;
import com.acctrue.tts.model.Farmers;
import com.acctrue.tts.model.OrderInfo;
import com.acctrue.tts.model.Product;
import com.acctrue.tts.model.StoreCode;
import com.acctrue.tts.model.Warehouse;
import com.acctrue.tts.utils.AccountUtil;
import com.acctrue.tts.utils.DateUtil;

@Deprecated
public final class TestAPI {
	
	static public boolean userLogin(String n,String p){
		return true;
	}
	
	static public UserInfo getCurrentUser(){
		return AccountUtil.getCurrentUser().getUserInfo();
	}
	
	static public List<Product> getTestProducts(){
		List<Product> list = new ArrayList<Product>();
		list.add(new Product(1001,"苹果"));
		list.add(new Product(1002,"李子"));
		list.add(new Product(1003,"梨"));
		list.add(new Product(1004,"葡萄"));
		list.add(new Product(1005,"香蕉"));
		list.add(new Product(1006,"萝卜"));
		list.add(new Product(1007,"白菜"));
		list.add(new Product(1008,"土豆"));
		list.add(new Product(1009,"黄瓜"));
		list.add(new Product(10010,"豆角"));
		list.add(new Product(10011,"香菜"));
		return list;
	}
	
	static public List<Farmers> getFarmerList(){
		List<Farmers> list = new ArrayList<Farmers>();
		list.add(new Farmers("A001","农户1"));
		list.add(new Farmers("A002","农户2"));
		list.add(new Farmers("A003","农户3"));
		return list;
	}
	
	static public List<FarmLands> getFarmLandsList(){
		List<FarmLands> list = new ArrayList<FarmLands>();
		list.add(new FarmLands("N001","农田1"));
		list.add(new FarmLands("N002","农田2"));
		list.add(new FarmLands("N003","农田3"));
		list.add(new FarmLands("N003","农田4"));
		return list;
	}
	
	static public List<Warehouse> getWarehouseList(){
		List<Warehouse> list = new ArrayList<Warehouse>();
		list.add(new Warehouse(0,1,"仓库1"));
		list.add(new Warehouse(0,2,"仓库2"));
		list.add(new Warehouse(0,3,"仓库3"));
		return list;
	}
	
	static public List<BizCorp> getBizCorps(){
		List<BizCorp> list = new ArrayList<BizCorp>();
		
		BizCorp b1 = new BizCorp();
		b1.setCorpId(1);
		//b1.setCurrentCorpId(1);
		b1.setCorpName("中国移动");
		b1.setCorpCode("cmcc");
		b1.setCorpPinYin("zgyd");
		//b1.setCorpNumber("A001");
		list.add(b1);
		
		BizCorp b2 = new BizCorp();
		b2.setCorpId(2);
		//b2.setCurrentCorpId(2);
		b2.setCorpName("中国联通");
		b2.setCorpCode("chinaunicom");
		b2.setCorpPinYin("zglt");
		//b2.setCorpNumber("A002");
		list.add(b2);
		
		return list;
	}
	
	static public List<OrderInfo> getOrderInfoList(){
		List<OrderInfo> list = new ArrayList<OrderInfo>();
		OrderInfo o1 = new OrderInfo();
		o1.setStoreId(UUID.randomUUID().toString());
		o1.setOrderType(0);
		o1.setOrderNo("201410170001");
		o1.setOrderKind("类别1");
		o1.setOrderTypeText("销售出库");
		o1.setBizCorpCode("A001");
		o1.setBizCorpName("首农");
		o1.setBizCorpCodeZD("B001");
		o1.setBizCorpNameZD("中农");
		o1.setActor(TestAPI.getCurrentUser().getUserName());
		o1.setActDate(DateUtil.getDatetime());
		o1.setFlag(OrderFlagEnum.Task.getId());
		o1.setUserId(0);
		list.add(o1);
		
		
		OrderInfo o2 = new OrderInfo();
		o2.setStoreId(UUID.randomUUID().toString());
		o2.setOrderType(0);
		o2.setOrderNo("201410170002");
		o2.setOrderKind("类别2");
		o2.setOrderTypeText("退货入库");
		o2.setBizCorpCode("A002");
		o2.setBizCorpName("公司2");
		o2.setBizCorpCodeZD("B002");
		o2.setBizCorpNameZD("公司直接2");
		o2.setActor(TestAPI.getCurrentUser().getUserName());
		o2.setActDate(DateUtil.getDatetime());
		o2.setFlag(OrderFlagEnum.Task.getId());
		o2.setUserId(0);
		list.add(o2);
		
		return list;
	}
	
	static public StoreCode getStoreCode(String storeId,String cid){
		StoreCode sc = new StoreCode();
		sc.setStoreId(cid);
		sc.setId(UUID.randomUUID().toString());
		sc.setCreateTime(DateUtil.getDatetime());
		sc.setCodeId(cid);
		return sc;
	}
	
	static public StoreCode getStoreCode(String storeId){
		return getStoreCode(storeId,UUID.randomUUID().toString());
	}

}
