import 'package:flutter/material.dart';

class Task {
  int id;
  String title;
  String notes;
  DateTime? date;
  TimeOfDay? time;
  int priority;
  bool isComplete;

  Task(
      {required this.id,
      required this.title,
      required this.notes,
      this.date,
      this.time,
      required this.priority,
      required this.isComplete});
}
