import 'package:flutter/material.dart';

class Timer {
  int id;
  String title;
  String notes;
  DateTime date;
  TimeOfDay timeStart;
  TimeOfDay timeEnd;

  Timer({
    required this.id,
    required this.title,
    required this.notes,
    required this.date,
    required this.timeStart,
    required this.timeEnd,
  });
}
