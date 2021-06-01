import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:focus/dao/tasks/task.dart';
import 'package:focus/dao/tasks/taskmodel.dart';
import 'package:intl/intl.dart';
import 'package:provider/provider.dart';

void showModifyTaskModal(BuildContext context, Task task) =>
    showModalBottomSheet(
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(
          top: Radius.circular(10.0),
        ),
      ),
      context: context,
      isScrollControlled: true,
      builder: (context) => Container(
        height: 180,
        padding: EdgeInsets.symmetric(horizontal: 18, vertical: 10),
        child: ModifyTaskForm(task: task),
      ),
    );

Icon priorityIcon(int i, {double? size}) {
  switch (i) {
    case 0:
      return Icon(Icons.flag, color: Colors.red, size: size);
    case 1:
      return Icon(Icons.flag, color: Colors.yellow, size: size);
    case 2:
      return Icon(Icons.flag, color: Colors.blue, size: size);
    case 3:
    default:
      return Icon(Icons.flag_outlined, size: size);
  }
}

class ModifyTaskForm extends StatefulWidget {
  final Task task;

  const ModifyTaskForm({Key? key, required this.task}) : super(key: key);

  @override
  _ModifyTaskFormState createState() => _ModifyTaskFormState();
}

class _ModifyTaskFormState extends State<ModifyTaskForm> {
  final _formKey = GlobalKey<FormState>();
  late Task task;

  @override
  void initState() {
    super.initState();
    task = widget.task;
    _date = task.date;
    if (_date != null) {
      _dateString = DateFormat('MMM d').format(_date!);
    }
    _time = task.time;
    priority = task.priority;
    _priorityString = "${task.priority + 1}";
    title = task.title;
    titleController = TextEditingController(text: title);
    noteController = TextEditingController(text: task.notes);
    notes = task.notes;
    if (notes == "") {
      _notesString = "Add note";
    }
  }

  bool submittable = true;
  String notes = "";
  String title = "";

  TextEditingController titleController = TextEditingController();

  DateTime? _date;
  String _dateString = "No date";

  TimeOfDay? _time;
  String _timeString = "No time";

  int priority = 3;
  String _priorityString = "";

  TextEditingController noteController = TextEditingController();
  String _notesString = "Edit note";
  bool savedCorrectly = false;

  Future<void> _selectDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: _date ?? DateTime.now(),
      firstDate: DateTime.now(),
      lastDate: DateTime(2101),
    );
    if (picked != null && picked != _date)
      setState(() {
        _date = picked;
        _dateString = DateFormat('MMM d').format(picked);
      });
  }

  Future<void> _selectTime(BuildContext context) async {
    final TimeOfDay? picked = await showTimePicker(
      context: context,
      initialTime: _time ?? TimeOfDay.now(),
    );
    setState(() {
      _time = picked;
      _timeString = picked?.format(context) ?? _timeString;
    });
  }

  Widget _setupPriorityList() {
    return Container(
      height: 300.0, // Change as per your requirement
      width: 300.0, // Change as per your requirement
      child: ListView.builder(
        shrinkWrap: true,
        itemCount: 4,
        itemBuilder: (BuildContext context, int index) {
          return ListTile(
            leading: priorityIcon(index),
            title: Text('Priority ${index + 1}'),
            onTap: () {
              setState(() {
                priority = index;
                _priorityString = '${index + 1}';
              });
              Navigator.pop(context);
            },
          );
        },
      ),
    );
  }

  Widget _setupNotes() {
    return Container(
      width: 300.0,
      height: 220.0,
      child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
        TextField(
          controller: noteController,
          keyboardType: TextInputType.multiline,
          maxLines: 7,
        ),
        Spacer(),
        Row(
          mainAxisAlignment: MainAxisAlignment.end,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            TextButton.icon(
              onPressed: () {
                setState(() {
                  savedCorrectly = true;
                  if (noteController.text.isNotEmpty) {
                    _notesString = "Edit note";
                  } else {
                    _notesString = "Add note";
                  }
                });
                Navigator.pop(context);
              },
              style: TextButton.styleFrom(primary: Colors.lightBlue),
              icon: Icon(Icons.send),
              label: Text("Save"),
            )
          ],
        )
      ]),
    );
  }

  Future<void> _selectPriority(BuildContext context) async {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text('Select a priority'),
          content: _setupPriorityList(),
        );
      },
    );
  }

  Future<void> _selectNotes(BuildContext context) async {
    String _previousString = noteController.text;
    savedCorrectly = false;
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text('Add a note'),
          content: _setupNotes(),
        );
      },
    ).then((value) {
      if (!savedCorrectly) {
        noteController.text = _previousString;
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    _timeString = _time?.format(context) ?? _timeString;
    var taskTitle = TextFormField(
      controller: titleController,
      decoration: const InputDecoration(
        contentPadding: EdgeInsets.zero,
        border: InputBorder.none,
        errorStyle: TextStyle(height: 0, color: Colors.transparent),
      ),
      onChanged: (string) {
        setState(() {
          submittable = _formKey.currentState!.validate();
        });
      },
      style: TextStyle(fontSize: 18),
      validator: (value) {
        if (value == null || value.isEmpty) {
          setState(() {
            submittable = false;
          });
          return "";
        } else {
          setState(() {
            submittable = true;
          });
          return null;
        }
      },
    );
    var taskDate = ActionChip(
      labelPadding: EdgeInsets.fromLTRB(4, 0, 8, 0),
      avatar: Icon(Icons.calendar_today_outlined, size: 18.0),
      label: Text(_dateString),
      onPressed: () => _selectDate(context),
    );
    var taskTime = ActionChip(
      labelPadding: EdgeInsets.fromLTRB(4, 0, 8, 0),
      avatar: Icon(Icons.alarm_outlined, size: 18.0),
      label: Text(_timeString),
      onPressed: () => _selectTime(context),
    );
    var taskPriority = ActionChip(
      labelPadding: EdgeInsets.fromLTRB(4, 0, 8, 0),
      avatar: priorityIcon(priority, size: 18.0),
      label: Text(_priorityString),
      onPressed: () => _selectPriority(context),
    );
    var taskAddNotes = ActionChip(
      labelPadding: EdgeInsets.fromLTRB(4, 0, 8, 0),
      avatar: Icon(Icons.notes_outlined, size: 18.0),
      label: Text(_notesString),
      onPressed: () => _selectNotes(context),
    );

    return Form(
      key: _formKey,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        mainAxisSize: MainAxisSize.min,
        children: <Widget>[
          taskTitle,
          SingleChildScrollView(
            scrollDirection: Axis.horizontal,
            child: Row(
              children: [
                taskDate,
                SizedBox(width: 10),
                taskTime,
                SizedBox(width: 10),
                taskPriority,
                SizedBox(width: 10),
                taskAddNotes
              ],
            ),
          ),
          Spacer(),
          Row(mainAxisAlignment: MainAxisAlignment.end, children: [
            TextButton.icon(
              onPressed: submittable
                  ? () {
                      title = titleController.text;
                      notes = noteController.text;
                      if (_formKey.currentState!.validate()) {
                        print("$title, $notes, $_date, $_time, $priority");
                        Provider.of<TaskModel>(context, listen: false)
                            .modifyTask(
                                task.id, title, notes, _date, _time, priority);
                        titleController.clear();
                        noteController.clear();
                        Navigator.pop(context);
                      } else {
                        return null;
                      }
                    }
                  : null,
              style: TextButton.styleFrom(primary: Colors.lightBlue),
              icon: Icon(Icons.send),
              label: Text("Save"),
            )
          ]),
        ],
      ),
    );
  }
}
