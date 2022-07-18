package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

public class ProjectsApp {
	
	private Scanner scanner = new Scanner(System.in);               //scanner accepts user input from the console 
	private ProjectService projectService = new ProjectService(); 
	private Project curProject; 
	
	// @formatter:off	
	private List<String> operations = List.of(                     //prints the list of operations to the console for the user to select
			"1) Add a project",
			"2) List projects", 
			"3) Select a project",
			"4) Update project details",
			"5) Delete a project"
	);  
	// @formatter:on
	
	
	
	public static void main(String[] args) {
	
		new ProjectsApp().processUserSelections();    	
	}

	private void processUserSelections() {                         //method displays menu selections, gets user selection & acts on selection
		
		boolean done = false; 
		
		while (!done) {
			try {
				int selection = getUserSelection(); 
				
				switch(selection) {
					case -1:
						done = exitMenu();
						break;
						
					case 1: 
						createProject();
						break; 
						
					case 2:
						listProjects();
						break; 
						
					case 3: 
						selectProject();
						break; 
						
					case 4:
						updateProjectDetails(); 
						break; 
					
					case 5:
						deleteProject(); 
						break; 
						
					default: 
						System.out.println("\n" + selection + " is not a valid selection. Try again!");
						break; 
					}
			}
			catch(Exception e) {
				System.out.println("\nERROR: " + e + " Try again!");				
			}
		}		
	}

	private void deleteProject() {
		
		listProjects(); 
		
		Integer projectId = getIntInput("Enter the ID of the project to delete");
		
		projectService.deleteProject(projectId); 
		
		System.out.println("Project " + projectId + " was deleted successfully.");
		
		if(Objects.nonNull(curProject) && curProject.getProjectId().equals(projectId)) {
			curProject = null; 
		}
	}

	private void updateProjectDetails() {
		
		if (Objects.isNull(curProject)) {
			System.out.println("\nPlease select a project.");
			return;
		}
		
		String projectName = getStringInput("Enter the project name [" + curProject.getProjectName() + "]");
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours [" + curProject.getEstimatedHours() + "]"); 
		BigDecimal actualHours = getDecimalInput("Enter the actual hours [" + curProject.getActualHours() + "]");
		Integer difficulty = getIntInput("Enter the project difficulty (1-5) [" + curProject.getDifficulty() + "]");
		String notes = getStringInput("Enter the project notes [" + curProject.getNotes() + "]");
			
		Project project = new Project(); 
		
		project.setProjectName(Objects.isNull(projectName) ? curProject.getProjectName() : projectName);
		project.setEstimatedHours(Objects.isNull(estimatedHours) ? curProject.getEstimatedHours() : estimatedHours); 
		project.setActualHours(Objects.isNull(actualHours) ? curProject.getActualHours() : actualHours);
		project.setDifficulty(Objects.isNull(difficulty) ? curProject.getDifficulty() : difficulty);
		project.setNotes(Objects.isNull(notes) ? curProject.getNotes() : notes);
		
		project.setProjectId(curProject.getProjectId());
		
		projectService.modifyProjectDetails(project); 
		
		curProject = projectService.fetchProjectById(curProject.getProjectId());		
				
	}

	private void selectProject() {          //method lists project ID & name so user can select a project ID. Current project is set to the returned project
		listProjects();
		
		Integer projectId = getIntInput("Enter a project ID to select a project"); 
		
		curProject = null;                 //will unselect the currently selected project
		
		curProject = projectService.fetchProjectById(projectId);
		
	}

	private void listProjects() {          //return and print a list of projects
		
		List<Project> projects = projectService.fetchAllProjects(); 
		
		System.out.println("\nProjects:");
		
		projects.forEach(project -> System.out.println("  " + project.getProjectId() + ": " + project.getProjectName()));
	}

	private void createProject() {             //method collects project info from user & stores the project row
		String projectName = getStringInput("Enter the Project Name"); 
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours"); 
		BigDecimal actualHours = getDecimalInput("Enter the actual hours"); 
		Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
		String notes = getStringInput("Enter the project notes");
		
		Project project = new Project(); 
		
		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);
		
		Project dbProject = projectService.addProject(project); 
		
		System.out.println("You have successfully created project: " + dbProject);
		
	}

	private BigDecimal getDecimalInput(String prompt) {
		
		String input = getStringInput(prompt); 
			
			if (Objects.isNull(input)) {
				return null; 
			}
			
			try {
				return new BigDecimal(input).setScale(2); 
			}
			catch(NumberFormatException e){
				throw new DbException(input + " is not a valid decimal number."); 
			}
	}

	private boolean exitMenu() {                                  //method for exiting program 
		
		System.out.println("\nExiting the menu. Good Bye!");
		return true;
	}

	private int getUserSelection() {                              //method prints the operations & accepts user input
		printOperations();                                        
		
		Integer input = getIntInput("Enter a menu selection");    //returns the user menu selection
		
		return Objects.isNull(input) ? -1 : input; 
	} 


	private void printOperations() {                              //prints menu selection options
		System.out.println("\nThese are the available selections. Press the Enter key to quit:");
		
		operations.forEach(line -> System.out.println(" " + line));
		
		if(Objects.isNull(curProject)) {                         //prints the current project when menu selections are displayed
			System.out.println("\nYou are not working with a project.");
		}
		else {
			System.out.println("\nYou are working with project: " + curProject);
		}
	}

	private Integer getIntInput(String prompt) {                 //method accepts user input & converts it to an Integer
		
		String input = getStringInput(prompt); 
		
		if (Objects.isNull(input)) {
			return null; 
		}
		
		try {
			return Integer.valueOf(input); 
		}
		catch(NumberFormatException e){
			throw new DbException(input + " is not a valid number."); 
		}
	}

	private String getStringInput(String prompt) {             //lowest level input method. Really prints the prompt and gets user input.
		
		System.out.print(prompt + ": ");
		
		String input = scanner.nextLine();
		
		return input.isBlank() ? null : input.trim(); 
		
	}		
}
 