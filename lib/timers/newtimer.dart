import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:focus/timers/timermodel.dart';
import 'package:intl/intl.dart';
import 'package:provider/provider.dart';

void showAddTimerModal(BuildContext context) => showModalBottomSheet(
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
        child: AddTimerForm(),
      ),
    );

class AddTimerForm extends StatefulWidget {
  const AddTimerForm({Key? key}) : super(key: key);

  @override
  _AddTimerFormState createState() => _AddTimerFormState();
}

class _AddTimerFormState extends State<AddTimerForm> {
  final _formKey = GlobalKey<FormState>();

  bool submittable = false;

  TextEditingController titleController = TextEditingController();

  bool dateChosen = false;
  DateTime _date = DateTime.now();
  String _dateString = "No date";

  bool timeStartChosen = false;
  TimeOfDay _timeStart = TimeOfDay(hour: 0, minute: 0);
  String _timeStartString = "No start time";

  bool timeEndChosen = false;
  TimeOfDay _timeEnd = TimeOfDay(hour: 0, minute: 30);
  String _timeEndString = "No end time";

  TextEditingController noteController = TextEditingController();
  String _notesString = "Add note";
  bool savedCorrectly = false;

  Future<void> _selectDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: _date,
      firstDate: DateTime.now(),
      lastDate: DateTime(2101),
    );
    if (picked != null && picked != _date)
      setState(() {
        _date = picked;
        _dateString = DateFormat('MMM d').format(picked);
        dateChosen = true;
      });
  }

  Future<void> _selectTimeStart(BuildContext context) async {
    final TimeOfDay? picked = await showTimePicker(
      context: context,
      initialTime: _timeStart,
    );
    setState(() {
      if (picked != null && picked != _timeStart) {
        timeStartChosen = true;
        _timeStart = picked;
        _timeStartString = "Starts at ${picked.format(context)}";
      }
    });
  }

  Future<void> _selectTimeEnd(BuildContext context) async {
    final TimeOfDay? picked = await showTimePicker(
      context: context,
      initialTime: _timeEnd,
    );
    setState(() {
      if (picked != null && picked != _timeEnd) {
        timeEndChosen = true;
        _timeEnd = picked;
        _timeEndString = "Ends at ${picked.format(context)}";
      }
    });
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
    var taskTitle = TextFormField(
      controller: titleController,
      decoration: const InputDecoration(
        contentPadding: EdgeInsets.zero,
        border: InputBorder.none,
        errorStyle: TextStyle(height: 0, color: Colors.transparent),
        hintText: 'Add a timer',
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
    var taskTimeStart = ActionChip(
      labelPadding: EdgeInsets.fromLTRB(4, 0, 8, 0),
      avatar: Icon(Icons.alarm_outlined, size: 18.0),
      label: Text(_timeStartString),
      onPressed: () => _selectTimeStart(context),
    );
    var taskTimeEnd = ActionChip(
      labelPadding: EdgeInsets.fromLTRB(4, 0, 8, 0),
      avatar: Icon(Icons.alarm_outlined, size: 18.0),
      label: Text(_timeEndString),
      onPressed: () => _selectTimeEnd(context),
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
        children: <Widget>[
          taskTitle,
          SingleChildScrollView(
            scrollDirection: Axis.horizontal,
            child: Row(
              children: [
                taskDate,
                SizedBox(width: 10),
                taskTimeStart,
                SizedBox(width: 10),
                taskTimeEnd,
                SizedBox(width: 10),
                taskAddNotes
              ],
            ),
          ),
          Spacer(),
          Row(mainAxisAlignment: MainAxisAlignment.end, children: [
            TextButton.icon(
              onPressed: (submittable &&
                      dateChosen &&
                      timeStartChosen &&
                      timeEndChosen)
                  ? () {
                      if (_formKey.currentState!.validate()) {
                        String title = titleController.text;
                        String notes = noteController.text;
                        titleController.clear();
                        noteController.clear();
                        Provider.of<TimerModel>(context, listen: false)
                            .addTimer(
                                title, notes, _date, _timeStart, _timeEnd);
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
