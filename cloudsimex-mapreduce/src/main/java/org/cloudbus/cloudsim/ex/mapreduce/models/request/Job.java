package org.cloudbus.cloudsim.ex.mapreduce.models.request;

import java.util.List;

public class Job {
	
	 public String dataSourceName;
     public List<MapTask> mapTasks;
     public List<ReduceTask> reduceTasks;
    
 
    public Task getTask(int taskId)
    {
	for (MapTask mapTask : mapTasks) {
	    if (mapTask.getCloudletId() == taskId)
		return mapTask;
	}

	for (ReduceTask reduceTask : reduceTasks) {
	    if (reduceTask.getCloudletId() == taskId)
		return reduceTask;
	}

	return null;
    }
    
    
    public String getDataSourceName() {
 		return dataSourceName;
 	}

 	public void setDataSourceName(String dataSourceName) {
 		this.dataSourceName = dataSourceName;
 	}

 	public List<MapTask> getMapTasks() {
 		return mapTasks;
 	}

 	public void setMapTasks(List<MapTask> mapTasks) {
 		this.mapTasks = mapTasks;
 	}

 	public List<ReduceTask> getReduceTasks() {
 		return reduceTasks;
 	}

 	public void setReduceTasks(List<ReduceTask> reduceTasks) {
 		this.reduceTasks = reduceTasks;
 	}

 

}
