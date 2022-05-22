import java.io.InputStream
import java.util.*
import java.io.File

const val EOF_SYMBOL = -1
const val ERROR_STATE = 0
const val SKIP_VALUE = 0
const val EOF_VALUE = -1

const val INT = 1
const val FLOAT = 2
const val IDENTIFIER = 3
const val COMMA = 4
const val L_PAREN = 5
const val R_PAREN = 6
const val LB_PAREN = 7
const val RB_PAREN = 8
const val ASSIGN = 9
const val MORE_OR_EQUAL = 10
const val EQUAL = 11
const val LESS = 12
const val LESS_OR_EQUAL = 13
const val MORE = 14
const val NOT_EQUAL = 15
const val POW = 16
const val PLUS = 17
const val MINUS = 18
const val TIMES = 19
const val DIVIDE = 20
const val BEND = 21
const val BIKE_CORRIDOR = 22
const val BIKE_PATH = 23
const val BIKE_SHED = 24
const val BIKE_STAND = 25
const val BIKE_TOUR_PATH = 26
const val BOX = 27
const val BUILDING = 28
const val CALL = 29
const val CONST = 30
const val CIRC = 31
const val CITY = 32
const val ELSE = 33
const val ELSE_IF = 34
const val FOR = 35
const val FUNC = 36
const val IF = 37
const val LINE = 38
const val M_BAJK = 39
const val PARK = 40
const val RENT_BIKE = 41
const val RETURN = 42
const val RIVER = 43
const val ROAD = 44
const val TO = 45
const val VAR = 46
const val SEMI = 47

const val NEWLINE = '\n'.code

val RESERVED_WORDS = mapOf(
    "bend" to BEND, "bikeCorridor" to BIKE_CORRIDOR, "bikePath" to BIKE_PATH, "bikeShed" to BIKE_SHED,
    "bikeStand" to BIKE_STAND, "bikeTourPath" to BIKE_TOUR_PATH, "box" to BOX, "building" to BUILDING, "call" to CALL,
    "const" to CONST, "circ" to CIRC, "city" to CITY, "else" to ELSE, "elseif" to ELSE_IF, "for" to FOR, "func" to FUNC,
    "if" to IF, "line" to LINE, "mBajk" to M_BAJK, "park" to PARK, "rentBike" to RENT_BIKE, "return" to RETURN,
    "river" to RIVER, "road" to ROAD, "to" to TO, "var" to VAR
)

interface Automaton {
    val states: Set<Int>
    val alphabet: IntRange
    fun next(state: Int, symbol: Int): Int
    fun value(state: Int): Int
    val startState: Int
    val finalStates: Set<Int>
}

object Example : Automaton {
    override val states =
        setOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29)
    override val alphabet = 0..255
    override val startState = 1
    override val finalStates =
        setOf(1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 19, 20, 21, 22, 23, 24, 26, 29)

    private val numberOfStates = states.maxOrNull()!! + 1
    private val numberOfSymbols = alphabet.maxOrNull()!! + 1
    private val transitions = Array(numberOfStates) { IntArray(numberOfSymbols) }
    private val values: Array<Int> = Array(numberOfStates) { 0 }

    private fun setTransition(from: Int, symbol: Char, to: Int) {
        transitions[from][symbol.code] = to
    }

    private fun setValue(state: Int, terminal: Int) {
        values[state] = terminal
    }

    override fun next(state: Int, symbol: Int): Int =
        if (symbol == EOF_SYMBOL) ERROR_STATE
        else {
            assert(states.contains(state))
            assert(alphabet.contains(symbol))
            transitions[state][symbol]
        }

    override fun value(state: Int): Int {
        assert(states.contains(state))
        return values[state]
    }

    init {
        for (symbol in '0'..'9') {
            setTransition(1, symbol, 2)
            setTransition(2, symbol, 2)
        }
        setValue(2, INT)

        setTransition(2, '.', 3)
        for (symbol in '0'..'9') {
            setTransition(3, symbol, 4)
            setTransition(4, symbol, 4)
        }
        setValue(4, FLOAT)

        for (symbol in 'A'..'Z') {
            setTransition(1, symbol, 5)
            setTransition(5, symbol, 5)
        }
        for (symbol in 'a'..'z') {
            setTransition(1, symbol, 5)
            setTransition(5, symbol, 5)
        }
        for (symbol in '0'..'9') {
            setTransition(5, symbol, 6)
            setTransition(6, symbol, 6)
        }
        setValue(5, IDENTIFIER)
        setValue(6, IDENTIFIER)

        setTransition(1, ',', 7)
        setValue(7, COMMA)
        setTransition(1, '(', 8)
        setValue(8, L_PAREN)
        setTransition(1, ')', 9)
        setValue(9, R_PAREN)
        setTransition(1, '{', 10)
        setValue(10, LB_PAREN)
        setTransition(1, '}', 11)
        setValue(11, RB_PAREN)
        setTransition(1, '=', 12)
        setValue(12, ASSIGN)
        setTransition(12, '>', 13)
        setValue(13, MORE_OR_EQUAL)
        setTransition(12, '=', 14)
        setValue(14, EQUAL)
        setTransition(1, '<', 15)
        setValue(15, LESS)
        setTransition(15, '=', 16)
        setValue(16, LESS_OR_EQUAL)
        setTransition(1, '>', 17)
        setValue(17, MORE)
        setTransition(1, '^', 20)
        setValue(20, POW)
        setTransition(1, '+', 21)
        setValue(21, PLUS)
        setTransition(1, '-', 22)
        setValue(22, MINUS)
        setTransition(1, '*', 23)
        setValue(23, TIMES)
        setTransition(1, '/', 24)
        setValue(24, DIVIDE)
        setTransition(24, '/', 25)
        for (symbol in alphabet) {
            setTransition(25, symbol.toChar(), 25)
        }
        setTransition(25, '\n', 26)
        setValue(26, SKIP_VALUE)
        setTransition(24, '*', 27)
        for (symbol in alphabet) {
            setTransition(27, symbol.toChar(), 27)
        }
        setTransition(27, '*', 28)
        for (symbol in alphabet) {
            setTransition(28, symbol.toChar(), 27)
        }
        setTransition(28, '/', 26)
        setTransition(1, '!', 18)
        setTransition(18, '=', 19)
        setValue(19, NOT_EQUAL)
        setTransition(1, ' ', 26)
        setTransition(1, '\n', 26)
        setTransition(1, '\t', 26)
        setTransition(1, '\r', 26)

        setTransition(1, ';', 29)
        setValue(29, SEMI)
    }
}

data class Token(val value: Int, val lexeme: String, val startRow: Int, val startColumn: Int)

interface Scanner {
    fun eof(): Boolean
    fun getToken(): Token?
}

class StreamScanner(private val automaton: Automaton, private val stream: InputStream) : Scanner {
    private var state = automaton.startState
    private var last: Int? = null
    private var buffer = LinkedList<Byte>()
    private var row = 1
    private var column = 1

    private fun updatePosition(symbol: Int) {
        if (symbol == NEWLINE) {
            row += 1
            column += 1
        } else {
            column += 1
        }
    }

    private fun reverseUpdatePosition(symbol: Int) {
        if (symbol == NEWLINE) {
            row -= 1
            column -= 1
        } else {
            column -= 1
        }
    }

    private fun getValue(): Int {
        var symbol = last ?: stream.read()
        state = automaton.startState

        while (true) {
            updatePosition(symbol)

            val nextState = automaton.next(state, symbol)
            if (nextState == ERROR_STATE) {
                if (automaton.finalStates.contains(state)) {
                    reverseUpdatePosition(symbol)
                    last = symbol
                    if (automaton.value(state) == IDENTIFIER) {
                        val reservedWord = RESERVED_WORDS[String(buffer.toByteArray())]
                        if (reservedWord != null) {
                            return reservedWord
                        }
                    }
                    return automaton.value(state)
                } else throw Error("Invalid pattern at ${row}:${column}")
            }
            state = nextState
            buffer.add(symbol.toByte())
            symbol = stream.read()
        }
    }

    override fun eof(): Boolean =
        last == EOF_SYMBOL

    override fun getToken(): Token? {
        if (eof())
            return Token(EOF_VALUE, "", row, column)

        val startRow = row
        val startColumn = column
        buffer.clear()

        val value = getValue()
        return if (value == SKIP_VALUE)
            getToken()
        else
            Token(value, String(buffer.toByteArray()), startRow, startColumn)
    }
}

fun name(value: Int) =
    when (value) {
        INT -> "INT"
        FLOAT -> "FLOAT"
        IDENTIFIER -> "IDENTIFIER"
        COMMA -> "COMMA"
        L_PAREN -> "L_PAREN"
        R_PAREN -> "R_PAREN"
        LB_PAREN -> "LB_PAREN"
        RB_PAREN -> "RB_PAREN"
        ASSIGN -> "ASSIGN"
        MORE_OR_EQUAL -> "MORE_OR_EQUAL"
        EQUAL -> "EQUAL"
        LESS -> "LESS"
        LESS_OR_EQUAL -> "LESS_OR_EQUAL"
        MORE -> "MORE"
        NOT_EQUAL -> "NOT_EQUAL"
        POW -> "POW"
        PLUS -> "PLUS"
        MINUS -> "MINUS"
        TIMES -> "TIMES"
        DIVIDE -> "DIVIDE"
        BEND -> "BEND"
        BIKE_CORRIDOR -> "BIKE_CORRIDOR"
        BIKE_PATH -> "BIKE_PATH"
        BIKE_SHED -> "BIKE_SHED"
        BIKE_STAND -> "BIKE_STAND"
        BIKE_TOUR_PATH -> "BIKE_TOUR_PATH"
        BOX -> "BOX"
        BUILDING -> "BUILDING"
        CALL -> "CALL"
        CONST -> "CONST"
        CIRC -> "CIRC"
        CITY -> "CITY"
        ELSE -> "ELSE"
        ELSE_IF -> "ELSE_IF"
        FOR -> "FOR"
        FUNC -> "FUNC"
        IF -> "IF"
        LINE -> "LINE"
        M_BAJK -> "M_BAJK"
        PARK -> "PARK"
        RENT_BIKE -> "RENT_BIKE"
        RETURN -> "RETURN"
        RIVER -> "RIVER"
        ROAD -> "ROAD"
        TO -> "TO"
        VAR -> "VAR"
        SEMI -> "SEMI"
        else -> throw Error("Invalid value")
    }

fun printTokens(scanner: Scanner) {
    val token = scanner.getToken()
    if (token != null) {
        if (token.value != EOF_VALUE) {
            print("${name(token.value)}(\"${token.lexeme}\") ")
            printTokens(scanner)
        }
    }
}

interface Expr {
    fun evalPartial(env: Map<String, Expr>): Expr

    // Privzete implementacije, tako se lahko izognemo preverjanju tipov, če funkcija ni implementirana pomeni, da ni podprta in se proži izjema
    fun add(other: Expr): Expr =
        throw TypeException

    fun sub(other: Expr): Expr =
        throw TypeException

    fun unaryMinus(): Expr =
        throw TypeException

    fun unaryPlus(): Expr =
        throw TypeException

    fun mul(other: Expr): Expr =
        throw TypeException

    fun div(other: Expr): Expr =
        throw TypeException

    // Namesto, da vrnete string lahko uporabite knjižnico za JSON in tukaj vrnete JSON objekt
    // Druga opcija pa je, da naredite objektni model za GEOJSON in nato ta objektni model pretvorite v JSON objekte
    fun toGeoJSON(): String =
        throw TypeException
}

var currentBlock="null"
var blockColor=mapOf("road" to "#555555", "bikePath" to "#63475c", "bikeTourPath" to "#6d307a", "corridor" to "#881199", "building" to "#545655",
    "river" to "#096ca2", "park" to "#447238", "bikeStand" to "#aa0004", "bikeShed" to "#716c39", "mBajk" to "#0317a7", "rentBike" to "#ff8000"
)

class Function(private var params: MutableList<String>, private var body: Stmt, private var closureEnv: Map<String, Expr> = emptyMap(), override var next:Stmt): Stmt {
    override fun toString(): String {
        return "{($params) ->\n $body)}"
    }

    override fun evalPartial(env: Map<String, Expr>): Stmt {
        return Function(params, body, closureEnv, next)
    }

    fun apply(args: MutableList<Expr>): Expr {
        if(params.size != args.size){
            throw Exception("Function params != call args")
        }else{
            for(i in params.indices){
                closureEnv=closureEnv+(params[i] to args[i])
            }
            val result = body.evalPartial(closureEnv)
            var itResult=result
            while(itResult.next!=End && itResult.next!=Null){
                itResult=itResult.next
            }
            if(itResult is Return && itResult.next is End){
                if(itResult.value is Point || itResult.value is Num) {
                    return itResult.value
                }else{
                    throw TypeException
                }
            }else{
                throw Exception("Function must end with return")
            }
        }
    }
}

class Call(private val func: Stmt, private val args: MutableList<Expr>) : Expr {

    override fun toString(): String =
        "$func($args)"

    // Ovredonti argumente in pokliče apply
    override fun evalPartial(env: Map<String, Expr>): Expr =
        when (val f = func.evalPartial(env)) {
            is Function -> {
                val resultArgs: MutableList<Expr> = mutableListOf()
                for(arg in args){
                    resultArgs.add(arg.evalPartial(env))
                }
                f.apply(resultArgs)
            }
            else -> throw TypeException
        }
}

class Point(val longitude: Expr, val latitude: Expr): Expr {
    override fun toString(): String =
        "($longitude,$latitude)"

    override fun evalPartial(env: Map<String, Expr>): Expr =
        Point(longitude.evalPartial(env), latitude.evalPartial(env))

    override fun toGeoJSON(): String =
        when {
            longitude is Num && latitude is Num -> "[" + longitude.toGeoJSON() + ", " + latitude.toGeoJSON() + "]"
            else -> throw TypeException
        }
}

class Add(private val left: Expr, private val  right: Expr): Expr{
    override fun toString(): String =
        "($left + $right)"

    override fun evalPartial(env: Map<String, Expr>): Expr =
        left.evalPartial(env).add(right.evalPartial(env))
}

class Sub(private val left: Expr, private val  right: Expr): Expr{
    override fun toString(): String =
        "($left - $right)"

    override fun evalPartial(env: Map<String, Expr>): Expr =
        left.evalPartial(env).sub(right.evalPartial(env))
}

class Mul(private val left: Expr, private val  right: Expr): Expr{
    override fun toString(): String =
        "($left * $right)"

    override fun evalPartial(env: Map<String, Expr>): Expr =
        left.evalPartial(env).mul(right.evalPartial(env))
}

class Div(private val left: Expr, private val  right: Expr): Expr{
    override fun toString(): String{
        return "($left/$right)"
    }

    override fun evalPartial(env: Map<String, Expr>): Expr =
        left.evalPartial(env).div(right.evalPartial(env))
}

class Pow(private val left: Expr, private val  right: Expr): Expr{
    override fun toString(): String{
        return "$left^$right"
    }

    override fun evalPartial(env: Map<String, Expr>): Expr{
        return this
    }
}

class UnaryPlus(private val value: Expr): Expr{
    override fun toString(): String{
        return "+$value"
    }

    override fun evalPartial(env: Map<String, Expr>): Expr{
        return value.unaryPlus()
    }
}

class UnaryMinus(private val value: Expr): Expr{
    override fun toString(): String{
        return "-$value"
    }

    override fun evalPartial(env: Map<String, Expr>): Expr{
        return value.unaryMinus()
    }
}

class Var(private val name: String): Expr {
    override fun toString(): String =
        when(name[0]){
           '-'->"-const$name"
           else->name
        }

    override fun evalPartial(env: Map<String, Expr>): Expr =
        if(env.contains(name) || env.contains("-const$name")){
            if(env.contains(name)){
                env[name]!!
            }else{
                env["-const$name"]!!
            }
        }else{
            throw UndefinedException(name)
        }
}

class Num(var value: Double): Expr {
    override fun toString(): String =
        value.toString()

    override fun evalPartial(env: Map<String, Expr>): Expr =
        this

    override fun add(other: Expr): Expr =
        when(other) {
            is Num -> Num(value + other.value)
            else -> throw TypeException
        }

    override fun sub(other: Expr): Expr =
        when(other) {
            is Num -> Num(value - other.value)
            else -> throw TypeException
        }

    override fun mul(other: Expr): Expr =
        when(other) {
            is Num -> Num(value * other.value)
            else -> throw TypeException
        }

    override fun div(other: Expr): Expr =
        when(other) {
            is Num -> Num(value / other.value)
            else -> throw TypeException
        }

    override fun unaryMinus(): Expr =
        Num(-value)

    override fun unaryPlus(): Expr =
        this

    override fun toGeoJSON(): String =
        value.toString()
}

interface Stmt {
    var next: Stmt

    fun evalPartial(env: Map<String, Expr>): Stmt

    fun toGeoJSON(): String =
        throw TypeException
}

class Line(private val from: Expr, private val to: Expr, private val width: Expr, override var next: Stmt): Stmt {
    override fun toString(): String =
        "\tline($from,$to, $width)\n$next"

    override fun evalPartial(env: Map<String, Expr>): Stmt =
        Line(from.evalPartial(env), to.evalPartial(env), width.evalPartial(env), next.evalPartial(env))

    override fun toGeoJSON(): String {
        when {
            from is Point && to is Point && width is Num -> {
                if (from.longitude == to.longitude && from.latitude == to.latitude) {
                    throw LineException()
                }else{
                    return  "    {\n" +
                            "      \"type\": \"Feature\",\n" +
                            "      \"properties\": {\n" +
                            "        \"stroke-width\": ${width.toGeoJSON()},\n" +
                            "        \"stroke\": \"${blockColor[currentBlock]}\",\n"+
                            "        \"class\": \"${currentBlock}\"\n"+
                            "      },\n" +
                            "      \"geometry\": {\n" +
                            "        \"type\": \"LineString\",\n" +
                            "        \"coordinates\": [\n" +
                            "          ${from.toGeoJSON()},\n" +
                            "          ${to.toGeoJSON()}\n" +
                            "        ]\n" +
                            "      }\n" +
                            "    },\n${next.toGeoJSON()}"
                }
            }
            else -> throw TypeException
        }
    }
}

//popravi bend
class Bend(private val from: Expr, private val to: Expr, private var angle: Expr, override var next: Stmt): Stmt {
    override fun toString(): String =
        "\tbend($from,$to, $angle)\n$next"

    override fun evalPartial(env: Map<String, Expr>): Stmt =
        Bend(from.evalPartial(env), to.evalPartial(env), angle.evalPartial(env), next.evalPartial(env))

    override fun toGeoJSON(): String {
        when {
            from is Point && to is Point && angle is Num -> {
                if (from.longitude == to.longitude && from.latitude == to.latitude) {
                    throw LineException()
                } else {
                    return "    {\n" +
                            "      \"type\": \"Feature\",\n" +
                            "      \"properties\": {\n" +
                            "        \"stroke-angle\": ${angle.toGeoJSON()},\n" +
                            "        \"stroke\": \"${blockColor[currentBlock]}\",\n"+
                            "        \"class\": \"${currentBlock}\"\n"+
                            "      },\n" +
                            "      \"geometry\": {\n" +
                            "        \"type\": \"LineString\",\n" +
                            "        \"coordinates\": [\n" +
                            "          ${from.toGeoJSON()},\n" +
                            "          ${to.toGeoJSON()}\n" +
                            "        ]\n" +
                            "      }\n" +
                            "    },\n${next.toGeoJSON()}"
                }
            }
            else -> throw TypeException
        }
    }
}

class Box(private val first: Expr, private val second: Expr, override var next: Stmt): Stmt {
    override fun toString(): String =
        "\tbox($first,$second)\n$next"

    override fun evalPartial(env: Map<String, Expr>): Stmt =
        Box(first.evalPartial(env), second.evalPartial(env), next.evalPartial(env))

    override fun toGeoJSON(): String {
        when {
            first is Point && second is Point -> {
                if(first.longitude == second.longitude || first.latitude == second.latitude){
                    throw BoxException()
                }else{
                    return  "    {\n" +
                            "      \"type\": \"Feature\",\n" +
                            "      \"properties\": {\n" +
                            "        \"fill\": \"${blockColor[currentBlock]}\",\n"+
                            "        \"class\": \"${currentBlock}\"\n"+
                            "      },\n" +
                            "      \"geometry\": {\n" +
                            "        \"type\": \"Polygon\",\n" +
                            "        \"coordinates\": [\n" +
                            "          [\n" +
                            "            [\n" +
                            "              ${first.longitude},\n" +
                            "              ${first.latitude}\n" +
                            "            ],\n" +
                            "            [\n" +
                            "              ${second.longitude},\n" +
                            "              ${first.latitude}\n" +
                            "            ],\n" +
                            "            [\n" +
                            "              ${second.longitude},\n" +
                            "              ${second.latitude}\n" +
                            "            ],\n" +
                            "            [\n" +
                            "              ${first.longitude},\n" +
                            "              ${second.latitude}\n" +
                            "            ],\n" +
                            "            [\n" +
                            "              ${first.longitude},\n" +
                            "              ${first.latitude}\n" +
                            "            ]\n" +
                            "          ]\n" +
                            "        ]\n" +
                            "      }\n" +
                            "    },\n${next.toGeoJSON()}"
                }
            }
            else -> throw TypeException
        }
    }
}

// Konstrukt, ki predstavlja dodelitev
class DefineVar(private var name: String, private var expr: Expr, override var next: Stmt): Stmt {

    override fun toString(): String =
        "\tvar $name = $expr\n$next"

    // Ustvarimo novo konstanto, pod ime shranimo vrednost izjave
    override fun evalPartial(env: Map<String, Expr>): Stmt {
        if(env.contains(name) || env.contains("-const$name")){
            throw AlreadyDefinedException(name)
        }
        return next.evalPartial(env + (name to expr.evalPartial(env)))
    }
}

class DefineConst(private var name: String, private var expr: Expr, override var next: Stmt): Stmt {

    override fun toString(): String =
        "\tconst $name = $expr\n$next"

    // Ustvarimo novo konstanto, pod ime shranimo vrednost izjave
    override fun evalPartial(env: Map<String, Expr>): Stmt {
        if(env.contains(name) || env.contains("-const$name")){
            throw AlreadyDefinedException(name)
        }
        return next.evalPartial(env + ("-const$name" to expr.evalPartial(env)))
    }
}

class Assignment(private val name: String, private val value: Expr, override var next: Stmt): Stmt{
    override fun toString(): String{
        return "\t$name=$value\n$next"
    }

    override fun evalPartial(env: Map<String, Expr>): Stmt {
        if(env.contains(name)){
            return next.evalPartial(env + (name to value.evalPartial(env)))
        }else if(env.contains("-const$name")){
            throw CantAssignException(name)
        }else {
            throw UndefinedException(name)
        }
    }
}

class RentBike(private val name: String, private val point: Point, override var next: Stmt): Stmt{
    override fun toString(): String{
        return "rentBike $name { $point }\n$next"
    }

    override fun evalPartial(env: Map<String, Expr>): Stmt {
        currentBlock="rentBike"
        return this
    }

    override fun toGeoJSON(): String {
        currentBlock="rentBike"
        return  "    {\n" +
                "      \"type\": \"Feature\",\n" +
                "      \"properties\": {\n" +
                "        \"marker-color\": \"${blockColor[currentBlock]}\",\n" +
                "        \"class\": \"${currentBlock}\"\n"+
                "      },\n" +
                "      \"geometry\": {\n" +
                "        \"type\": \"Point\",\n" +
                "        \"coordinates\": [\n" +
                "          ${point.longitude},\n" +
                "          ${point.latitude}\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n${next.toGeoJSON()}"
    }
}

class BikeShed(private val name: String, private val point: Point, override var next: Stmt): Stmt{
    override fun toString(): String{
        return "bikeShed $name { $point }\n$next"
    }

    override fun evalPartial(env: Map<String, Expr>): Stmt {
        currentBlock="bikeShed"
        return BikeShed(name, point.evalPartial(env) as Point, next.evalPartial(mutableMapOf()))
    }

    override fun toGeoJSON(): String {
        currentBlock="bikeShed"
        return  "    {\n" +
                "      \"type\": \"Feature\",\n" +
                "      \"properties\": {\n" +
                "        \"marker-color\": \"${blockColor[currentBlock]}\",\n" +
                "        \"class\": \"${currentBlock}\"\n"+
                "      },\n" +
                "      \"geometry\": {\n" +
                "        \"type\": \"Point\",\n" +
                "        \"coordinates\": [\n" +
                "          ${point.longitude},\n" +
                "          ${point.latitude}\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n${next.toGeoJSON()}"
    }
}

class MBajk(private val name: String, private val point: Point, override var next: Stmt): Stmt{
    override fun toString(): String{
        return "mBajk $name { $point }\n$next"
    }

    override fun evalPartial(env: Map<String, Expr>): Stmt {
        currentBlock="mBajk"
        return MBajk(name, point.evalPartial(env) as Point, next.evalPartial(mutableMapOf()))
    }

    override fun toGeoJSON(): String {
        currentBlock="mBajk"
        return  "    {\n" +
                "      \"type\": \"Feature\",\n" +
                "      \"properties\": {\n" +
                "        \"marker-color\": \"${blockColor[currentBlock]}\",\n" +
                "        \"class\": \"${currentBlock}\"\n"+
                "      },\n" +
                "      \"geometry\": {\n" +
                "        \"type\": \"Point\",\n" +
                "        \"coordinates\": [\n" +
                "          ${point.longitude},\n" +
                "          ${point.latitude}\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n${next.toGeoJSON()}"
    }
}

class BikeStand(private val name: String, private val integer: Expr, private val point: Expr, override var next: Stmt): Stmt{
    override fun toString(): String{
        return "bikeStand($integer) $name { $point }\n$next"
    }

    override fun evalPartial(env: Map<String, Expr>): Stmt {
        currentBlock="bikeStand"
        return BikeStand(name, integer.evalPartial(env), point.evalPartial(env), next.evalPartial(env))
    }

    override fun toGeoJSON(): String {
        currentBlock="bikeStand"
        when {
            point is Point && integer is Num -> {
                return "    {\n" +
                        "      \"type\": \"Feature\",\n" +
                        "      \"properties\": {\n" +
                        "        \"marker-color\": \"${blockColor[currentBlock]}\",\n" +
                        "        \"class\": \"${currentBlock}\",\n" +
                        "        \"capacity\": \"${integer.toString().substring(0, integer.toString().length - 2)}\"" +
                        "      },\n" +
                        "      \"geometry\": {\n" +
                        "        \"type\": \"Point\",\n" +
                        "        \"coordinates\": [\n" +
                        "          ${point.longitude},\n" +
                        "          ${point.latitude}\n" +
                        "        ]\n" +
                        "      }\n" +
                        "    },\n${next.toGeoJSON()}"
            }
            else -> throw TypeException
        }
    }
}

class Park(private val name: String, private val body: Stmt, private var closureEnv: Map<String, Expr> = emptyMap(), override var next: Stmt): Stmt{
    override fun toString(): String {
        return "park $name{\n$body\n}\n$next"
    }

    override fun evalPartial(env: Map<String, Expr>): Stmt {
        currentBlock="park"
        return Park(name, body.evalPartial(closureEnv), closureEnv, next.evalPartial(mutableMapOf()))
    }

    override fun toGeoJSON(): String {
        currentBlock="park"
        return body.toGeoJSON() + next.toGeoJSON()
    }
}

class River(private val name: String, private val body: Stmt, private var closureEnv: Map<String, Expr> = emptyMap(), override var next: Stmt): Stmt{
    override fun toString(): String {
        return "river $name{\n$body\n}\n$next"
    }

    override fun evalPartial(env: Map<String, Expr>): Stmt {
        currentBlock="river"
        return River(name, body.evalPartial(closureEnv), closureEnv, next.evalPartial(mutableMapOf()))
    }

    override fun toGeoJSON(): String {
        currentBlock="river"
        return body.toGeoJSON() + next.toGeoJSON()
    }
}

class Building(private val name: String, private val body: Stmt, private var closureEnv: Map<String, Expr> = emptyMap(), override var next: Stmt): Stmt{
    override fun toString(): String {
        return "building $name{\n$body\n}\n$next"
    }

    override fun evalPartial(env: Map<String, Expr>): Stmt {
        currentBlock="building"
        return Building(name, body.evalPartial(closureEnv), closureEnv, next.evalPartial(mutableMapOf()))
    }

    override fun toGeoJSON(): String {
        currentBlock="building"
        return body.toGeoJSON() + next.toGeoJSON()
    }
}

class Corridor(private val name: String, private val body: Stmt, private var closureEnv: Map<String, Expr> = emptyMap(), override var next: Stmt): Stmt{
    override fun toString(): String {
        return "corridor $name{\n$body\n}\n$next"
    }

    override fun evalPartial(env: Map<String, Expr>): Stmt {
        currentBlock="corridor"
        return Corridor(name, body.evalPartial(closureEnv), closureEnv, next.evalPartial(mutableMapOf()))
    }

    override fun toGeoJSON(): String {
        currentBlock="corridor"
        return body.toGeoJSON() + next.toGeoJSON()
    }
}

class TourPath(private val name: String, private val body: Stmt, private var closureEnv: Map<String, Expr> = emptyMap(), override var next: Stmt): Stmt{

    override fun toString(): String {
        return "bikeTourPath $name{\n$body\n}\n$next"
    }

    override fun evalPartial(env: Map<String, Expr>): Stmt {
        currentBlock="tourPath"
        return TourPath(name, body.evalPartial(closureEnv), closureEnv, next.evalPartial(mutableMapOf()))
    }

    override fun toGeoJSON(): String {
        currentBlock="tourPath"
        return body.toGeoJSON() + next.toGeoJSON()
    }
}

class BikePath(private val name: String, private val body: Stmt, private var closureEnv: Map<String, Expr> = emptyMap(), override var next: Stmt): Stmt{
    override fun toString(): String {
        return "bikePath $name{\n$body\n}\n$next"
    }

    override fun evalPartial(env: Map<String, Expr>): Stmt {
        currentBlock="bikePath"
        return BikePath(name, body.evalPartial(closureEnv), closureEnv, next.evalPartial(mutableMapOf()))
    }

    override fun toGeoJSON(): String {
        currentBlock="bikePath"
        return body.toGeoJSON() + next.toGeoJSON()
    }
}

class Road(private val name: String, private val body: Stmt, private var closureEnv: Map<String, Expr> = emptyMap(), override var next: Stmt): Stmt{
    override fun toString(): String {
        return "\troad $name{\n$body\n}\n$next"
    }

    override fun evalPartial(env: Map<String, Expr>): Stmt {
        currentBlock="road"
        return Road(name, body.evalPartial(closureEnv), closureEnv, next.evalPartial(mutableMapOf()))
    }

    override fun toGeoJSON(): String {
        currentBlock="road"
        return body.toGeoJSON() + next.toGeoJSON()
    }
}

class City(private val name: String, private val body: Stmt, override var next: Stmt=End): Stmt {
    override fun toString(): String{
        return "city $name {\n$body\n}"
    }

    override fun evalPartial(env: Map<String, Expr>): Stmt {
        return City(name, body.evalPartial(mutableMapOf()))
    }

    override fun toGeoJSON(): String {
        return  "{\n" +
                "  \"type\": \"FeatureCollection\",\n" +
                "  \"features\": [\n" +
                "   ${body.toGeoJSON().dropLast(2)}\n" +
                "" +
                "  ]\n" +
                "}"
    }
}

class Return(val value: Expr, override var next: Stmt): Stmt{
    override fun toString(): String{
        return "return $value\n"
    }

    override fun evalPartial(env: Map<String, Expr>): Stmt {
        return Return(value.evalPartial(env), next)
    }
}

class For(private val spr1: Expr, private val spr2: Expr, private val body: Stmt, override var next: Stmt): Stmt{
    override fun toString(): String{
        return "for ($spr1 to $spr2){\n$body\n}\n$next\n"
    }

    override fun evalPartial(env: Map<String, Expr>): Stmt {
        return For(spr1.evalPartial(env), spr2.evalPartial(env), body.evalPartial(env), next.evalPartial(env))
    }

    override fun toGeoJSON(): String {
        when {
            spr1 is Num && spr2 is Num -> {
                var returnString=""
                while(spr1.value < spr2.value){
                    returnString+=body.toGeoJSON()
                    spr1.value++
                }
                returnString+=next.toGeoJSON()
                return returnString
            }
            else -> throw TypeException
        }
    }
}

class If(private val spr1: Expr, private val spr2: Expr, private val operator: String, private val body: Stmt, override var next: Stmt): Stmt{
    override fun toString(): String{
        return "if ($spr1 $operator $spr2){\n$body\n}\n$next"
    }

    override fun evalPartial(env: Map<String, Expr>): Stmt {
        return If(spr1.evalPartial(env), spr2.evalPartial(env), operator, body.evalPartial(env), next.evalPartial(env))
    }

    override fun toGeoJSON(): String {
        when {
            spr1 is Num && spr2 is Num -> {
                when(operator){
                    "<"->{
                        return if(spr1.value < spr2.value){
                            if(next is ElseIf){
                                (next as ElseIf).setSkip()
                            }
                            if(next is Else){
                                (next as Else).setSkip()
                            }
                            body.toGeoJSON() + next.toGeoJSON()
                        }else{
                            next.toGeoJSON()
                        }
                    }
                    ">"->{
                        return if(spr1.value > spr2.value){
                            if(next is ElseIf){
                                (next as ElseIf).setSkip()
                            }
                            if(next is Else){
                                (next as Else).setSkip()
                            }
                            body.toGeoJSON() + next.toGeoJSON()
                        }else{
                            next.toGeoJSON()
                        }
                    }
                    "=="->{
                        return if(spr1.value == spr2.value){
                            if(next is ElseIf){
                                (next as ElseIf).setSkip()
                            }
                            if(next is Else){
                                (next as Else).setSkip()
                            }
                            body.toGeoJSON() + next.toGeoJSON()
                        }else{
                            next.toGeoJSON()
                        }
                    }
                    "<="->{
                        return if(spr1.value <= spr2.value){
                            if(next is ElseIf){
                                (next as ElseIf).setSkip()
                            }
                            if(next is Else){
                                (next as Else).setSkip()
                            }
                            body.toGeoJSON() + next.toGeoJSON()
                        }else{
                            next.toGeoJSON()
                        }
                    }
                    "=>"->{
                        return if(spr1.value >= spr2.value){
                            if(next is ElseIf){
                                (next as ElseIf).setSkip()
                            }
                            if(next is Else){
                                (next as Else).setSkip()
                            }
                            body.toGeoJSON() + next.toGeoJSON()
                        }else{
                            next.toGeoJSON()
                        }
                    }
                    "!="->{
                        return if(spr1.value != spr2.value){
                            if(next is ElseIf){
                                (next as ElseIf).setSkip()
                            }
                            if(next is Else){
                                (next as Else).setSkip()
                            }
                            body.toGeoJSON() + next.toGeoJSON()
                        }else{
                            next.toGeoJSON()
                        }
                    }
                    else->throw TypeException
                }
            }
            else -> throw TypeException
        }
    }
}

class ElseIf(private val spr1: Expr, private val spr2: Expr, private val operator: String, private val body: Stmt, override var next: Stmt, private var skip: Boolean=false): Stmt{
    fun setSkip(){
        skip=true
    }

    override fun toString(): String{
        return "elseif ($spr1 $operator $spr2){\n$body\n}\n$next"
    }

    override fun evalPartial(env: Map<String, Expr>): Stmt {
        return ElseIf(spr1.evalPartial(env), spr2.evalPartial(env), operator, body.evalPartial(env), next.evalPartial(env), skip)
    }

    override fun toGeoJSON(): String {
        if (skip) {
            if(next is ElseIf){
                (next as ElseIf).setSkip()
            }
            if(next is Else){
                (next as Else).setSkip()
            }
            return next.toGeoJSON()
        }
        when {
            spr1 is Num && spr2 is Num -> {
                when(operator){
                    "<"->{
                        return if(spr1.value < spr2.value){
                            if(next is ElseIf){
                                (next as ElseIf).setSkip()
                            }
                            if(next is Else){
                                (next as Else).setSkip()
                            }
                            body.toGeoJSON() + next.toGeoJSON()
                        }else{
                            next.toGeoJSON()
                        }
                    }
                    ">"->{
                        return if(spr1.value > spr2.value){
                            if(next is ElseIf){
                                (next as ElseIf).setSkip()
                            }
                            if(next is Else){
                                (next as Else).setSkip()
                            }
                            body.toGeoJSON() + next.toGeoJSON()
                        }else{
                            next.toGeoJSON()
                        }
                    }
                    "=="->{
                        return if(spr1.value == spr2.value){
                            if(next is ElseIf){
                                (next as ElseIf).setSkip()
                            }
                            if(next is Else){
                                (next as Else).setSkip()
                            }
                            body.toGeoJSON() + next.toGeoJSON()
                        }else{
                            next.toGeoJSON()
                        }
                    }
                    "<="->{
                        return if(spr1.value <= spr2.value){
                            if(next is ElseIf){
                                (next as ElseIf).setSkip()
                            }
                            if(next is Else){
                                (next as Else).setSkip()
                            }
                            body.toGeoJSON() + next.toGeoJSON()
                        }else{
                            next.toGeoJSON()
                        }
                    }
                    "=>"->{
                        return if(spr1.value >= spr2.value){
                            if(next is ElseIf){
                                (next as ElseIf).setSkip()
                            }
                            if(next is Else){
                                (next as Else).setSkip()
                            }
                            body.toGeoJSON() + next.toGeoJSON()
                        }else{
                            next.toGeoJSON()
                        }
                    }
                    "!="->{
                        return if(spr1.value != spr2.value){
                            if(next is ElseIf){
                                (next as ElseIf).setSkip()
                            }
                            if(next is Else){
                                (next as Else).setSkip()
                            }
                            body.toGeoJSON() + next.toGeoJSON()
                        }else{
                            next.toGeoJSON()
                        }
                    }
                    else->throw TypeException
                }
            }
            else -> throw TypeException
        }
    }
}

class Else(private val body: Stmt, override var next: Stmt, private var skip: Boolean=false): Stmt{
    fun setSkip(){
        skip=true
    }

    override fun toString(): String{
        return "else{\n$body\n}\n$next"
    }

    override fun evalPartial(env: Map<String, Expr>): Stmt {
        return Else(body.evalPartial(env), next.evalPartial(env), skip)
    }

    override fun toGeoJSON(): String {
        return if (skip) {
            next.toGeoJSON()
        } else {
            body.toGeoJSON() + next.toGeoJSON()
        }
    }
}

// Konstrukt, ki predstavlja nevtralen element (ne dela nič)
object End: Stmt {
    override fun toString(): String =
        "end"

    override var next:Stmt=this

    override fun evalPartial(env: Map<String, Expr>): Stmt =
        this

    override fun toGeoJSON(): String =
        ""
}

// Konstrukt, ki predstavlja null element (ne dela nič)
object Null: Stmt {
    override fun toString(): String =
        "null"

    override var next:Stmt=this

    override fun evalPartial(env: Map<String, Expr>): Stmt =
        this
}

class ParseException(row: Int, column: Int) : Exception("Error at $row : $column")
object TypeException: Exception("TYPE ERROR")
class AlreadyDefinedException(name: String): Exception("Variable $name is already defined")
class CantAssignException(name: String): Exception("Constant variable $name can't be assigned to another value")
class UndefinedException(name: String): Exception("Variable '$name' is undefined")
class BoxException: Exception("Box vertices must have different longitudes and latitudes")
class LineException: Exception("Line/Bend points must be different")

class TaskParser(private val scanner: Scanner) {
    private var last: Token? = null

    private fun error(): Nothing{
        throw ParseException(last!!.startRow , last!!.startColumn)
    }

    fun parse(): Stmt {
        last = scanner.getToken()
        val result = parsePROGRAM()
        return if (last!!.value == EOF_VALUE) result
        else error()
    }

    private fun parsePROGRAM(): Stmt {
        val functionsEnv: MutableMap<String, Function> = parseFUNCTIONS(mutableMapOf())
        return parseCITY(functionsEnv)
    }

        private fun parseFUNCTIONS(functionsEnv: MutableMap<String, Function>): MutableMap<String, Function> {
            return when (last!!.value) {
                FUNC -> {
                    val function: Pair<String, Function> = parseFUNCTION(functionsEnv)
                    functionsEnv[function.first] = function.second
                    parseFUNCTIONS(functionsEnv)
                }
                CITY -> {
                    functionsEnv
                }
                else -> error()
            }
        }

        private fun parseCITY(functionsEnv: MutableMap<String, Function>) : Stmt{
            parseTerminal(CITY)
            val cityName=parseTerminal(IDENTIFIER)
            parseTerminal(LB_PAREN)
            val body = parseBLOCKS(functionsEnv)
            parseTerminal(RB_PAREN)
            return City(cityName, body)
        }

        private fun parseBLOCKS(functionEnv: MutableMap<String, Function>): Stmt{
            val block = parseBLOCK(functionEnv)
            return parseMOREBLOCKS(block, block, functionEnv)
        }

        private fun parseMOREBLOCKS(firstBlock: Stmt, lastBlock: Stmt, functionEnv: MutableMap<String, Function>):  Stmt =
            when (last!!.value) {
                ROAD, BIKE_PATH, BIKE_TOUR_PATH, BIKE_CORRIDOR, BUILDING, RIVER, PARK, BIKE_STAND, BIKE_SHED, M_BAJK, RENT_BIKE -> {
                    val nextBlock = parseBLOCK(functionEnv)
                    lastBlock.next = nextBlock
                    parseMOREBLOCKS(firstBlock, nextBlock, functionEnv)
                }
                RB_PAREN -> {
                    lastBlock.next=End
                    firstBlock
                }
                else -> error()
            }

        private fun parseBLOCK(functionEnv: MutableMap<String, Function>): Stmt =
            when (last!!.value) {
                ROAD -> parseROAD(functionEnv)
                BIKE_PATH -> parseBIKEPATH(functionEnv)
                BIKE_TOUR_PATH -> parseTOURPATH(functionEnv)
                BIKE_CORRIDOR -> parseCORRIDOR(functionEnv)
                BUILDING -> parseBUILDING(functionEnv)
                RIVER -> parseRIVER(functionEnv)
                PARK -> parsePARK(functionEnv)
                BIKE_STAND, BIKE_SHED, M_BAJK, RENT_BIKE -> parseMARKPOINT()
                else -> error()
            }

        private fun parseROAD(functionEnv: MutableMap<String, Function>): Road {
            parseTerminal(ROAD)
            val roadName=parseTerminal(IDENTIFIER)
            parseTerminal(LB_PAREN)
            val body = parseCOMMANDS(functionEnv)
            parseTerminal(RB_PAREN)
            return Road(roadName, body, mapOf(), End)
        }

        private fun parseBIKEPATH(functionEnv: MutableMap<String, Function>) : BikePath {
            parseTerminal(BIKE_PATH)
            val bikePathName=parseTerminal(IDENTIFIER)
            parseTerminal(LB_PAREN)
            val body = parseCOMMANDS(functionEnv)
            parseTerminal(RB_PAREN)
            return BikePath(bikePathName, body, mutableMapOf(), End)
        }

        private fun parseTOURPATH(functionEnv: MutableMap<String, Function>): TourPath {
            parseTerminal(BIKE_TOUR_PATH)
            val tourPathName=parseTerminal(IDENTIFIER)
            parseTerminal(LB_PAREN)
            val body=parseCOMMANDS(functionEnv)
            parseTerminal(RB_PAREN)
            return TourPath(tourPathName, body, mutableMapOf(), End)
        }

        private fun parseCORRIDOR(functionEnv: MutableMap<String, Function>): Corridor {
            parseTerminal(BIKE_CORRIDOR)
            val corridorName=parseTerminal(IDENTIFIER)
            parseTerminal(LB_PAREN)
            val body=parseCOMMANDS(functionEnv)
            parseTerminal(RB_PAREN)
            return Corridor(corridorName, body, mutableMapOf(), End)
        }

        private fun parseBUILDING(functionEnv: MutableMap<String, Function>): Building {
            parseTerminal(BUILDING)
            val buildingName=parseTerminal(IDENTIFIER)
            parseTerminal(LB_PAREN)
            val body=parseCOMMANDS(functionEnv)
            parseTerminal(RB_PAREN)
            return Building(buildingName, body, mutableMapOf(), End)
        }

        private fun parseRIVER(functionEnv: MutableMap<String, Function>): River {
            parseTerminal(RIVER)
            val riverName=parseTerminal(IDENTIFIER)
            parseTerminal(LB_PAREN)
            val body=parseCOMMANDS(functionEnv)
            parseTerminal(RB_PAREN)
            return River(riverName, body, mutableMapOf(), End)
        }

        private fun parsePARK(functionEnv: MutableMap<String, Function>): Park {
            parseTerminal(PARK)
            val parkName=parseTerminal(IDENTIFIER)
            parseTerminal(LB_PAREN)
            val body=parseCOMMANDS(functionEnv)
            parseTerminal(RB_PAREN)
            return Park(parkName, body, mutableMapOf(), End)
        }

        private fun parseMARKPOINT(): Stmt =
            when (last!!.value) {
                BIKE_STAND -> parseBIKESTAND()
                BIKE_SHED -> parseBIKESHED()
                M_BAJK -> parseMBAJK()
                RENT_BIKE -> parseRENTBIKE()
                else -> error()
            }

        private fun parseBIKESTAND(): BikeStand {
            parseTerminal(BIKE_STAND)
            parseTerminal(L_PAREN)
            val capacity=parseInt()
            parseTerminal(R_PAREN)
            val bikeStandName=parseTerminal(IDENTIFIER)
            parseTerminal(LB_PAREN)
            val point=parsePOINT()
            parseTerminal(RB_PAREN)
            return BikeStand(bikeStandName, capacity, point, End)
        }

        private fun parseBIKESHED(): BikeShed {
            parseTerminal(BIKE_SHED)
            val bikeShedName=parseTerminal(IDENTIFIER)
            parseTerminal(LB_PAREN)
            val point=parsePOINT()
            parseTerminal(RB_PAREN)
            return BikeShed(bikeShedName, point, End)
        }

        private fun parseMBAJK(): MBajk {
            parseTerminal(M_BAJK)
            val mBajkName=parseTerminal(IDENTIFIER)
            parseTerminal(LB_PAREN)
            val point=parsePOINT()
            parseTerminal(RB_PAREN)
            return MBajk(mBajkName, point, End)
        }

        private fun parseRENTBIKE(): RentBike {
            parseTerminal(RENT_BIKE)
            val rentBikeName=parseTerminal(IDENTIFIER)
            parseTerminal(LB_PAREN)
            val point=parsePOINT()
            parseTerminal(RB_PAREN)
            return RentBike(rentBikeName, point, End)
        }

        private fun parseCOMMANDS(functionEnv: MutableMap<String, Function>): Stmt{
            val command=parseCOMMAND(functionEnv)
            return parseMORECOMMANDS(command, command, functionEnv)
        }

        private fun parseMORECOMMANDS(fullCommand: Stmt, lastCommand: Stmt, functionEnv: MutableMap<String, Function>): Stmt {
            when (last!!.value) {
                LINE, BEND, BOX, FOR, IF, VAR, CONST, IDENTIFIER -> {
                    val nextCommand = parseCOMMAND(functionEnv)
                    return if (lastCommand is If) {
                        var command = lastCommand
                        while (command.next != End && command.next != Null) {
                            command = command.next
                        }
                        command.next = nextCommand
                        parseMORECOMMANDS(fullCommand, nextCommand, functionEnv)
                    }else {
                        lastCommand.next = nextCommand
                        parseMORECOMMANDS(fullCommand, nextCommand, functionEnv)
                    }
                }
                RB_PAREN, RETURN -> {
                    lastCommand.next = End
                    return fullCommand
                }
                else -> error()
            }
        }

        private fun parseCOMMAND(functionEnv: MutableMap<String, Function>): Stmt =
            when (last!!.value) {
                LINE -> parseLINE()
                BEND -> parseBEND()
                BOX -> parseBOX()
                FOR -> parseFOR(functionEnv)
                IF -> parseIF(functionEnv)
                VAR -> parseVARIABLE(functionEnv)
                CONST -> parseCONST(functionEnv)
                IDENTIFIER -> parseASSIGNMENT()
                else -> error()
            }

        private fun parseLINE(): Line {
            parseTerminal(LINE)
            parseTerminal(L_PAREN)
            val point1=parsePOINTORVAR()
            parseTerminal(COMMA)
            val point2=parsePOINTORVAR()
            parseTerminal(COMMA)
            val width=parseWIDTH()
            parseTerminal(R_PAREN)
            return Line(point1, point2, width, Null)
        }

        private fun parseBEND(): Bend {
            parseTerminal(BEND)
            parseTerminal(L_PAREN)
            val point1=parsePOINTORVAR()
            parseTerminal(COMMA)
            val point2=parsePOINTORVAR()
            parseTerminal(COMMA)
            val angle=parseANGLE()
            parseTerminal(R_PAREN)
            return Bend(point1, point2, angle, Null)
        }

        private fun parseANGLE(): Expr =
            when (last!!.value) {
                INT -> parseInt()
                IDENTIFIER -> parseVar()
                else -> error()
            }

        private fun parseWIDTH(): Expr =
            when (last!!.value) {
                INT -> parseInt()
                IDENTIFIER -> parseVar()
                else -> error()
            }

        private fun parseBOX(): Box {
            parseTerminal(BOX)
            parseTerminal(L_PAREN)
            val point1=parsePOINTORVAR()
            parseTerminal(COMMA)
            val point2=parsePOINTORVAR()
            parseTerminal(R_PAREN)
            return Box(point1, point2, Null)
        }

        private fun parsePOINTORVAR(): Expr =
            when (last!!.value) {
                L_PAREN -> parsePOINT()
                IDENTIFIER -> parseVar()
                else -> error()
            }

        private fun parseFOR(functionEnv: MutableMap<String, Function>): For {
            parseTerminal(FOR)
            parseTerminal(L_PAREN)
            val spr1=parseSPR()
            parseTerminal(TO)
            val spr2=parseSPR()
            parseTerminal(R_PAREN)
            parseTerminal(LB_PAREN)
            val body=parseCOMMANDS(functionEnv)
            parseTerminal(RB_PAREN)
            return For(spr1, spr2, body, Null)
        }

        private fun parseIF(functionEnv: MutableMap<String, Function>): If {
            parseTerminal(IF)
            parseTerminal(L_PAREN)
            val spr1=parseSPR()
            val operator=parseOPERATOR()
            val spr2=parseSPR()
            parseTerminal(R_PAREN)
            parseTerminal(LB_PAREN)
            val body=parseCOMMANDS(functionEnv)
            parseTerminal(RB_PAREN)
            val next=parseELSEORELSEIFSELSE(functionEnv)
            return If(spr1, spr2, operator, body, next)
        }

        private fun parseELSEORELSEIFSELSE(functionEnv: MutableMap<String, Function>): Stmt =
            when (last!!.value) {
                ELSE -> parseELSE(functionEnv)
                ELSE_IF -> parseELSEIFSELSE(functionEnv)
                LINE, BEND, BOX, FOR, IF, VAR, CONST, IDENTIFIER, RB_PAREN, RETURN -> End
                else -> error()
            }

        private fun parseELSEIFSELSE(functionEnv: MutableMap<String, Function>): Stmt {
            val elseifs=parseELSEIFS(functionEnv)
            var itElseIfs=elseifs
            while(itElseIfs.next!=End && itElseIfs.next!=Null){
                itElseIfs=itElseIfs.next
            }
            val elsee=parseELSE(functionEnv)
            itElseIfs.next=elsee
            return elseifs
        }

        private fun parseELSEIFS(functionEnv: MutableMap<String, Function>): Stmt {
            val elseif=parseELSEIF(functionEnv)
            val moreEleseIfs=parseMOREELSEIFS(functionEnv)
            elseif.next = moreEleseIfs
            return elseif
        }

        private fun parseMOREELSEIFS(functionEnv: MutableMap<String, Function>): Stmt {
            return when (last!!.value) {
                ELSE_IF -> {
                    val elseif = parseELSEIF(functionEnv)
                    val moreElseIfs = parseMOREELSEIFS(functionEnv)
                    elseif.next = moreElseIfs
                    elseif
                }
                ELSE -> {
                    End
                }
                else -> error()
            }
        }

        private fun parseELSEIF(functionEnv: MutableMap<String, Function>): ElseIf {
            parseTerminal(ELSE_IF)
            parseTerminal(L_PAREN)
            val spr1=parseSPR()
            val operator=parseOPERATOR()
            val spr2=parseSPR()
            parseTerminal(R_PAREN)
            parseTerminal(LB_PAREN)
            val body=parseCOMMANDS(functionEnv)
            parseTerminal(RB_PAREN)
            return ElseIf(spr1, spr2, operator, body, End)
        }

        private fun parseELSE(functionEnv: MutableMap<String, Function>): Else {
            parseTerminal(ELSE)
            parseTerminal(LB_PAREN)
            val body=parseCOMMANDS(functionEnv)
            parseTerminal(RB_PAREN)
            return Else(body, End)
        }

        private fun parseOPERATOR(): String =
            when (last!!.value) {
                LESS -> parseTerminal(LESS)
                LESS_OR_EQUAL -> parseTerminal(LESS_OR_EQUAL)
                MORE_OR_EQUAL -> parseTerminal(MORE_OR_EQUAL)
                MORE -> parseTerminal(MORE)
                EQUAL -> parseTerminal(EQUAL)
                NOT_EQUAL -> parseTerminal(NOT_EQUAL)
                else -> error()
            }

        private fun parseSPR(): Expr =
            when (last!!.value) {
                FLOAT -> parseFloat()
                INT -> parseInt()
                IDENTIFIER -> parseVar()
                else -> error()
            }

        private fun parsePOINT(): Point {
            parseTerminal(L_PAREN)
            val longitude = parseEXPR()
            parseTerminal(COMMA)
            val latitude = parseEXPR()
            parseTerminal(R_PAREN)
            return Point(longitude, latitude)
        }

        private fun parseVARIABLE(functionEnv: MutableMap<String, Function>): DefineVar {
            parseTerminal(VAR)
            val name=parseTerminal(IDENTIFIER)
            parseTerminal(ASSIGN)
            val value=parseVALUE(functionEnv)
            return DefineVar(name, value, Null)
        }

        private fun parseCONST(functionEnv: MutableMap<String, Function>): DefineConst {
            parseTerminal(CONST)
            val name=parseTerminal(IDENTIFIER)
            parseTerminal(ASSIGN)
            val value=parseVALUE(functionEnv)
            return DefineConst(name, value, Null)
        }

        private fun parseASSIGNMENT(): Assignment {
            val name=parseTerminal(IDENTIFIER)
            parseTerminal(ASSIGN)
            val value=parseEXPR()
            return Assignment(name, value, Null)
        }

        private fun parseVALUE(functionEnv: MutableMap<String, Function>): Expr =
            when (last!!.value) {
                FLOAT -> parseFloat()
                INT -> parseInt()
                IDENTIFIER -> parseVar()
                L_PAREN -> parsePOINT()
                CALL -> parseCALL(functionEnv)
                else -> error()
            }

        private fun parseCALL(functionEnv: MutableMap<String, Function>): Expr{
            parseTerminal(CALL)
            val funcName=parseTerminal(IDENTIFIER)
            parseTerminal(L_PAREN)
            val callArgs: MutableList<Expr> = parseCALLARGS()
            parseTerminal(R_PAREN)
            if(functionEnv.contains(funcName)){
                val function=functionEnv[funcName]
                return Call(function!!, callArgs)
            }else{
                throw Exception("Function with name: $funcName doesn't exist")
            }
        }

        private fun parseCALLARGS(): MutableList<Expr> {
            var list = mutableListOf<Expr>()
            list.add(parseVARIABLENAMEORNUMBER())
            list = parseMOREVORN(list)
            return list
        }

        private fun parseMOREVORN(list: MutableList<Expr>): MutableList<Expr> {
            return when (last!!.value) {
                COMMA -> {
                    parseTerminal(COMMA)
                    list.add(parseVARIABLENAMEORNUMBER())
                    parseMOREVORN(list)
                }
                R_PAREN -> list
                else -> error()
            }
        }

        private fun parseVARIABLENAMEORNUMBER(): Expr =
            when (last!!.value) {
                FLOAT -> parseFloat()
                INT -> parseInt()
                IDENTIFIER -> parseVar()
                else -> error()
            }

        private fun parseFUNCTION(functionEnv: MutableMap<String, Function>): Pair<String, Function> {
            parseTerminal(FUNC)
            val functionName: String = parseTerminal(IDENTIFIER)
            parseTerminal(L_PAREN)
            val params: MutableList<String> = parsePARAMS(mutableListOf())
            parseTerminal(R_PAREN)
            parseTerminal(LB_PAREN)
            val body = parseCOMMANDS(functionEnv)
            var itBody=body
            while(itBody.next!=End && itBody.next!=Null){
                itBody=itBody.next
            }
            val returnSt = parseRETURN()
            itBody.next=returnSt
            parseTerminal(RB_PAREN)
            val function = Function(params, body, emptyMap(), End)
            return Pair(functionName, function)
        }

        private fun parsePARAMS(params: MutableList<String>): MutableList<String> {
            return when (last!!.value) {
                IDENTIFIER -> {
                    params.add(parseTerminal(IDENTIFIER))
                    parseMOREPARAMS(params)
                }
                R_PAREN -> {
                    params
                }
                else -> error()
            }
        }

        private fun parseMOREPARAMS(params: MutableList<String>): MutableList<String> {
            return when (last!!.value) {
                COMMA -> {
                    parseTerminal(COMMA)
                    params.add(parseTerminal(IDENTIFIER))
                    parseMOREPARAMS(params)
                }
                R_PAREN -> {
                    params
                }
                else -> error()
            }
        }

        private fun parseRETURN(): Return {
            parseTerminal(RETURN)
            val returnValue=parseRETURNVALUE()
            return Return(returnValue, End)
        }

        private fun parseRETURNVALUE(): Expr {
            return when (last!!.value) {
                L_PAREN -> {
                    parsePOINT()
                }
                FLOAT -> {
                    parseFloat()
                }
                IDENTIFIER -> {
                    parseVar()
                }
                INT -> {
                    parseInt()
                }
                else -> error()
            }
        }

        private fun parseEXPR(): Expr = parseE()

        private fun parseE(): Expr {
            val value = parseT()
            return parseEE(value)
        }

        private fun parseEE(accumulator: Expr): Expr {
            when (last!!.value) {
                PLUS -> {
                    parseTerminal(PLUS)
                    val value = parseT()
                    return parseEE(Add(accumulator, value))
                }
                MINUS -> {
                    parseTerminal(MINUS)
                    val value = parseT()
                    return parseEE(Sub(accumulator, value))
                }
                R_PAREN, COMMA, LINE, BEND, BOX, FOR, IF, VAR, CONST, IDENTIFIER, RB_PAREN, RETURN -> {
                    return accumulator
                }
                else -> error()
            }
        }

        private fun parseT(): Expr{
            val value = parseX()
            return parseTT(value)
        }

        private fun parseTT(accumulator: Expr): Expr =
            when (last!!.value) {
                TIMES -> {
                    parseTerminal(TIMES)
                    val value = parseX()
                    parseTT(Mul(accumulator, value))
                }
                DIVIDE -> {
                    parseTerminal(DIVIDE)
                    val value = parseX()
                    parseTT(Div(accumulator, value))
                }
                R_PAREN, COMMA, PLUS, MINUS, LINE, BEND, BOX, FOR, IF, VAR, CONST, IDENTIFIER, RB_PAREN, RETURN -> accumulator
                else -> error()
            }

        private fun parseX(): Expr{
            val value = parseY()
            return parseXX(value)
        }

        private fun parseXX(accumulator: Expr): Expr {
            return when (last!!.value) {
                POW -> {
                    parseTerminal(POW)
                    val value = parseX()
                    Pow(accumulator, value)
                }
                R_PAREN, COMMA, PLUS, MINUS, TIMES, DIVIDE, LINE, BEND, BOX, FOR, IF, VAR, CONST, IDENTIFIER, RB_PAREN, RETURN -> {
                    accumulator
                }
                else -> {
                    error()
                }
            }
        }

        private fun parseY(): Expr {
            when (last!!.value) {
                MINUS -> {
                    parseTerminal(MINUS)
                    val value = parseF()
                    return UnaryMinus(value)
                }
                PLUS -> {
                    parseTerminal(PLUS)
                    val value = parseF()
                    return UnaryPlus(value)
                }
                L_PAREN, FLOAT, INT, IDENTIFIER, LINE, BEND, BOX, FOR, IF, VAR, CONST, RB_PAREN, RETURN -> {
                    return parseF()
                }
                else -> {
                    error()
                }
            }
        }

        private fun parseF(): Expr {
            when (last!!.value) {
                L_PAREN -> {
                    parseTerminal(L_PAREN)
                    val result = parseE()
                    parseTerminal(R_PAREN)
                    return result
                }
                FLOAT -> {
                    return parseFloat()
                }
                IDENTIFIER -> {
                    return parseVar()
                }
                INT -> {
                    return parseInt()
                }
                else -> error()
            }
        }

        private fun parseTerminal(value: Int): String =
            if (last?.value == value) {
                val lexeme = last!!.lexeme
                last = scanner.getToken()
                lexeme
            }
            else error()

        private fun parseFloat() = Num(parseTerminal(FLOAT).toDouble())

        private fun parseVar() = Var(parseTerminal(IDENTIFIER))

        private fun parseInt() = Num(parseTerminal(INT).toDouble())
}

fun main(args: Array<String>) {
    val program = TaskParser(StreamScanner(Example, File(args[0]).inputStream())).parse()
    println(program.evalPartial(mutableMapOf()).toGeoJSON())
}