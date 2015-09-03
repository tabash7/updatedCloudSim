package org.cloudbus.cloudsim.ex.mapreduce;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.ex.mapreduce.models.cloud.Cloud;
import org.cloudbus.cloudsim.ex.mapreduce.models.cloud.VmInstance;
import org.cloudbus.cloudsim.ex.mapreduce.models.request.Request;
import org.cloudbus.cloudsim.ex.mapreduce.models.request.Requests;
import org.cloudbus.cloudsim.ex.mapreduce.models.request.Task;
import org.cloudbus.cloudsim.ex.mapreduce.models.request.UserClass;
import org.cloudbus.cloudsim.ex.mapreduce.policy.Policy.CloudDeploymentModel;
import org.cloudbus.cloudsim.ex.util.CustomLog;

import util.WorkloadFileReader;

/**
 * This class contains the main method for execution of the simulation. Here,
 * simulation parameters are defined. Parameters that are dynamic are read from
 * the properties file, whereas other parameters are hardcoded.
 * 
 * Decision on what should be configurable and what is hardcoded was somehow
 * arbitrary. Therefore, if you think that some of the hardcoded values should
 * be customizable, it can be added as a Property. In the Property code there is
 * comments on how to add new properties to the experiment.
 * 
 */
public class Simulation {

    // From Cloud.yaml
    private static Cloud cloud;

    // From Requests.yaml
    private static Requests requests;

    private static MapReduceEngine engine;

    private static java.util.Properties props = new java.util.Properties();
    
    private static List<WorkloadRequestFromFile> RequestsByGEN;
    
    private static config conf;
    
    //Request list from workload generator
    
    private static List<WorkloadRequestFromFile> RequestListGen;
	static List<Request> NewList = new ArrayList<Request>();



    /**
     * Prints input parameters and execute the simulation a number of times, as
     * defined in the configuration.
     * 
     * @throws Exception
     * 
     */
    public static void main(String[] args) throws Exception {

	Log.printLine("========== Simulation configuration ==========");
	for (Properties property : Properties.values()) {
	    Log.printLine("= " + property + ": " + property.getProperty());
	}
	Log.printLine("==============================================");
	Log.printLine("");

	String[] experimentFilesName = Properties.EXPERIMENT.getProperty().split(",");
	if (experimentFilesName.length == 0)
	    experimentFilesName = new String[] { Properties.EXPERIMENT.getProperty() };

	// default.log logging
	try (InputStream is = Files.newInputStream(Paths.get("custom_log.properties"))) {
	    props.load(is);
	}
	CustomLog.configLogger(props);

	// vms.csv logging
	CustomLog.redirectToFile("output/logs/vms.csv");
	CustomLog.printHeader(VmInstance.class, ",",
		new String[] { "ExperimentNumber", "WorkloadNumber", "RequestId", "J", "UserClass",
			"Policy", "CloudDeploymentModel", "Id", "Name", "ExecutionTime", "ExecutionCost",
			"TasksIdAsString" });
	// tasks.csv logging
	CustomLog.redirectToFile("output/logs/tasks.csv");
	CustomLog.printHeader(Task.class, ",", new String[] { "ExperimentNumber", "WorkloadNumber", "RequestId", "J",
		"UserClass",
		"Policy", "CloudDeploymentModel", "CloudletId", "TaskType", "CloudletLength",
		"CloudletStatusString", "SubmissionTime", "ExecStartTime", "FinalExecTime", "FinishTime",
		"InstanceVmId", "VmType" });
	// requests.csv logging
	CustomLog.redirectToFile("output/logs/requests.csv");
	CustomLog.printHeader(Request.class, ",", new String[] { "ExperimentNumber", "WorkloadNumber", "Id", "J",
		"UserClass", "Policy",
		"CloudDeploymentModel", "Deadline", "Budget", "ExecutionTime", "Cost", "IsDeadlineViolated",
		"IsBudgetViolated", "NumberOfVMs", "LogMessage" });
	
	
	

    // Fifth step: Read Cloudlets from workload file in the swf format
	
//    WorkloadFileReader workloadFileReader = new WorkloadFileReader("src/main/java/util/LCG.swf.gz", 1);
// 
//    RequestsByGEN = workloadFileReader.generateWorkload();
//    
//    for (WorkloadRequestFromFile Req : RequestsByGEN) {
//    	
//        System.err.println(" CloudLet ID : " + Req.id);
//
//    }

    
    //workloadmapReduce workloadFileReader = new workloadmapReduce("src/main/java/util/LCG.swf.gz", 1);
    


	for (int experimentNumber = 0; experimentNumber < experimentFilesName.length; experimentNumber++)
	{
	    String experimentFileName = "experiments/" + experimentFilesName[experimentNumber];
	    Experiment experiment = YamlFile.getRequestsFromYaml(experimentFileName);
	    Experiment.currentExperimentName = experimentFilesName[experimentNumber].split(".yaml")[0];

	    // Experiments Plotting
	    Experiment.logExperimentsHeader(experiment.workloads.get(0).requests);

	    for (int workloadNumber = 0; workloadNumber < experiment.workloads.size(); workloadNumber++) {
		// BACK TO DEFAULT LOG FILE
		CustomLog.redirectToFile(props.getProperty("FilePath"), true);
		CustomLog.printLine("[[[[[[[ Experiment Number: " + (experimentNumber + 1) + " | Workload Number: "
			+ (workloadNumber + 1) + " ]]]]]]]");
		runSimulationRound(experimentNumber, experimentFileName, workloadNumber,
			experiment.workloads.get(workloadNumber).userClassAllowedPercentage);
		CustomLog.closeAndRemoveHandlers();
	    }
	}
    }

    /**
     * One round of the simulation is executed by this method. Output is printed
     * to the log.
     * 
     */
    private static void runSimulationRound(int experimentNumber, String experimentFileName, int workloadNumber,
	    Map<UserClass, Double> userClassAllowedPercentage) {
	Log.printLine("Starting simulation for experiment number: " + (experimentNumber + 1) + " and workload number: "
		+ (workloadNumber + 1)+ "user class" + userClassAllowedPercentage);

	try {

	    // Initialize the CloudSim library
	    CloudSim.init(1, Calendar.getInstance(), false);
	    
	       // Create Broker
	    engine = new MapReduceEngine();
	    engine.currentWorkloadNumber = workloadNumber + 1;
	    System.err.println(" current workload " + engine.currentWorkloadNumber );
	    engine.currentExperimentNumber = experimentNumber + 1;
	    System.err.println(" current currentExperimentNumber " + engine.currentExperimentNumber );

	    Cloud.brokerID = engine.getId();
	    
//	    // Create datacentres and cloudlets
	    cloud = YamlFile.getCloudFromYaml("inputs/" + Properties.CLOUD.getProperty());
	    cloud.setUserClassAllowedPercentage(userClassAllowedPercentage);
	    engine.setCloud(cloud);
	    Experiment Experiments = YamlFile.getRequestsFromYaml(experimentFileName);
	    
	    WorkloadFileReader workloadFileReader = new WorkloadFileReader("src/main/java/util/LCG.swf.gz", 1);
	    System.out.println(" . . . " + workloadFileReader.toString().trim());
	    RequestListGen = workloadFileReader.generateWorkload().subList(0, 1);
	//    RequestListGen = workloadFileReader.generateWorkload();

	//.subList(0, 300);
	   // RequestListGen = workloadFileReader.generateWorkload();
	    
	    if(RequestListGen.isEmpty()){
	    	System.err.println("No results.. . . .");
	    }else{
			String space = " ------ ";
	     for (WorkloadRequestFromFile RequestGen : RequestListGen) {
	    	// RequestGen.setUserId(brokerId);
//		cloudlet.setVmId(vmid);
	    //     System.err.println(" . . . . . . Request ID : . . . . . " + RequestGen.id);
				System.out.println("Request Details " + RequestGen.budget + space+ RequestGen.deadline + space +
						RequestGen.id + space +RequestGen.jobFile + space +
						RequestGen.policy+ space + RequestGen.submissionTime + space +  UserClass.GOLD);
				
			
					
				NewList.add(new Request( RequestGen.submissionTime,  RequestGen.deadline,  RequestGen.budget, 
						RequestGen.jobFile,  UserClass.GOLD));
				
			}
	   
	    }
	    
//	     requests.requests = NewList;
	
	     requests  = new Requests(NewList);
	     String space =" - -- ";
	 	for (Request request : requests.requests)
		{
	 		request.policy = conf.POLICY;
	 		request.setCloudDeploymentModel(CloudDeploymentModel.Public);
//		    request.policy = Experiments.workloads.get(workloadNumber).policy;
//		    request.setCloudDeploymentModel(Experiments.workloads.get(workloadNumber).cloudDeploymentModel);
			System.out.println("Request Details " + request.budget + space+ request.deadline + space + request.id + 
					space +request.jobFile + space + request.policy +space + request.submissionTime + space + request.getCloudDeploymentModel() );

		}
  
	//    requests = (Requests) RequestListGen.get(0).getWl();
//	    String space2 = " - - - -";
//		for (Request request : requests.requests){
//
//			
//			System.out.println("Request Details " + request.budget + space2+ request.deadline + space2 + request.id + space2 +request.jobFile + space2 + request.policy +space2 + request.submissionTime );
//
//			
//		}
	    //    System.err.println(" . . . . . . Request Size : . . . . . " + requests.requests);


//	    requests = Experiments.workloads.get(workloadNumber).requests;
	    
	//    System.out.println("Requests Size" + requests.requests.size());

//	    int preExperimentIndex = workloadNumber - 1;
//	    while (requests.requests.size() == 0 && preExperimentIndex >= 0)
//	    {
//		requests = Experiments.workloads.get(preExperimentIndex).requests;
//		
//		String space = " ------ ";
//		for (Request request : requests.requests)
//		{
//			System.out.println("Request Details " + request.budget + space+ request.deadline + space + request.id + space +request.jobFile + space + request.policy +space + request.submissionTime );
//		    request.policy = Experiments.workloads.get(workloadNumber).policy;
//		    request.setCloudDeploymentModel(Experiments.workloads.get(workloadNumber).cloudDeploymentModel);
//		}
//		preExperimentIndex--;
//	    }
//	    String space = "--------------------";
//		System.out.println("Budget" + space + "deadline" + space + "id" + space+ "jobfile" + space + "policy" + space + "submission" + space);
//	    String space2 = "------";
//
//		for (Request request : requests.requests){
//
//			
//			System.out.println("Request Details " + request.budget + space2+ request.deadline + space + request.id + space2 +request.jobFile + space2 + request.policy +space2 + request.submissionTime );
//
//			
//		}
	    engine.setRequests(requests);

	    // START
	    CloudSim.startSimulation();
	    engine.logExecutionSummary();

	    Log.printLine("");
	    Log.printLine("");
	} catch (Exception e) {
	    Log.printLine("Unwanted errors happen.");
	    e.printStackTrace();
	} finally {
	    CloudSim.stopSimulation();
	}
    }
}
