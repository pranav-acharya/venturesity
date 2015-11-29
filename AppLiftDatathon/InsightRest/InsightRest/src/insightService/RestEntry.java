package insightService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.sun.jersey.json.impl.provider.entity.JSONObjectProvider;

@Path("/insight")
public class RestEntry {
	
@Path("/{filid}")
@GET
@Produces("application/json")
public Response  runJob(@PathParam("gileid") String filId)
{
	ExecutorCall exe= new ExecutorCall();
	 exe.execute(filId);
	return Response.ok().build();
}

@Path("/publish")
@POST
@Produces("application/json")
@Consumes("application/json")
public String executeJob(InputStream input)
{
	StringBuilder builder = new StringBuilder();
	try{
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		String line ;
		while( (line=reader.readLine() )!=null)
		{
			builder.append(line);
		}
		JSONObject obj = new JSONObject(builder.toString());
		ExecutorCall exe= new ExecutorCall();
		exe.execute(obj);
		System.out.println(obj.toString());
		return obj.toString();
	}
	catch (Exception e)
	{
		
	}
	
	return " ";
}


}
