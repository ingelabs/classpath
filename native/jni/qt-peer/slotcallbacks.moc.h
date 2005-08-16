/****************************************************************************
** Meta object code from reading C++ file 'slotcallbacks.cpp'
**
** Created: Tue Aug 16 00:30:30 2005
**      by: The Qt Meta Object Compiler version 58 (Qt 4.0.0)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'slotcallbacks.cpp' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 58
#error "This file was generated using the moc from 4.0.0. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

static const uint qt_meta_data_SlotCallback[] = {

 // content:
       1,       // revision
       0,       // classname
       0,    0, // classinfo
       6,   10, // methods
       0,    0, // properties
       0,    0, // enums/sets

 // slots: signature, parameters, type, tag, flags
      14,   13,   13,   13, 0x0a,
      38,   30,   13,   13, 0x0a,
      64,   58,   13,   13, 0x0a,
      85,   13,   13,   13, 0x0a,
     106,   99,   13,   13, 0x0a,
     132,  127,   13,   13, 0x0a,

       0        // eod
};

static const char qt_meta_stringdata_SlotCallback[] = {
    "SlotCallback\0\0buttonClicked()\0checked\0buttonToggled(bool)\0index\0"
    "choiceActivated(int)\0textChanged()\0action\0scrollBarAction(int)\0item\0"
    "listItemClicked(QListWidgetItem*)\0"
};

const QMetaObject SlotCallback::staticMetaObject = {
    { &QObject::staticMetaObject, qt_meta_stringdata_SlotCallback,
      qt_meta_data_SlotCallback, 0 }
};

const QMetaObject *SlotCallback::metaObject() const
{
    return &staticMetaObject;
}

void *SlotCallback::qt_metacast(const char *_clname)
{
    if (!_clname) return 0;
    if (!strcmp(_clname, qt_meta_stringdata_SlotCallback))
	return static_cast<void*>(const_cast<SlotCallback*>(this));
    return QObject::qt_metacast(_clname);
}

int SlotCallback::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QObject::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    if (_c == QMetaObject::InvokeMetaMethod) {
        switch (_id) {
        case 0: buttonClicked(); break;
        case 1: buttonToggled(*(bool*)_a[1]); break;
        case 2: choiceActivated(*(int*)_a[1]); break;
        case 3: textChanged(); break;
        case 4: scrollBarAction(*(int*)_a[1]); break;
        case 5: listItemClicked(*(QListWidgetItem**)_a[1]); break;
        }
        _id -= 6;
    }
    return _id;
}
