package projects.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import projects.dao.ProjectDao;
import projects.entity.Project;
import projects.exception.DbException;

@SuppressWarnings("unused")
public class ProjectService {                          //acts as a pass-through between main and DAO
	
	private ProjectDao projectDao = new ProjectDao(); 

	public Project addProject(Project project) {
		
		return projectDao.insertProject(project);		
	}

	public List<Project> fetchAllProjects() {         //returns the results of the method call to the DAO class
		
		return projectDao.fetchAllProjects(); 
	}

	public Project fetchProjectById(Integer projectId) {  //method retrieves a single project with all details
		
		return projectDao.fetchProjectById(projectId).orElseThrow(() -> new NoSuchElementException(
				"Project with project ID = " + projectId + " does not exist.")); 
	}

	public void modifyProjectDetails(Project project) {
		
		if(!projectDao.modifyProjectDetails(project)) {
			throw new DbException ("Project with ID=" + project.getProjectId() + " does not exist.");
		} 
	}

	public void deleteProject(Integer projectId) {
		
		if(!projectDao.deleteProject(projectId)) {
			throw new DbException ("Project with ID=" + projectId + " does not exist.");
		}		
	}
}
