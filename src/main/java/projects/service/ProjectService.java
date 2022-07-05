package projects.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import projects.dao.ProjectDao;
import projects.entity.Project;

public class ProjectService {                          //acts as a pass-through between main and DAO
	
	private ProjectDao projectDao = new ProjectDao(); 

	public Project addProject(Project project) {
		
		return projectDao.insertProject(project);		
	}

	public List<Project> fetchAllProjects() {         //returns the results of the method call to the DAO class
		
		return projectDao.fetchAllProjects(); 
	}

	public Project fetchProjectByID(Integer projectId) {  //method retrieves a single project with all details
		
		return projectDao.fetchProjectById(projectId).orElseThrow(() -> new NoSuchElementException(
				"Project with project ID = " + projectId + " does not exist.")); 
	}
}
