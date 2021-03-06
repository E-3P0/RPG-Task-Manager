import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

public class TaskManagerTests {
    @Test
    public void findTaskTest() throws NonExistentObjectException, DuplicateObjectException {
        TaskManager testManager = new TaskManager(true);
        Task testTask = new Task(0, "Test1", "A test task", 5, 20, 0, false);
        testManager.addCurrentTask(testTask);

        assertEquals(testTask,testManager.findCurrentTask(0));
    }

    @Test
    public void addTaskTest() throws NonExistentObjectException, DuplicateObjectException {
        TaskManager testManager = new TaskManager(true);
        Task testTask = new Task(0, "Test1", "A test task", 5, 20, 0, false);
        testManager.addCurrentTask(testTask);

        assertEquals("Test1", testManager.findCurrentTask(0).getTitle());
        assertThrows(DuplicateObjectException.class, ()->testManager.addCurrentTask(testTask));

    }

    @Test
    public void editTaskTest() throws NonExistentObjectException, DuplicateObjectException {
        TaskManager testManager = new TaskManager(true);
        Task testTask = new Task(0, "Test1", "A test task", 5, 20, 0, false);
        testManager.addCurrentTask(testTask);
        assertEquals("Test1", testManager.findCurrentTask(0).getTitle());
        testTask.setTitle("Test2");
        testManager.editCurrentTask(0, testTask);
        assertEquals("Test2", testManager.findCurrentTask(0).getTitle());
    }

    @Test
    public void completeTaskTest() throws NonExistentObjectException, DuplicateObjectException {
        TaskManager testManager = new TaskManager(true);
        Task testTask = new Task(0, "Test1", "A test task", 5, 20, 0, false);
        testManager.addCurrentTask(testTask);
        testManager.completeCurrentTask(testTask.id,1);
        TaskList completedTasks = testManager.getCompletedTaskList();
        assertEquals("Test1",completedTasks.getTask(testTask.id).getTitle());
        assertEquals(1,completedTasks.getTask(testTask.id).getCompletionQuality());
        assertEquals(5.0,completedTasks.getTask(testTask.id).calcExp());
        testManager.testGetRPG().rpgManager.player = new Player(); //make sure xp changes aren't saved.
        testManager.testGetRPG().rpgManager.transferEXP(0);
    }

    @Test
    public void getCurrentTasksTest() throws NonExistentObjectException, DuplicateObjectException {
        TaskManager testManager = new TaskManager(true);
        Task testTask1 = new Task(0, "Test1", "A test task", 5, 20, 0, false);
        Task testTask2 = new Task(1, "Test2", "Another test task", 4, 25, 0, false);
        Task testTask3 = new Task(2, "Test3", "And another test task", 6, 15, 0, false);

        testManager.addCurrentTask(testTask1);
        testManager.addCurrentTask(testTask2);
        testManager.addCurrentTask(testTask3);

        TaskList currentTasks = testManager.getCurrentTaskList();
        for(int taskID = 0; taskID < currentTasks.getSize();taskID++){
            assertEquals(currentTasks.getTaskAt(taskID), testManager.findCurrentTask(taskID));
        }
        assertThrows(NonExistentObjectException.class, ()-> currentTasks.getTask(currentTasks.getSize()));
        assertThrows(NonExistentObjectException.class, ()-> currentTasks.getTask(-1));
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
        TaskManager testManager = new TaskManager(true);
        TaskList defaultTasks = testManager.getDefaultTaskList();
//        String expected = "{id: Title | Description | Quality | TimeLimit | Type | Complete\n" +
//                "0: " + "Do the Dishes" +" "+ "Clean all your unwashed dishes." +" "+ 0 +" "+ 0 +" "+ "default" +" "+ false + "\n" +
//                "1: " + "Do your Laundry" +" "+ "Clean your clothes." +" "+ 0 +" "+ 0 +" "+ "default" +" "+ false + "\n" +
//                "2: " + "Clean your room" +" "+ "Organize and dust off your room." +" "+ 0 +" "+ 0 +" "+ "default" +" "+ false + "\n" +
//                "3: " + "Floss your teeth" +" "+ "Floss under your gums too." +" "+ 0 +" "+ 0 +" "+ "default" +" "+ false + "\n" + "}";

        assertEquals(testManager.viewDefaultTasks(), defaultTasks.toString());

    }

    @Test
    public void timeTests() throws NonExistentObjectException, DuplicateObjectException {
        TaskManager testManager = new TaskManager(true);
        //Testing one timed task
        testManager.addCustomTask("Do Homework Before Class", "Due in an hour!", 10, 60, 0);
        testManager.selectTask("Do Homework Before Class");

        LocalDateTime currentTime = LocalDateTime.now();
        assertEquals("[NOTICE][ No tasks failed.]", testManager.checkTimedTasks(currentTime));

        currentTime = currentTime.plusMinutes(65);

        assertEquals(testManager.checkTimedTasks(currentTime),"FAILED: Do Homework Before Class");
        assertTrue(testManager.viewCurrentTasks().contains("No tasks."));
        assertTrue(testManager.viewFailedTasks().contains("Do Homework Before Class"));

        //Testing two timed tasks
        TaskManager testManager2 = new TaskManager(true);

        testManager2.addCustomTask("Do Homework Before Class", "Due in an hour!", 10, 60, 0);
        testManager2.selectTask("Do Homework Before Class");
        testManager2.addCustomTask("Email Teacher", "Before class!", 10, 30, 0);
        testManager2.selectTask("Email Teacher");
        currentTime = LocalDateTime.now();

        assertEquals("[NOTICE][ No tasks failed.]", testManager2.checkTimedTasks(currentTime));
        currentTime = currentTime.plusMinutes(65);

        assertEquals(testManager2.checkTimedTasks(currentTime),"FAILED: Do Homework Before Class, Email Teacher");
        assertTrue(testManager2.viewCurrentTasks().contains("No tasks."));
        assertTrue(testManager2.viewFailedTasks().contains("Do Homework Before Class")
                && testManager2.viewFailedTasks().contains("Email Teacher"));
    }

    @Test
    public void mainTaskTests() throws NonExistentObjectException {
       TaskManager testManager = new TaskManager(true);

       //Trying to do things with main task while none is selected
       assertTrue(testManager.getMainTask().getTitle().isEmpty());
       assertEquals(testManager.stopMainTask(), "ERROR: No main task selected to stop.");
       assertEquals(testManager.completeMain(),"[ERROR: No main task selected to complete.]");
       assertThrows(NonExistentObjectException.class, () -> testManager.incMainProgress(1));

       //Selecting a main task and trying to select a second one
       assertEquals(testManager.selectTask("Finish 1st Semester"),"Task started!");
       assertEquals(testManager.getMainTask().getTitle(),"Finish 1st Semester");
       assertEquals(testManager.selectTask("Get a Job"),"ERROR: Can't have more than one main task selected.");
       assertEquals(testManager.getMainTask().getTitle(),"Finish 1st Semester");
       assertEquals(testManager.getMainTask().getProgress(), 0);
       //Stopping main task while one is selected.
       assertEquals(testManager.stopMainTask(), "Main task stopped.");
       assertTrue(testManager.getMainTask().getTitle().isEmpty()); //stopping main task makes it a blank Task obj

        //Adding progress
        assertEquals(testManager.selectTask("Get a Job"),"Task started!");
        assertEquals(testManager.getMainTask().getProgress(), 0);
        testManager.incMainProgress(10);
        assertEquals(testManager.getMainTask().getProgress(), 10);

        assertThrows(IllegalArgumentException.class, () -> testManager.incMainProgress(-1));
        assertThrows(IllegalArgumentException.class, () -> testManager.incMainProgress(101));
        assertThrows(IllegalArgumentException.class, () -> testManager.incMainProgress(0));
        //TODO stop user from entering anything other than an int

        testManager.incMainProgress(89);
        assertEquals(testManager.getMainTask().getProgress(), 99);
        assertEquals(testManager.completeMain(),"[ERROR: Main task not at 100% progress, can't complete.]");
        testManager.incMainProgress(1);
        assertEquals(testManager.completeMain(),"[SUCCESS: Main task completed!]");
        testManager.testGetRPG().rpgManager.player = new Player(); //make sure xp changes aren't saved.
        testManager.testGetRPG().rpgManager.transferEXP(0);

    }
}
