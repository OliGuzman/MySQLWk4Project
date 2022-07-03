package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

public class ProjectsApp {
	
	private ProjectService projectService = new ProjectService(); 

	// @formatter:off	
	private List<String> operations = List.of(                     //prints the list of operations to the console for the user to select
			"1) Add a project"
	);  
	// @formatter:on
	
	private Scanner scanner = new Scanner(System.in);               //scanner accepts user input from the console 
	
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
					
					default: 
						System.out.println("\n" + selection + " is not a valid selection.");
						break; 
					}
			}
			catch(Exception e) {
				System.out.println("ERROR: " + e + " TRY AGAIN!");				
			}
		}		
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
