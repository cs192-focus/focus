import 'package:flutter/material.dart';
import 'package:focus/dao/timers/modtimer.dart';
import 'package:focus/dao/timers/timermodel.dart';
import 'package:intl/intl.dart';
import 'package:provider/provider.dart';
import 'package:focus/dao/timers/timer.dart';

/// A chip representing the Date.
class DateChip extends StatelessWidget {
  final DateTime? date;

  const DateChip({Key? key, required this.date}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return InputChip(
      labelPadding: EdgeInsets.fromLTRB(4, 0, 8, 0),
      avatar: Icon(Icons.calendar_today_outlined, size: 18.0),
      label: Text(DateFormat('MMM d').format(date!)),
      onSelected: (bool value) {},
    );
  }
}

/// A chip representing the Time.
class TimeChip extends StatelessWidget {
  final TimeOfDay timeStart;
  final TimeOfDay timeEnd;

  const TimeChip({Key? key, required this.timeStart, required this.timeEnd})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return InputChip(
      labelPadding: EdgeInsets.fromLTRB(4, 0, 8, 0),
      avatar: Icon(Icons.alarm_outlined, size: 18.0),
      label: Text("${timeStart.format(context)} - ${timeEnd.format(context)}"),
      onSelected: (bool value) {},
    );
  }
}

/// An item in the TimerList.
class TimerListItem extends StatelessWidget {
  final Timer timer;
  final String title;
  final List<Widget> info;

  const TimerListItem({
    Key? key,
    required this.timer,
    required this.title,
    required this.info,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: () async {
        final bool? modify = await _asyncConfirmationDialog(context, timer);
        if (modify != null && modify == true) {
          showModifyTimerModal(context, timer);
        }
      },
      child: Container(
        padding: EdgeInsets.fromLTRB(20, 10, 20, 10),
        child: Row(
          children: <Widget>[
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  Row(
                    children: <Widget>[
                      Text(
                        title,
                        style: TextStyle(
                          fontWeight: FontWeight.w400,
                          fontSize: 18.0,
                        ),
                      )
                    ],
                  ),
                  Row(children: info),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class TimerList extends StatefulWidget {
  @override
  _TimerListState createState() => _TimerListState();
}

class _TimerListState extends State<TimerList> {
  @override
  Widget build(BuildContext context) {
    return Consumer<TimerModel>(
      builder: (context, timerlist, child) {
        if (timerlist.timers.isEmpty) {
          return Center(child: Text("No timers!"));
        }
        return ListView.builder(
          padding: EdgeInsets.only(top: 20),
          itemCount: (timerlist.timers.length * 2) - 1,
          itemBuilder: (context, i) {
            if (i.isOdd) return Divider();
            int posn = i ~/ 2;
            var t = timerlist.getTimer(posn);
            var widgets = <Widget>[];
            widgets.add(DateChip(date: t.date));
            widgets.add(TimeChip(timeStart: t.timeStart, timeEnd: t.timeEnd));
            return Dismissible(
              background: Container(
                  color: Colors.red, child: Icon(Icons.delete_outline)),
              key: Key(t.id.toString()),
              onDismissed: (direction) {
                timerlist.deleteTimer(posn);
                ScaffoldMessenger.of(context).showSnackBar(SnackBar(
                  content: Text('Timer deleted.'),
                  action: SnackBarAction(
                    label: 'Undo',
                    onPressed: () {
                      timerlist.undoDeleteTimer();
                    },
                  ),
                ));
              },
              child: TimerListItem(timer: t, title: t.title, info: widgets),
            );
          },
        );
      },
    );
  }
}

Future<bool?> _asyncConfirmationDialog(BuildContext context, Timer timer) async {
  return showDialog<bool>(
    context: context,
    barrierDismissible: false, // user must tap button for close dialog!
    builder: (BuildContext context) {
      return AlertDialog(
        title: Text(timer.title),
        content: Container(
          width: 300.0,
          height: 220.0,
          child: Column(
            children: [
              Row(
                children: [
                  DateChip(date: timer.date),
                  SizedBox(width: 10),
                  TimeChip(timeStart: timer.timeStart, timeEnd: timer.timeEnd),
                ],
              ),
              Column(
                children: [
                  SizedBox(
                    width: double.infinity,
                    child: Container(
                      height: 170.0,
                      child: Expanded(
                        child: SingleChildScrollView(
                          scrollDirection: Axis.vertical,
                          child: Text(
                            timer.notes,
                            textAlign: TextAlign.left,
                          ),
                        ),
                      ),
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
        actions: <Widget>[
          TextButton(
            child: const Text('Cancel'),
            onPressed: () {
              Navigator.pop(context, false);
            },
          ),
          TextButton(
            child: const Text('Modify'),
            onPressed: () {
              Navigator.pop(context, true);
            },
          ),
        ],
      );
    },
  );
}