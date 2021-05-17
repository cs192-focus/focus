import 'dart:collection';

import 'package:flutter/material.dart';
import 'package:focus/dao/timers/timer.dart';

class TimerModel extends ChangeNotifier {
  final _timers = <Timer>[];
  int? lastDeletedTimerPosition;
  Timer? lastDeletedTimer;

  TimerModel() {
    _timers.add(Timer(
      id: 0,
      title: "Timer 1",
      notes: "",
      date: DateTime.now(),
      timeStart: TimeOfDay(hour: 0, minute: 0),
      timeEnd: TimeOfDay(hour: 0, minute: 30),
    ));
    _timers.add(Timer(
      id: 1,
      title: "Timer 2",
      notes: "",
      date: DateTime.now(),
      timeStart: TimeOfDay(hour: 17, minute: 0),
      timeEnd: TimeOfDay(hour: 17, minute: 30),
    ));
    _timers.add(Timer(
      id: 2,
      title: "Timer 3",
      notes: "",
      date: DateTime.now(),
      timeStart: TimeOfDay(hour: 21, minute: 0),
      timeEnd: TimeOfDay(hour: 21, minute: 30),
    ));
  }

  UnmodifiableListView<Timer> get timers => UnmodifiableListView(_timers);

  Timer getTimer(int i) => _timers[i];

  void addTimer(
    String title,
    String notes,
    DateTime date,
    TimeOfDay timeStart,
    TimeOfDay timeEnd,
  ) {
    _timers.add(Timer(
        id: _timers.last.id + 1,
        title: title,
        notes: notes,
        date: date,
        timeStart: timeStart,
        timeEnd: timeEnd));
    notifyListeners();
  }

  void undoDeleteTimer() {
    if (lastDeletedTimer != null && lastDeletedTimerPosition != null) {
      _timers.insert(lastDeletedTimerPosition!, lastDeletedTimer!);
      notifyListeners();
    }
  }

  void deleteTimer(int i) {
    lastDeletedTimer = _timers.removeAt(i);
    lastDeletedTimerPosition = i;
    print(
        "Removed timer $i with ID ${lastDeletedTimer?.id}, ${_timers.length} tasks remaining in the list.");
    notifyListeners();
  }
}
