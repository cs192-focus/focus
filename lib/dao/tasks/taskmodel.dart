import 'dart:collection';

import 'package:flutter/material.dart';
import 'package:focus/dao/tasks/task.dart';

class TaskModel extends ChangeNotifier {
  final _tasks = <Task>[];
  int? lastDeletedTaskPosition;
  Task? lastDeletedTask;

  TaskModel() {
    _tasks.add(Task(
      id: 0,
      title: "Task 1",
      notes: "This is a test note",
      date: DateTime.now(),
      time: TimeOfDay(hour: 0, minute: 0),
      priority: 3,
      isComplete: false,
    ));
    _tasks.add(Task(
      id: 1,
      title: "Task 2",
      notes: "This is another note",
      date: DateTime.now(),
      time: TimeOfDay(hour: 17, minute: 0),
      priority: 2,
      isComplete: false,
    ));
    _tasks.add(Task(
      id: 2,
      title: "Task 3",
      notes: "This is a third note",
      date: DateTime.now(),
      time: TimeOfDay(hour: 21, minute: 0),
      priority: 1,
      isComplete: false,
    ));
  }

  UnmodifiableListView<Task> get tasks => UnmodifiableListView(_tasks);

  Task getTask(int i) => _tasks[i];

  void addTask(String title, String notes, DateTime? date, TimeOfDay? time,
      int priority) {
    _tasks.add(Task(
      id: _tasks.last.id + 1,
      title: title,
      notes: notes,
      date: date,
      time: time,
      priority: priority,
      isComplete: false,
    ));
    notifyListeners();
  }

  void undoDeleteTask() {
    if (lastDeletedTask != null && lastDeletedTaskPosition != null) {
      _tasks.insert(lastDeletedTaskPosition!, lastDeletedTask!);
      notifyListeners();
    }
  }

  void deleteTask(int i) {
    lastDeletedTask = _tasks.removeAt(i);
    lastDeletedTaskPosition = i;
    print(
        "Removed task $i with ID ${lastDeletedTask?.id}, ${_tasks.length} tasks remaining in the list.");
    notifyListeners();
  }

  void modifyTask(int i, String title, String notes, DateTime? date,
      TimeOfDay? time, int priority) {
    _tasks[i].title = title;
    _tasks[i].notes = notes;
    _tasks[i].date = date;
    _tasks[i].time = time;
    _tasks[i].priority = priority;
    notifyListeners();
  }
}
