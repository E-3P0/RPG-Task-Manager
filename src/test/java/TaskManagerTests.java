import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTests {
    @Test
    public void findTaskTest(){
        TaskManager testManager = new TaskManager();
        Task testTask = new Task(0, "Test1", "A test task", 5, 20, 0, false);
        testManager.addCurrentTask(testTask);

        assertEquals(testTask,testManager.findCurrentTask(0));
    }

    @Test
    public void addTaskTest(){
        TaskManager testManager = new TaskManager();
        Task testTask = new Task(0, "Test1", "A test task", 5, 20, 0, false);
        testManager.addCurrentTask(testTask);

        assertEquals("Test1", testManager.findCurrentTask(0).getTitle());

    }

    @Test
    public void editTaskTest(){
        TaskManager testManager = new TaskManager();
        Task testTask = new Task(0, "Test1", "A test task", 5, 20, 0, false);
        testManager.addCurrentTask(testTask);
        assertEquals("Test1", testManager.findCurrentTask(0).getTitle());
        testTask.setTitle("Test2");
        testManager.editCurrentTask(0, testTask);
        assertEquals("Test2", testManager.findCurrentTask(0).getTitle());
    }

    @Test
    public void completeTaskTest(){
        TaskManager testManager = new TaskManager();
        Task testTask = new Task(0, "Test1", "A test task", 5, 20, 0, false);
        testManager.addCurrentTask(testTask);
        testManager.completeCurrentTask(0);
        TaskList completedTasks = testManager.viewCompletedTasks();
        assertEquals("Test1",completedTasks.getTask(0).getTitle());
    }

    @Test
    public void getCurrentTasksTest(){
        TaskManager testManager = new TaskManager();
        Task testTask1 = new Task(0, "Test1", "A test task", 5, 20, 0, false);
        Task testTask2 = new Task(1, "Test2", "Another test task", 4, 25, 0, false);
        Task testTask3 = new Task(2, "Test2", "And another test task", 6, 15, 0, false);

        testManager.addCurrentTask(testTask1);
        testManager.addCurrentTask(testTask2);
        testManager.addCurrentTask(testTask3);

        TaskList currentTasks = testManager.getCurrentTasks();
        for(int taskID = 0; taskID < currentTasks.getSize();taskID++){
            assertEquals(currentTasks.getTask(taskID), testManager.findCurrentTask(taskID));
        }
    }

    @Test
    public void viewCompletedTasksTest(){

    }

    @Test
    public void saveTasksTest(){

    }

    @Test
    public void startGameTest(){

    }

    @Test
    public void selectTaskTest(){

    }

    @Test
    public void addCustomTaskTest(){

    }

    @Test
    public void viewDefaultTasksTest(){

    }
}
