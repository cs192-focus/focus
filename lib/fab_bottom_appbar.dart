import 'package:flutter/material.dart';

class FABBottomAppBarItem {
  FABBottomAppBarItem({required this.iconData, required this.text});
  IconData iconData;
  String text;
}

class FABBottomAppBar extends StatefulWidget {
  final List<FABBottomAppBarItem> items;
  final ValueChanged<int> onTabSelected;
  final Color color;
  final Color selectedColor;
  final double height;
  final double iconSize;

  const FABBottomAppBar(
      {Key? key,
      required this.items,
      required this.onTabSelected,
      required this.color,
      required this.selectedColor,
      required this.height,
      required this.iconSize})
      : super(key: key);

  @override
  State<StatefulWidget> createState() => FABBottomAppBarState();
}

class FABBottomAppBarState extends State<FABBottomAppBar> {
  int _selectedIndex = 0;

  _updateIndex(int index) {
    widget.onTabSelected(index);
    setState(() {
      _selectedIndex = index;
    });
  }

  Widget _buildTabItem({
    required FABBottomAppBarItem item,
    required int index,
    required ValueChanged<int> onPressed,
  }) {
    Color color = _selectedIndex == index ? widget.selectedColor : widget.color;
    return Expanded(
      child: SizedBox(
        height: widget.height,
        child: Material(
          type: MaterialType.transparency,
          child: InkWell(
            onTap: () => onPressed(index),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                Icon(item.iconData, color: color, size: widget.iconSize),
                Text(
                  item.text,
                  style: TextStyle(color: color),
                )
              ],
            ),
          ),
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    List<Widget> items = List.generate(widget.items.length, (int index) {
      return _buildTabItem(
        item: widget.items[index],
        index: index,
        onPressed: _updateIndex,
      );
    });

    return BottomAppBar(
      child: Row(
        mainAxisSize: MainAxisSize.max,
        mainAxisAlignment: MainAxisAlignment.spaceAround,
        children: items,
      ),
    );
  }
}
