import java.time.LocalDate
import java.time.LocalTime

// not sure if title or name so let's just go with title
// notes is included based on the createTask function in the TaskManagementUI
// dueDate is LocalDate (can change it to string)
// dueTime is LocalTime (can change it to string)
class Task(title: String, dueDate: LocalDate, dueTime: LocalTime, priority: Int){
    private var title: String
    private var notes: String = """""" // not sure if Raw String is enough or should be Array<String>
    private var dueDate: LocalDate
    private var dueTime: LocalTime
    private var priority: Int
    private var complete: Boolean

    constructor(title: String, notes: String, dueDate: LocalDate, dueTime: LocalTime, priority: Int): this(title, dueDate, dueTime, priority){
        this.notes = notes
    }

    init{
        this.title = title
        this.dueDate = dueDate
        this.dueTime = dueTime
        this.priority = priority
        this.complete = false
    }

    fun getTitle(): String{
        return this.title
    }

    fun getNotes(): String{
        return this.notes
    }

    fun getDueDate(): LocalDate{
        return this.dueDate
    }

    fun getDueTime(): LocalTime{
        return this.dueTime
    }

    fun getPriority(): Int{
        return this.priority
    }

    fun getComplete(): Boolean{
        return this.complete
    }

    fun setTitle(title: String){
        this.title = title
    }

    fun setNotes(notes: String){
        this.notes = notes
    }

    fun setDueDate(dueDate: LocalDate){
        this.dueDate = dueDate
    }

    fun setDueTime(dueTime: LocalTime){
        this.dueTime = dueTime
    }

    fun setPriority(priority: Int){
        this.priority = priority
    }

    fun setComplete(complete: Boolean){
        this.complete = complete
    }
}

class ModifyTaskList {
    companion object {
        var count: Int = 0
        var list: MutableList<Task> = mutableListOf<Task>()
    }
    fun addTask (t: Task) {
        count++
        list.add(t)
    }
    fun deleteTask (t: Task) {
        count--
        list.remove(t)
    }
    fun viewList () {
        println("- Task List -")
        for (elem in list) {
            println(elem.getTitle())
        }
    }
}

fun main (args: Array<String>){
    var dueDate = LocalDate.parse("2020-02-14")
    var dueTime = LocalTime.parse("20:14:03")
//  LocalDate.parse("yyyy-MM-dd")
//  LocalTime.parse("hh:mm:ss")
    var notes = """SEE
        |SEE
        |SEE
        |SEE
    """.trimMargin()
    var newtask1 = Task("Trial", notes, dueDate, dueTime, 1)
    var newtask2 = Task("Trial2", notes, dueDate, dueTime, 1)
    ModifyTaskList().addTask(newtask1)
    ModifyTaskList().viewList()
    ModifyTaskList().addTask(newtask2)
    ModifyTaskList().viewList()
    ModifyTaskList().deleteTask(newtask1)
    ModifyTaskList().viewList()
}