package controllers.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.Driver;
import models.Permission;
import models.Fleet;
import models.Role;
import models.User;
import models.Vehicle;
import play.cache.Cache;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.With;
import vo.FleetVO;
import controllers.Interceptor;

/**
 * Fleets API
 * @author weiwei
 *
 */
@With(APIInterceptor.class)
public class Fleets extends Controller{
	
	/**
	 * Fetch fleet's info
	 */
	public static void index(final int page, final int pageSize, final FleetVO fleet){
		try{
			renderJSON(APICallback.success(Fleet.search(page, pageSize, fleet)));
		}catch(Throwable e){
			renderJSON(APICallback.fail(APIError.FLEET_FETCH_FAIL, e.getMessage()));
		}
	}
	
	/**
	 * Get fleet info
	 */
	public static void show(Long id){
		try{
			Fleet fleet = Fleet.fetchById(id);
			renderJSON(APICallback.success(new FleetVO().init(fleet)));
		}catch(Throwable e){
			renderJSON(APICallback.fail(APIError.FLEET_FETCH_FAIL, e.getMessage()));
		}
	}
	
	/**
	 * Create fleet info
	 */
	public static void create(final FleetVO fleet) {
		
		try{
			FleetVO _fleet = Fleet.createByVO(fleet);
			renderJSON(APICallback.success(_fleet));
		} catch (Throwable e){
			renderJSON(APICallback.fail(fleet, APIError.FLEET_CERATE_FAIL, e.getMessage()));
		}
	}
	
	/**
	 * Update fleet info
	 */
	public static void update(final FleetVO fleet){
		
		try{
			Fleet.updateByVO(fleet);
			renderJSON(APICallback.success(fleet));
		} catch (Throwable e){
			renderJSON(APICallback.fail(APIError.FLEET_UPDATE_FAIL, e.getMessage()));
		}
	}
	
	/**
	 * Delete fleet info
	 * @param id
	 */
	public static void destroy(Long id) {
		try{
			Fleet.deleteById(id);
			renderJSON(APICallback.success(id));
		} catch (Throwable e){
			renderJSON(APICallback.fail(id, APIError.FLEET_DESTROY_FAIL, e.getMessage()));
		}
	}
	/**
	 * Assign Drivers to a Permission
	 */
	public static void createRelations(Long id, List<Long> vehicle_ids, List<Long> leader_ids) {
		try{
			Fleet.assign(id, vehicle_ids, leader_ids, false);
			renderJSON(APICallback.success());
		} catch (Throwable e){
			renderJSON(APICallback.fail(APIError.FLEET_ASSIGN_FAIL, e.getMessage()));
		}
	}
	
	public static void destroyRelations(Long id, List<Long> vehicle_ids, List<Long> leader_ids) {
		try{
			Fleet.unassign(id, vehicle_ids, leader_ids);
			renderJSON(APICallback.success());
		} catch (Throwable e){
			renderJSON(APICallback.fail(APIError.FLEET_UNASSIGN_FAIL, e.getMessage()));
		}
	}
	
	public static void relations(Long id) {
		try{
			Fleet fleet = Fleet.fetchById(id);
			Map map = new HashMap();
			map.put("vehicles", Vehicle.toIds(fleet.vehicles));
			map.put("leaders", Driver.toIds(fleet.leaders));
			
			renderJSON(APICallback.success(map));
		} catch (Throwable e){
			renderJSON(APICallback.fail(APIError.FLEET_FETCH_FAIL, e.getMessage()));
		}
	}
	
	public static void assignables(){
		try{
			List<Vehicle> validList = Vehicle.assignables();
			Map map = new HashMap();
			map.put("vehicle", Vehicle.toIds(validList));
			
			renderJSON(APICallback.success(map));
		} catch (Throwable e){
			renderJSON(APICallback.fail(APIError.FLEET_FETCH_FAIL, e.getMessage()));
		}
	}
}
