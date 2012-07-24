package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;
import utils.CommonUtil;

import com.alibaba.fastjson.JSON;

/**
 * 设备,可以被分配到某一辆车上
 * 
 * @author weiwei
 * 
 */
@Entity
@Table(name = "t_device")
public class Device extends Model {

	@Column(unique = true)
	public String name;
	
	public String host;
	
	@Column(name="device_key", unique = true)
	public String key;
	
	public boolean alive;
	
	/* this is a json */
	@Column(name="server_urls", columnDefinition="TEXT")
	public String serverUrls;
	
	@Column(columnDefinition="TEXT")
	public String action;
	
	@Column(columnDefinition="TEXT")
	public String misc;
	
	public String status;

	@Override
	public String toString() {
		return "Device [name=" + name + ", host=" + host + ", key=" + key
				+ ", alive=" + alive + ", serverUrls=" + serverUrls
				+ ", action=" + action + ", misc=" + misc + ", status="
				+ status + "]";
	}

	public void validate(){
		final StringBuilder builder = new StringBuilder();
		final String msg = "%s Can't be empty, ";
		if (CommonUtil.isEmptyString(name))
			builder.append(CommonUtil.formatStr(msg, "Name"));
		
		if (CommonUtil.isEmptyString(key))
			builder.append(CommonUtil.formatStr(msg, "Key"));
		
		if (CommonUtil.isEmptyString(host))
			builder.append(CommonUtil.formatStr(msg, "Host"));
		
		if (CommonUtil.isEmptyString(status))
			builder.append(CommonUtil.formatStr(msg, "Status"));
		
		if (!"online".equals(status) && !"offline".equals(status))
			builder.append(String.format("%s is online or offline", "Status"));
		
		final String result = builder.toString();
		if (result.trim().length() > 0)
			throw new RuntimeException(result);
	}
	
	public static Device findByName(String deviceName) {
		return find("byName", deviceName).first();
	}
	
	public static String createByJson(String models) {
		List<Device> vos = CommonUtil.parseArray(models, Device.class);
		if (vos == null)
			return models;
		
		for (Device vo : vos){
			vo.validate();
			vo.create();
		}
		
		final String _models = CommonUtil.toJson(vos);
		
		return _models;
	}

	public static boolean deleteByJson(String models) {
		List<Device> vos = JSON.parseArray(models, Device.class);
		if (vos == null)
			return false;
		
		for (Device vo : vos){
			Device device = Device.findById(vo.id);
			if (device == null)
				continue ;
			
			device.delete();
		}
		
		return true;
	}

	public static boolean updateByJson(String models) {
		List<Device> vos = JSON.parseArray(models, Device.class);
		if (vos == null)
			return false;
		
		for (Device vo : vos){
			vo.validate();
			
			Device device = Device.findById(vo.id);
			if (device == null)
				continue ;
		
			device.name = vo.name;
			device.key = vo.key;
			device.action = vo.action;
			device.alive = vo.alive;
			device.host = vo.host;
			device.misc = vo.misc;
			device.serverUrls = vo.serverUrls;
			device.status = vo.status;
			
			device.save();
		}
		
		return true;
	}
	
	public static List<Device> findByCondition(int page, int pageSize, String name, String key, String host){
		final List<Object> params = new ArrayList<Object>();
		final StringBuilder sb = new StringBuilder();
		parseCondition(name, key, host, params, sb);
		List<Device> devices = null;
		
		if (page <= 0 || pageSize <= 0)
			devices = Device.find(sb.toString(), params.toArray()).fetch() ;
		else
			devices = Device.find(sb.toString(), params.toArray()).fetch(page, pageSize) ;
		
		return devices;
	}
	
	public static long countByCondition(String name, String key, String host){
		final List<Object> params = new ArrayList<Object>();
		final StringBuilder sb = new StringBuilder();
		parseCondition(name, key, host, params, sb);
		
		return Device.count(sb.toString(), params.toArray());
	}
	
	
	private static void parseCondition(String name, String key, String host,
			final List<Object> params, final StringBuilder sb) {
		if (name != null && !name.isEmpty()){
			sb.append("name like ?");
			params.add(new StringBuilder("%").append(name).append("%").toString());
		}
		
		if (key != null && !key.isEmpty()){
			if (sb.length() > 0)
				sb.append(" and ");
			
			sb.append("key like ?");
			params.add(new StringBuilder("%").append(key).append("%").toString());
		}
		
		if (host != null && !host.isEmpty()){
			if (sb.length() > 0)
				sb.append(" and ");
			
			sb.append("host like ?");
			params.add(new StringBuilder("%").append(host).append("%").toString());
		}
	}
}
