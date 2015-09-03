package org.cloudbus.cloudsim.ex.mapreduce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.ex.mapreduce.models.request.Request;
import org.cloudbus.cloudsim.ex.mapreduce.models.request.Requests;
import org.cloudbus.cloudsim.ex.mapreduce.models.request.UserClass;
import org.cloudbus.cloudsim.ex.mapreduce.policy.Policy.CloudDeploymentModel;

public class Workload


{
	protected List<? extends WorkloadRequestFromFile> WlList;

    String policy;
    CloudDeploymentModel cloudDeploymentModel = CloudDeploymentModel.Hybrid;
    Map<UserClass, Double> userClassAllowedPercentage = new HashMap<UserClass, Double>();
    Requests requests;

    public Workload(String policy, String cloudDeploymentModel, Map<String, Double> userClassAllowedPercentage,
	    ArrayList<Request> requests)
    {
	this.policy = policy;

	try
	{
	    this.cloudDeploymentModel = CloudDeploymentModel.valueOf(cloudDeploymentModel);
	} catch (Exception ex)
	{
	    Log.printLine(ex.getMessage());
	    Log.printLine("CloudDeploymentModel.Hybrid will be used");
	}

	for (Map.Entry<String, Double> userClassMap : userClassAllowedPercentage.entrySet()) {
	    UserClass userClass = UserClass.valueOf(userClassMap.getKey());
	    this.userClassAllowedPercentage.put(userClass, userClassMap.getValue());
	}
	
//	for(int i=0;i<requests.size();i++){
//		
//		System.err.println("Request is " + requests.get(i).jobFile);
//		
//	}
	
	this.requests = new Requests(requests);

	for (Request request : this.requests.requests)
	{
	    request.policy = policy;
	    request.setCloudDeploymentModel(CloudDeploymentModel.valueOf(cloudDeploymentModel));
	}
	
	
	
    }
    
    public Workload( ArrayList<WorkloadRequestFromFile> requests)
        {
//       	System.err.println(" inside worloadRequest , , , , " + requests.size());
       
       	setWlList(new ArrayList<WorkloadRequestFromFile>());

//       	System.err.println("##################################");
    	
    	this.policy = policy;
    	try
    	{

           	System.err.println( " inside asdasd , , , , " + requests.size() );
           

    		
    	} catch (Exception ex)
    	{
    	    Log.printLine(ex.getMessage());
    	    Log.printLine("CloudDeploymentModel.Hybrid will be used");
    	}
    	


    
//    	
//    	for(int i=0;i<requests.size();i++){
//    		
//    		System.err.println("Request is from WorkloadRequestFromFile " + requests.get(i).deadline);
//    		
//    	}
    	
    	//this.requests = new Requests(requests);

//    	for (Request request : this.requests.requests)
//    	{
//    	    request.policy = policy;    	}
//    	
//    	
//    	
        }
    
	@SuppressWarnings("unchecked")
	public <T extends WorkloadRequestFromFile> List<T> getWlList() {
		return (List<T>) WlList;
	}

	/**
	 * Sets the vm list.
	 * 
	 * @param <T> the generic type
	 * @param vmList the new vm list
	 */
	protected <T extends WorkloadRequestFromFile> void setWlList(List<T> WlList) {
		this.WlList = WlList;
	}
}
