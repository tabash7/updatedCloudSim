package org.cloudbus.cloudsim.ex.mapreduce;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.ex.mapreduce.models.request.Job;
import org.cloudbus.cloudsim.ex.mapreduce.models.request.Request;
import org.cloudbus.cloudsim.ex.mapreduce.models.request.UserClass;
import org.cloudbus.cloudsim.ex.util.Id;

import EDU.oswego.cs.dl.util.concurrent.WaitableLong;


public class WorkloadRequestFromFile {
	
    public int id;
    public double submissionTime;
    public double budget;
    public int len;
    public Job job;
   // public UserClass userClass;
    public String jobFile;
    public String policy;
    public double deadline;
    public String userClassValue;
    
    List<WorkloadRequestFromFile> wl = new ArrayList<WorkloadRequestFromFile>();

	//WorkloadRequestFromFile wglNew = new WorkloadRequestFromFile(budget,runTime,userID,jobFile,policy,submissionTime,userClass);

		public WorkloadRequestFromFile(double budget, double deadline, int userID, String jobFile,String policy, double submissionTime, String userClassValue) {
    //	System.err.println(" inside worloadRequest , , , , " + deadline);

		id = Id.pollId(WorkloadRequestFromFile.class);

    	this.submissionTime = submissionTime;

    	DecimalFormat df = new DecimalFormat(".00");
//    	this.len =  len;

    	this.budget = Double.valueOf(df.format(budget));
    	this.deadline = Double.valueOf(df.format(deadline));
    	this.jobFile = jobFile;
    	this.userClassValue = userClassValue;
    	this.policy = policy;

    	
    	//System.err.println("Workload request added " + id );
    	
    	//wl.add("budget,deadline,userID,jobFile,policy,submissionTime,userClass");
    	

    	
	}
		
		
    	
		public List<WorkloadRequestFromFile> getWl() {
			return wl;
		}


		public void setWl(List<WorkloadRequestFromFile> wl) {
			this.wl = wl;
		}
		

    
    
    
    
    
    

}
