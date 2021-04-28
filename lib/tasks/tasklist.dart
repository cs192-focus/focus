import 'package:flutter/material.dart';
import 'package:focus/tasks/taskmodel.dart';
import 'package:intl/intl.dart';
import 'package:provider/provider.dart';

Icon priorityLabel(int i, {double? size}) {
  switch (i) {
    case 0:
      return Icon(Icons.label, color: Colors.red, size: size);
    case 1:
      return Icon(Icons.label, color: Colors.yellow, size: size);
    case 2:
      return Icon(Icons.label, color: Colors.blue, size: size);
    case 3:
    default:
      return Icon(Icons.label_outlined, size: size);
  }
}

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
  final TimeOfDay? time;

  const TimeChip({Key? key, required this.time}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return InputChip(
      labelPadding: EdgeInsets.fromLTRB(4, 0, 8, 0),
      avatar: Icon(Icons.alarm_outlined, size: 18.0),
      label: Text(time!.format(context)),
      onSelected: (bool value) {},
    );
  }
}

/// An item in the TaskList.
class TaskListItem extends StatelessWidget {
  final String title;
  final bool value;
  final int priority;
  final List<Widget> info;
  final Function onChanged;

  const TaskListItem(
      {Key? key,
      required this.title,
      required this.info,
      required this.value,
      required this.priority,
      required this.onChanged})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return InkWell(
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
                      priorityLabel(priority, size: 18),
                      SizedBox(width: 10),
                      Text(
                        title,
                        style: TextStyle(
                            color: value
                                ? Theme.of(context).disabledColor
                                : Theme.of(context).textTheme.bodyText2!.color,
                            fontWeight: FontWeight.w400,
                            fontSize: 18.0,
                            decoration: value
                                ? TextDecoration.lineThrough
                                : TextDecoration.none),
                      )
                    ],
                  ),
                  Row(children: info),
                ],
              ),
            ),
            Checkbox(
              value: value,
              onChanged: (bool? newValue) {
                onChanged(newValue);
              },
            ),
          ],
        ),
      ),
    );
  }
}

class TaskList extends StatefulWidget {
  @override
  _TaskListState createState() => _TaskListState();
}

class _TaskListState extends State<TaskList> {
  @override
  Widget build(BuildContext context) {
    return Consumer<TaskModel>(
      builder: (context, tasklist, child) {
        if (tasklist.tasks.isEmpty) {
          return Center(child: Text("No tasks!"));
        }
        return ListView.builder(
          padding: EdgeInsets.only(top: 20),
          itemCount: (tasklist.tasks.length * 2) - 1,
          itemBuilder: (context, i) {
            if (i.isOdd) return Divider();
            int posn = i ~/ 2;
            var t = tasklist.getTask(posn);
            var widgets = <Widget>[];
            if (t.date != null) {
              widgets.add(DateChip(date: t.date));
            }
            if (t.time != null) {
              widgets.add(TimeChip(time: t.time));
            }
            return Dismissible(
              background: Container(
                color: Colors.red,
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.start,
                  children: [
                    SizedBox(width: 20),
                    Icon(Icons.delete_outline, size: 32)
                  ],
                ),
              ),
              key: Key(t.id.toString()),
              onDismissed: (direction) {
                tasklist.deleteTask(posn);
              },
              child: TaskListItem(
                title: t.title,
                info: widgets,
                value: t.isComplete,
                priority: t.priority,
                onChanged: (bool newValue) {
                  setState(() {
                    t.isComplete = newValue;
                  });
                },
              ),
            );
          },
        );
      },
    );
  }
}
