grammar antlr;

program: func* city EOF;
city: 'city ' name '{' blocks '}';

blocks: block+;
block: road | bikepath | tourpath | coridor | building | river | park | marked;

road: 'road ' name '{' commands '}';

bikepath: 'bikePath ' name '{' commands '}';

tourpath: 'bikeTourPath ' name '{' commands '}';

coridor: 'bikeCoridor ' name '{' commands '}';

building: 'building ' name '{' commands '}';

river: 'river ' name '{' commands '}';

park: 'park ' name '{' commands '}';

marked: bikestand | bikeshed | mbajk;
bikestand: 'bikeStand ' name point;
bikeshed: 'bikeShed ' name point;
mbajk: 'mbajk ' name point;

commands: command+;
command: line | bend | box | circ | for | if | variable | const;
line: 'line(' point ', ' point ', ' width ')';
width: exp;

bend: 'bend(' point ', ' point ', ' angle ')';
angle: INTEGER;

box : 'box(' point ', ' point')';

circ : 'circ(' point ', ' radius')';
radius : exp;

for: 'for ' spr ' to ' spr '{' commands '}';

if: 'if ' spr operator spr '{' commands '}' elseif* else?;

elseif: 'else if ' spr operator spr '{' commands '}';

else: '{' commands '}';

operator: '>'|'=>'|'<='|'<'|'=='|'!=';

spr: number | variable_name;

point: '('exp',' exp')';

variable: 'var ' variable_name '=' value';';

const: 'const ' variable_name '=' value';';

value: number | variable_name | point | call;
call: 'call ' name '(' callargs ')';
callargs: random(',' random)*;
random: variable_name | number;

func: 'func ' name '('args')' '{'commands return'};';
args: variable_name (',' variable_name)*;
return: 'return ' block |'return ' point |'return ' number |'return ' variable_name;

name: WORD;
variable_name: WORD;
WORD: VALID_CHAR+;
VALID_CHAR: ('a' .. 'z') | ('A' .. 'Z');
number: INTEGER ('.' INTEGER)?;
INTEGER: DIGIT+;
exp: e;
e: t ee;
ee: ('+' t ee)*;
t: f tt;
tt: ('*' f tt)*;
f: '('e')' | number | variable_name;
DIGIT: ('0' .. '9');