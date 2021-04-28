import 'package:focus/timers/newtimer.dart';
import 'package:focus/timers/timerlist.dart';
import 'package:focus/timers/timermodel.dart';
import 'package:provider/provider.dart';
import 'package:flutter/material.dart';
import 'package:focus/fab_bottom_appbar.dart';
import 'package:focus/strings.dart';
import 'package:focus/tasks/newtask.dart';
import 'package:focus/tasks/tasklist.dart';
import 'package:flutter_speed_dial/flutter_speed_dial.dart';
import 'package:focus/tasks/taskmodel.dart';

void main() => runApp(MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (context) => TaskModel()),
        ChangeNotifierProvider(create: (context) => TimerModel()),
      ],
      child: MyApp(),
    ));

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: Strings.title,
      darkTheme: ThemeData(
        brightness: Brightness.dark,
        accentColor: Colors.red,
      ),
      theme: ThemeData(
        primarySwatch: Colors.red,
      ),
      themeMode: ThemeMode.dark,
      home: FocusHomePage(
        title: Strings.title,
      ),
    );
  }
}

class FocusHomePage extends StatefulWidget {
  final String title;

  const FocusHomePage({Key? key, required this.title}) : super(key: key);

  @override
  _FocusHomePageState createState() => _FocusHomePageState();
}

class _FocusHomePageState extends State<FocusHomePage> {
  var lists = <Widget>[TaskList(), TimerList()];
  var index = 0;

  @override
  void _selectedTab(int i) {
    setState(() {
      index = i;
    });
  }

  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
        centerTitle: true,
      ),
      body: lists[index],
      floatingActionButtonLocation: FloatingActionButtonLocation.centerDocked,
      floatingActionButton: SpeedDial(
        icon: Icons.add,
        activeIcon: Icons.close,
        buttonSize: 56.0,
        visible: true,

        /// If true overlay will render no matter what.
        renderOverlay: true,
        curve: Curves.bounceIn,
        overlayColor: Colors.black,
        overlayOpacity: 0.5,
        tooltip: 'Add task or timer',
        heroTag: 'speed-dial-hero-tag',
        backgroundColor: Theme.of(context).accentColor,
        foregroundColor: Theme.of(context).colorScheme.onSurface,
        activeBackgroundColor: Theme.of(context).colorScheme.surface,
        elevation: 8.0,
        shape: CircleBorder(),
        // orientation: SpeedDialOrientation.Up,
        // childMarginBottom: 2,
        // childMarginTop: 2,
        children: [
          SpeedDialChild(
            child: Icon(Icons.assignment_outlined),
            backgroundColor: Colors.red,
            label: 'New task',
            labelStyle: TextStyle(fontSize: 16.0),
            onTap: () => showAddTaskModal(context),
          ),
          SpeedDialChild(
            child: Icon(Icons.timer_outlined),
            backgroundColor: Colors.blue,
            label: 'New timer',
            labelStyle: TextStyle(fontSize: 16.0),
            onTap: () => showAddTimerModal(context),
          ),
        ],
      ),
      bottomNavigationBar: FABBottomAppBar(
        color: Theme.of(context).colorScheme.onSurface.withOpacity(.6),
        selectedColor: Theme.of(context).colorScheme.onSurface,
        height: 64.0,
        iconSize: 24.0,
        onTabSelected: _selectedTab,
        items: [
          FABBottomAppBarItem(
              iconData: Icons.assignment_outlined, text: 'Tasks'),
          FABBottomAppBarItem(iconData: Icons.timer_outlined, text: 'Timers'),
        ],
      ),
    );
  }
}
