import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TaskManager {

    private TaskList defaultTaskList;
    private TaskList currentTaskList;
    private TaskList dailyTaskList;
    private TaskList completedTaskList;
    private TaskList customTaskList;
    private TaskList failedTaskList;
    private Task mainTask;
    private LocalDateTime lastTimeUsed = null;
    private LocalDateTime startTime;
    private RPGUI rpg;



     TaskManager(boolean genTasks){
        defaultTaskList = new TaskList();
        currentTaskList = new TaskList();
        dailyTaskList = new TaskList();
        completedTaskList = new TaskList();
        customTaskList = new TaskList();
        failedTaskList = new TaskList();
        mainTask = new Task();
        startTime = LocalDateTime.now();
        rpg = new RPGUI();

         startUp(); //starts time stuff

        if(genTasks){
            try {
                generateDefaultTaskList();
                //generateDailyTaskList
            } catch (DuplicateObjectException ignored){} //this isn't going to happen
        }
    }
    TaskManager(){ //Need default constructor for json, just calls the other constructor and makes a blank object
         this(false);
    }

    public RPGUI testGetRPG(){ return rpg;}

    public LocalDateTime getStartTime(){
        return startTime;
    }

    public void startUp(){
        startTime = LocalDateTime.now();
        try {
            if(lastTimeUsed == null) {
                generateDefaultTaskList();
                generateDefaultDailyTaskList();
            }
        }catch(DuplicateObjectException e){
            System.out.println("[WARNING][Default and/or Daily tasks have already been generated!]");
        }
        if(lastTimeUsed != null && ChronoUnit.HOURS.between(lastTimeUsed, startTime) > 24) {
            populateDailyTasks();
        }
    }

    public Task findCurrentTask(int id) throws NonExistentObjectException {
          return currentTaskList.getTaskAt(id);
    }

    public void reassignGlobalIDs(){
            int globalCountID = 0;
            if(!mainTask.getTitle().isEmpty()) {
                mainTask.setID(globalCountID);
                globalCountID = globalCountID + 1;
            }
            for (int taskID = 0; taskID < defaultTaskList.getSize(); taskID++) {
                defaultTaskList.getTaskAt(taskID).setID(globalCountID);
                globalCountID = globalCountID + 1;
            }
            for (int taskID = 0; taskID < currentTaskList.getSize(); taskID++) {
                currentTaskList.getTaskAt(taskID).setID(globalCountID);
                globalCountID = globalCountID + 1;
            }
            for (int taskID = 0; taskID < dailyTaskList.getSize(); taskID++) {
                dailyTaskList.getTaskAt(taskID).setID(globalCountID);
                globalCountID = globalCountID + 1;
            }
            for (int taskID = 0; taskID < completedTaskList.getSize(); taskID++) {
                completedTaskList.getTaskAt(taskID).setID(globalCountID);
                globalCountID = globalCountID + 1;
            }
            for (int taskID = 0; taskID < customTaskList.getSize(); taskID++) {
                customTaskList.getTaskAt(taskID).setID(globalCountID);
                globalCountID = globalCountID + 1;
            }
            for (int taskID = 0; taskID < failedTaskList.getSize(); taskID++) {
                failedTaskList.getTaskAt(taskID).setID(globalCountID);
                globalCountID = globalCountID + 1;
            }
    }

    public void generateDefaultTaskList() throws DuplicateObjectException {
        Task doDishes = new Task(validID(), "Do the Dishes", "Clean all your unwashed dishes.", 10, 0, 0, false);
        defaultTaskList.addTask(doDishes);

        Task doLaundry = new Task(validID(), "Do your Laundry", "Clean your clothes.", 10, 0, 3, false);
        defaultTaskList.addTask(doLaundry);

        Task cleanRoom = new Task(validID(), "Clean your room", "Organize and dust off your room.", 10, 3, 0, false);
        defaultTaskList.addTask(cleanRoom);

        Task finishSemester = new Task(validID(), "Finish 1st Semester", "Ithaca College", 1000, 0, 1, false);
        defaultTaskList.addTask(finishSemester);

        Task getJob = new Task(validID(), "Get a Job", "Money can be exchanged for goods & services", 500, 0, 1, false);
        defaultTaskList.addTask(getJob);

        Task flossTeeth = new Task(validID(), "Floss your teeth", "Floss under your gums too.", 10, 0, 2, false);
        defaultTaskList.addTask(flossTeeth);

        Task checkEmail = new Task(validID(), "Check your email", "Sift through work and spam mail.", 5, 0, 2, false);
        defaultTaskList.addTask(checkEmail);

        Task exercise = new Task(validID(), "Walk or Exercise", "Keep yourself in good shape.", 20, 0, 2, false);
        defaultTaskList.addTask(exercise);

        Task pet = new Task(validID(), "Feed your pet", "Or we will call the ASPCA", 55, 0, 2, false);
        defaultTaskList.addTask(pet);

        Task garbage = new Task(validID(), "Garbage Day", "Or we will call the ASPCA", 55, 0, 3, false);
        defaultTaskList.addTask(garbage);

        Task sweeping = new Task(validID(), "Sweep your Home", "The less dust, the better.", 35, 0, 3, false);
        defaultTaskList.addTask(sweeping);

        Task dinner = new Task(validID(), "Cook a Meal", "Don't starve, make yourself something good!", 45, 0, 2, false);
        defaultTaskList.addTask(dinner);

        Task makeBed = new Task(validID(), "Make your Bed", "Don't be lazy, make it look comfy.", 5, 0, 2, false);
        defaultTaskList.addTask(makeBed);

        Task cleanBathroom = new Task(validID(), "Clean Bathroom", "Don't be gross. You'll thank yourself later.", 15, 0, 3, false);
        defaultTaskList.addTask(cleanBathroom);

        Task organizeMail = new Task(validID(), "Organize Mail", "Sift through your mail and sort it!", 10, 0, 2, false);
        defaultTaskList.addTask(organizeMail);

    }

    public void generateDefaultDailyTaskList() throws DuplicateObjectException {

    }


    public void addCurrentTask(Task newTask) throws DuplicateObjectException {
        try {
            newTask.setID(validID());
            currentTaskList.addTask(newTask);
        }catch(DuplicateObjectException e){
            throw new DuplicateObjectException(e.getMessage());
        }
    }

    public void addCustomTask(String title, String desc, int quality, int timeLimit, int type){
        try {
            int id = customTaskList.getSize();
            Task newTask = new Task(id, title, desc, quality, timeLimit, type, false);
            newTask.setID(validID());
            customTaskList.addTask(newTask);
        }catch(DuplicateObjectException e){
            System.out.println("[Error][" + e.getMessage() + "]");
        }
    }

    public int validID(){
        int size = defaultTaskList.getSize() + currentTaskList.getSize() + dailyTaskList.getSize() + completedTaskList.getSize() + customTaskList.getSize() + failedTaskList.getSize();
        if(!mainTask.getTitle().isEmpty()){
            size += 1;
        }
        return size;
    }

    public String selectTask(String title) throws NonExistentObjectException {
      Task task;
      try {
          if (defaultTaskList.findTask(title) != -1) { //if it exists in defaultTaskList
              task = defaultTaskList.getTask(title);
              if (!(mainTask.getTitle().isEmpty()) && task.getType() == 1) {
                  return "ERROR: Can't have more than one main task selected.";
              }
              task.startTime();
              try {
                  if (task.getType() == 1) mainTask = task;
                  else if (task.getType() == 2){
                      task.setID(validID());
                      Task newTaskInstance = new Task(validID(), task.title, task.desc, task.baseQuality, task.timeLimit, task.type, task.complete);
                      dailyTaskList.addTask(recalculateHoursLeft(newTaskInstance));
                  }else{
                      Task newTaskInstance = new Task(validID(), task.title, task.desc, task.baseQuality, task.timeLimit, task.type, task.complete);
                      currentTaskList.addTask(newTaskInstance);
                  }
              }catch(DuplicateObjectException e){
                  System.out.println("[ERROR][You already have this main task selected!]");
              }
          } else if (customTaskList.findTask(title) != -1) { //if it exists in customTaskList
              task = customTaskList.getTask(title);
              if (!(mainTask.getTitle().isEmpty()) && task.getType() == 1) {
                  return "ERROR: Can't have more than one main task selected.";
              }
              task.startTime();
              try {
                  Task newTaskInstance = new Task(validID(), task.title, task.desc, task.baseQuality, task.timeLimit, task.type, task.complete);
                  if (task.getType() == 1) {
                      mainTask = newTaskInstance;
                  }
                  else {
                      currentTaskList.addTask(newTaskInstance);
                  }
              }catch(DuplicateObjectException e){
                  System.out.println("[ERROR][You already have this main task selected!]");
              }
          } else {
              return "SELECTING TASK: task not found - not added to current tasks.";
          }
      }catch(NonExistentObjectException e){
          throw new NonExistentObjectException("Nonexistent or Invalid Task Requested!");
      }
        return "Task started!";
    }

    public void selectTask(int id, int listType) throws NonExistentObjectException {
        try{
          Task task;
          if(defaultTaskList.findTask(id) != -1) {
              task = defaultTaskList.getTask(id);
              checkIfDaily(task);
          }else if(customTaskList.findTask(id) != -1) {
              task = customTaskList.getTask(id);
              checkIfDaily(task);
          }
        }catch(NonExistentObjectException e){
            throw new NonExistentObjectException("Nonexistent or Invalid Task Requested!");
        }catch(DuplicateObjectException e){
            System.out.println("[Error][" + e.getMessage() + "]");
        }
    }

    public void checkIfDaily(Task task) throws DuplicateObjectException {
        if(task.getType() == 1){
            if (!(mainTask.getTitle().isEmpty()) && task.getType() == 1) {
                throw new DuplicateObjectException("ERROR: Can't have more than one main task selected.");
            }
            mainTask = task;
            task.startTime();
        }else if(task.getType() == 2) {
            Task newTaskInstance = new Task(validID(), task.title, task.desc, task.baseQuality, task.timeLimit, task.type, task.complete);
            newTaskInstance.setID(validID());
            dailyTaskList.addTask(recalculateHoursLeft(newTaskInstance));
        }else{
            Task newTaskInstance = new Task(validID(), task.title, task.desc, task.baseQuality, task.timeLimit, task.type, task.complete);
            newTaskInstance.setID(validID());
            currentTaskList.addTask(newTaskInstance);
        }
    }

    public void stopTask(String title) throws NonExistentObjectException {
        try{
            int index = currentTaskList.findTask(title);
            if(index != -1) {
                currentTaskList.removeTask(title);
                reassignGlobalIDs();
                return;
            }
            index = dailyTaskList.findTask(title);
            if(index != -1){
                dailyTaskList.removeTask(title);
                reassignGlobalIDs();
                return;
            }
            throw new NonExistentObjectException("Nonexistent or Invalid Task Requested!");
        }catch (NonExistentObjectException e){
            throw new NonExistentObjectException("Nonexistent or Invalid Task Requested!");
        }
    }

    public void stopTask(int id) throws NonExistentObjectException {
        try{
            int task = currentTaskList.findTask(id);
            if(task != -1) {
                currentTaskList.removeTask(id);
                reassignGlobalIDs();
                return;
            }
            task = dailyTaskList.findTask(id);
            if(task != -1){
                dailyTaskList.removeTask(id);
                reassignGlobalIDs();
                return;
            }
            throw new NonExistentObjectException("Nonexistent or Invalid Task Requested!");
        }catch (NonExistentObjectException e){
            throw new NonExistentObjectException("Nonexistent or Invalid Task Requested!");
        }
    }


    public void editCurrentTask(int id, Task newTask) throws NonExistentObjectException {
        try {
            currentTaskList.editTask(id, newTask);
        }catch (NonExistentObjectException e) {
            throw new NonExistentObjectException("Nonexistent or Invalid Task Requested!");
        }
    }

    public void editTask(String title, String newTitle, String desc, int quality, int timeLimit, int type) throws NonExistentObjectException {
        try {
            Task editedTask;
            int index = currentTaskList.findTask(title);
            if(currentTaskList.findTask(title) != -1){
                editedTask = currentTaskList.getTaskAt(index);
                editedTask.setTitle(newTitle);
                editedTask.setDesc(desc);
                editedTask.setBaseQuality(quality);
                editedTask.setTimeLimit(timeLimit);
                editedTask.setType(type);
                return;
            }else if(dailyTaskList.findTask(title) != -1) {
                index = dailyTaskList.findTask(title);
                editedTask = dailyTaskList.getTaskAt(index);
                editedTask.setTitle(newTitle);
                editedTask.setDesc(desc);
                editedTask.setBaseQuality(quality);
                editedTask.setTimeLimit(timeLimit);
                editedTask.setType(type);
                return;
            }else if(customTaskList.findTask(title) != -1) {
                index = customTaskList.findTask(title);
                editedTask = customTaskList.getTaskAt(index);
                editedTask.setTitle(newTitle);
                editedTask.setDesc(desc);
                editedTask.setBaseQuality(quality);
                editedTask.setTimeLimit(timeLimit);
                editedTask.setType(type);
                return;
            }
            throw new NonExistentObjectException("Nonexistent or Invalid Task Requested!");
        }catch (NonExistentObjectException e) {
            throw new NonExistentObjectException("Nonexistent or Invalid Task Requested!");
        }
    }

    public void editTask(int id, String newTitle, String desc, int quality, int timeLimit, int type, int taskList) throws NonExistentObjectException {
        try {
            Task editedTask;
                if(currentTaskList.findTask(id) != -1) {
                    editedTask = currentTaskList.getTask(id);
                    editedTask.setTitle(newTitle);
                    editedTask.setDesc(desc);
                    editedTask.setBaseQuality(quality);
                    editedTask.setTimeLimit(timeLimit);
                    editedTask.setType(type);
                    return;
                }else if(dailyTaskList.findTask(id) != -1) {
                    editedTask = dailyTaskList.getTask(id);
                    editedTask.setTitle(newTitle);
                    editedTask.setDesc(desc);
                    editedTask.setBaseQuality(quality);
                    editedTask.setTimeLimit(timeLimit);
                    editedTask.setType(type);
                    return;
                }else if(customTaskList.findTask(id) != -1) {
                    editedTask = customTaskList.getTask(id);
                    editedTask.setTitle(newTitle);
                    editedTask.setDesc(desc);
                    editedTask.setBaseQuality(quality);
                    editedTask.setTimeLimit(timeLimit);
                    editedTask.setType(type);
                    return;
                }
                throw new NonExistentObjectException("Nonexistent or Invalid Task Requested!");
        }catch (NonExistentObjectException e) {
            throw new NonExistentObjectException("Nonexistent or Invalid Task Requested!");
        }
    }

    public void populateDailyTasks(){
        for(int dailyID = 0; dailyID < dailyTaskList.getSize(); dailyID++){
            try{
                Task newDaily = dailyTaskList.getTaskAt(dailyID);
                newDaily.setID(validID());
                newDaily.startTime();
                currentTaskList.addTask(recalculateHoursLeft(newDaily));
            }/*catch(NonExistentObjectException e){
                System.out.println("[ERROR][Internal error. Daily task no longer found.]");
            }*/catch(DuplicateObjectException e){
                System.out.println("[NOTICE][Daily task was not completed. Skipped!]");
            }
        }
    }

    public Task recalculateHoursLeft(Task task){
        LocalDateTime nextDay = LocalDateTime.now().minusDays(-1);
        nextDay = nextDay.minusHours(nextDay.getHour());nextDay = nextDay.minusMinutes(nextDay.getMinute());nextDay = nextDay.minusSeconds(nextDay.getSecond());
        task.setTimeLimit((int) ChronoUnit.HOURS.between(startTime, nextDay));
        return task;
    }

    public double completeCurrentTask(int id, double completionQuality) throws NonExistentObjectException {
        try {
            Task completedTask = currentTaskList.getTask(id);
            completedTask.complete();
            completedTask.setCompletionQuality(completionQuality);
            completedTaskList.addTask(completedTask);
            currentTaskList.removeTask(completedTask.id);
            double xp = completedTask.calcExp();
            rpg.transferEXP(xp);
            return xp;
        }catch (NonExistentObjectException e){
            throw new NonExistentObjectException("Nonexistent or Invalid Task Requested!");
        }catch(DuplicateObjectException e){
            System.out.println("[ERROR][ Task already completed.]");
            return 0;
        } catch (IOException e) {
            System.out.println("[ERROR][ EXP transfer failed (load/save error).]");
            System.out.println("[ "+ e +"]");
            return 0;
        }
    }

    public double complete(String title, double completionQuality) throws NonExistentObjectException, IllegalArgumentException{
      if(completionQuality < 0.1 || completionQuality > 1 ){
          throw new IllegalArgumentException("Invalid completion quality (needs between 0.1 and 1)");
      }
      int id = currentTaskList.getTaskAt(currentTaskList.findTask(title)).id;
      return completeCurrentTask(id, completionQuality);
    }

    public double complete(int id, double completionQuality) throws NonExistentObjectException, IllegalArgumentException{
        if(completionQuality < 0.1 || completionQuality > 1 ){
            throw new IllegalArgumentException("Invalid completion quality (needs between 0.1 and 1)");
        }
        return completeCurrentTask(id, completionQuality);
    }

    public String viewCurrentTasks(){
        return currentTaskList.toString();
    }

    public TaskList getCurrentTaskList(){
      return currentTaskList;
    }

    public String viewCompletedTasks(){
        return completedTaskList.toString();
    }

    public TaskList getCompletedTaskList(){
        return completedTaskList;
    }

    public String viewCustomTasks(){
        return customTaskList.toString();
    }

    public String viewDailyTasks(){
         return dailyTaskList.toString();
    }
    public TaskList getCustomTaskList(){
        return customTaskList;
    }

    public String viewDefaultTasks(){
        return defaultTaskList.toString();
    }

    public TaskList getDefaultTaskList(){
        return defaultTaskList;
    }

    public String viewFailedTasks(){
        return failedTaskList.toString();
    }

    public TaskList getFailedTaskList(){
        return failedTaskList;
    }

    public void startGame(){
         rpg.commandHandler(); //starts rpg interface
    }

    public String checkTimedTasks(LocalDateTime currentTime) throws NonExistentObjectException, DuplicateObjectException {
        String failedTasks="FAILED: ";  Task task;  LocalDateTime time;
        TaskList newFailedTasks = new TaskList();

        for (int i = 0; i < currentTaskList.getSize(); i++){
            task = currentTaskList.getTaskAt(i);
            if (task.checkIfTimed()){
                //time = new Date(task.getStartTime().getTime() + task.getTimeLimit()*60000);
                time = task.getStartTime().plusMinutes(task.getTimeLimit()); //time to finish task by
                if(currentTime.isAfter(time)) {
                    failedTasks= failedTasks.concat(task.getTitle());
                    failedTasks = failedTasks.concat(", ");
                    try {
                        newFailedTasks.addTask(task);
                    }catch(DuplicateObjectException e){
                        System.out.println("[Error][" + e.getMessage() + "]");
                    }
                }
            }
        }
        if (failedTasks.equals("FAILED: ")) return "[NOTICE][ No tasks failed.]";
        else {
            for (int i = 0; i < newFailedTasks.getSize(); i++){ //only go through failed tasks lists if there were failed tasks
                task = newFailedTasks.getTaskAt(i);
                currentTaskList.removeTask(task.getTitle());
                failedTaskList.addTask(task);
            }
            failedTasks = failedTasks.substring(0, failedTasks.length()-2); //removes ending ", "
            return failedTasks;
        }
    }

    /*** MAIN TASKS ***/

    public Task getMainTask(){
        return mainTask;
    }

    public String stopMainTask(){
        if (mainTask.getTitle().isEmpty()){
            return "ERROR: No main task selected to stop.";
        }
        mainTask = new Task();
        return "Main task stopped.";
    }

    public void incMainProgress(int progress) throws IllegalArgumentException, NonExistentObjectException {
        if(progress <= 0 || progress > 100){
            throw new IllegalArgumentException("Invalid progress amount.");
        }
        else if (mainTask.getTitle().isEmpty()){
            throw new NonExistentObjectException("No main task selected.");
        }
        mainTask.addProgress(progress);
    }

    public String completeMain(){
        if (mainTask.getTitle().isEmpty()){
            return "[ERROR: No main task selected to complete.]";
        }
        if (mainTask.getProgress() < 100){
            return "[ERROR: Main task not at 100% progress, can't complete.]";
        }
        mainTask.complete();
        try {
            mainTask.setCompletionQuality(1);
            rpg.transferEXP(mainTask.calcExp());
            System.out.println("[EXPERIENCE: You gained " + mainTask.calcExp() + " experience!]");
        } catch (IOException e) {
            System.out.println("[ERROR][ EXP transfer failed (load/save error).]");
            System.out.println("[ "+ e +"]");
        }
        try {
            mainTask.setID(validID());
            completedTaskList.addTask(mainTask);
        }catch (DuplicateObjectException e){
            System.out.println("[NOTICE: You have completed this main task before!]");
        }
        mainTask = new Task();
        return "[SUCCESS: Main task completed!]";

    }

}
