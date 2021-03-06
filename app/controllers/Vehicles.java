package controllers;

import static models.User.Constant.THEME;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Device;
import models.EventRecord;
import models.EventType;
import models.Fleet;
import models.LiveGPSData;
import models.Schedule;
import models.Vehicle;
import play.mvc.Controller;
import play.mvc.With;
import play.templates.TemplateLoader;
import utils.CommonUtil;
import vo.ComboVO;
import vo.Grid;
import vo.TreeView;
import vo.VehicleEvent;
import vo.VehicleGPS;

import annotations.Perm;

import com.google.gson.Gson;

/**
 * vehicle领域相关的控制器
 * 
 * @author weiwei
 */
@With(Interceptor.class)
public class Vehicles extends Controller {
	
	/**
	 * 统计给定车队的车辆的事件统计信息
	 */
	@Perm
	public static void events(String fleets) {
		long[] fleetsLong = CommonUtil.splitToLong(fleets, ",");

		List<Vehicle> vehicles = Vehicle.filterByFleet(fleetsLong);
		if (vehicles == null)
			renderJSON("");

		List<VehicleEvent> events = new ArrayList<VehicleEvent>(vehicles.size());
		for (Vehicle v : vehicles) {
			if (v.device == null)
				continue;
			
			// ======= 统计事件数据 ======
			List<EventRecord> eventRecords = EventRecord.find("device_key = ?", v.device.key).fetch();
			if (eventRecords == null)
				continue;

			VehicleEvent event = new VehicleEvent();
			event.vehicleNo = v.number;
			event.device = v.device.name;
			event.status = v.device.status;
			event.id = v.id;

			for (EventRecord er : eventRecords) {
				if (EventType.Constant.SUDDEN_ACCELERATION.equals(er.type.techName)) {
					event.suddenAcceleration = EventRecord.count("event_type_tech_name = ? and device_key = ?", er.type.techName, v.device.key);
					continue;
				}

				if (EventType.Constant.SUDDEN_BRAKING.equals(er.type.techName)) {
					event.suddenBrake = EventRecord.count("event_type_tech_name = ? and device_key = ?", er.type.techName, v.device.key);
					continue;
				}

				if (EventType.Constant.SPEEDING.equals(er.type.techName)) {
					event.speeding = EventRecord.count("event_type_tech_name = ? and device_key = ?", er.type.techName, v.device.key);
					continue;
				}

				if (EventType.Constant.SUDDEN_LEFT.equals(er.type.techName)) {
					event.suddenLeftTurn = EventRecord.count("event_type_tech_name = ? and device_key = ?", er.type.techName, v.device.key);
					continue;
				}

				if (EventType.Constant.SUDDEN_RIGHT.equals(er.type.techName)) {
					event.suddenRightTurn = EventRecord.count("event_type_tech_name = ? and device_key = ?", er.type.techName, v.device.key);
					continue;
				}

				if (EventType.Constant.IDLE.equals(er.type.techName)) {
					event.idling = EventRecord.count("event_type_tech_name = ? and device_key = ?", er.type.techName, v.device.key);
					continue;
				}
			}
			event.total = event.countTotal();
			events.add(event);
		}

		Map data = CommonUtil.assemGridData(events, "id");

		renderJSON(data);
	}

	/**
	 * 获取给定车队的车辆的GPS实时信息
	 */
	@Perm
	public static void gps(String fleets) {
		long[] fleetsLong = CommonUtil.splitToLong(fleets, ",");
		List<Vehicle> vehicles = Vehicle.filterByFleet(fleetsLong);
		List<VehicleGPS> vehicleGps = Vehicle.findGPS(vehicles);

		renderJSON(vehicleGps);
	}

	/**
	 * 车辆管理：检索车辆信息
	 */
	@Perm
	public static void search(int page, int pageSize, String number, String license, String fleetName, String deviceName, String description, String cctvIp, String type) {
		Map map = Vehicle.search(page, pageSize, number, license, fleetName, deviceName, description, cctvIp, type);
		renderJSON(map);
	}

	/**
	 * 车辆管理：更新车辆信息
	 */
	@Perm
	public static void update(String models) {
		if (Vehicle.updateByJson(models))
			renderJSON(models);
	}

	/**
	 * 获取所有车辆信息
	 */
	@Perm
	public static void read(int page, int pageSize) {
		renderJSON(Vehicle.search(page, pageSize, null));
	}

	/**
	 * 车辆管理：删除车辆信息
	 */
	@Perm
	public static void destroy(String models) {
		try {
			if (Vehicle.deleteByJson(models))
				renderJSON(models);
		} catch (Throwable e) {
			throw new RuntimeException("Vehicle Destroy Error -> " + e.getMessage());
		}
	}

	/**
	 * 车辆管理：添加车辆信息
	 */
	@Perm
	public static void add(String models) {
		renderJSON(Vehicle.createByJson(models));
	}

	/**
	 * 访问车辆管理页面
	 */
	@Perm
	public static void grid(String id) {
		final String preUrl = "/Vehicles/";
		Map map = new HashMap();
		Grid grid = new Grid();
		grid.tabId = id; // vehicles
		grid.createUrl = preUrl + "add";
		grid.updateUrl = preUrl + "update";
		grid.destroyUrl = preUrl + "destroy";
		grid.readUrl = preUrl + "read";
		grid.searchUrl = preUrl + "search";
		grid.editable = "popup";
		
		// fleet combobox
		List<Fleet> fleetList = Fleet.findAll();
		List<ComboVO> fleets = new ArrayList<ComboVO>();
		if (fleetList != null)
			for (Fleet fleet : fleetList) 
				fleets.add(new ComboVO(fleet.name, fleet.name));
			
		map.put("fleets", CommonUtil.getGson().toJson(fleets));

		// device combobox
		List<Device> deviceList = Device.findAll();
		List<ComboVO> devices = new ArrayList<ComboVO>();
		if (deviceList != null)
			for (Device device : deviceList) 
				devices.add(new ComboVO(device.name, device.name));
			
		map.put("devices", CommonUtil.getGson().toJson(devices));
		map.put("grid", grid);
		
		renderHtml(TemplateLoader.load(template(renderArgs.get(THEME) + "/Vehicles/grid.html")).render(map));
	}

	/**
	 * 访问车队树形结构
	 */
	@Perm
	public static void tree() {
		Map map = new HashMap();

		List<Fleet> fleetList = Fleet.findAll();
		List<ComboVO> fleets = new ArrayList<ComboVO>();
		if (fleetList != null)
			for (Fleet fleet : fleetList) 
				fleets.add(new ComboVO(fleet.name, fleet.id));
			
		map.put("fleets", CommonUtil.getGson().toJson(fleets));
		List<TreeView> tree = Vehicle.assemVehicleTreeAndFleetTree(null);
		String vehicleJson = new Gson().toJson(tree);
		map.put("treeData", vehicleJson);
		
		renderHtml(TemplateLoader.load(template(renderArgs.get(THEME) + "/Vehicles/tree.html")).render(map));
	}

	/**
	 * 查询车辆信息，车队保持树形结构
	 */
	@Perm
	public static void searchTree(long fleetid, String number) {
		Map map = new HashMap();

		List<Fleet> fleetList = Fleet.findAll();
		List<ComboVO> fleets = new ArrayList<ComboVO>();
		if (fleetList != null)
			for (Fleet fleet : fleetList) 
				fleets.add(new ComboVO(fleet.name, fleet.id));
			
		map.put("fleets", CommonUtil.getGson().toJson(fleets));
		
		String vehicleJson = new Gson().toJson(Vehicle.assemVehicleTreeByFleetIdAndNumber(fleetid, number));
		
		renderJSON(vehicleJson);
	}

	/**
	 * 打开某辆车的路径显示窗口
	 */
	@Perm
	public static void path(String vehicleNo) {
		List<ComboVO> vc = Vehicle.getCombo();
		String vehicles = CommonUtil.getGson().toJson(vc);

		List<ComboVO> sch = Schedule.getComboByVehicle(vehicleNo);
		String schedules = CommonUtil.getGson().toJson(sch);

		render(renderArgs.get(THEME) + "/Vehicles/path.html", vehicleNo, vehicles, schedules);
	}

	/**
	 * 给定车牌号码查询对应的工作安排信息
	 */
	@Perm
	public static void schedules(String vehicleNo) {
		List<ComboVO> schedules = Schedule.getComboByVehicle(vehicleNo);
		renderJSON(schedules);
	}

	/**
	 * 查询车辆的行驶路径
	 */
	@Perm
	public static void searchPath(String vehicleNo, String startDate, String startTime, String endDate, String endTime) {
		List<String[]> points = Vehicle.routeGPS(-1, -1, vehicleNo, startDate, startTime, endDate, endTime);

		renderJSON(points);
	}

}
