package projects.service;

import projects.dao.ProjectDao;
import projects.entity.Project;

public class ProjectService {                          //acts as a pass-through between main and DAO
	
	private ProjectDao projectDao = new ProjectDao(); 

	public Project addProject(Project project) {
		
		return projectDao.insertProject(project);		
	}
}
