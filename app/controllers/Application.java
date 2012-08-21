package controllers;

import static models.User.Constant.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import notifiers.MyMailer;

import models.Driver;
import models.DriverReport;
import models.Fleet;
import models.User;

import play.cache.Cache;
import play.mvc.Controller;
import play.mvc.With;
import vo.TreeView;

import com.google.gson.Gson;

/**
 * Application entry contoller
 * @author weiwei
 *
 */
@With({Interceptor.class})
public class Application extends Controller {
	
	/**
	 * Home page
	 */
    public static void index() {
        List<TreeView> tree = Fleet.assemFleetTree();
        String fleetJson = new Gson().toJson(tree);
        renderTemplate(renderArgs.get(THEME) + "/Application/index.html", fleetJson);
    }
    
	/**
	 * Management
	 */
    public static void admin(){
    	render(renderArgs.get(THEME) + "/Application/admin.html");
    }
    
}