package insightService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;
@Path("/get")
public class RestService {

	
	@GET
	@Path("/pp")
	@Produces("apllication/json")
	public String getResponse(@QueryParam("start") long start , @QueryParam("end") long end , @QueryParam("type") String type )
	{
		DataExporter de= new DataExporter();
		de.initialize();
		JSONObject obj=de.generateJson(start, end, type);
		return 	obj.toString();
	}
	
	
	
	
	
	
	
}
