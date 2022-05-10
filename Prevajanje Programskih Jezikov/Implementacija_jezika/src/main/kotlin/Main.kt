import java.io.InputStream
import java.util.*

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
const val VAR =46

const val NEWLINE = '\n'.code

val RESERVED_WORDS = mapOf("bend" to BEND, "bikeCoridor" to BIKE_CORRIDOR, "bikePath" to BIKE_PATH, "bikeShed" to BIKE_SHED,
    "bikeStand" to BIKE_STAND, "bikeTourPath" to BIKE_TOUR_PATH, "box" to BOX, "building" to BUILDING, "call" to CALL,
    "const" to CONST, "circ" to CIRC, "city" to CITY, "else" to ELSE, "elseif" to ELSE_IF, "for" to FOR, "func" to FUNC,
    "if" to IF, "line" to LINE, "mBajk" to M_BAJK, "park" to PARK, "rentBike" to RENT_BIKE, "return" to RETURN,
    "river" to RIVER, "road" to ROAD, "to" to TO, "var" to VAR)

interface Automaton {
    val states: Set<Int>
    val alphabet: IntRange
    fun next(state: Int, symbol: Int): Int
    fun value(state: Int): Int
    val startState: Int
    val finalStates: Set<Int>
}

object Example : Automaton {
    override val states = setOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28)
    override val alphabet = 0 .. 255
    override val startState = 1
    override val finalStates = setOf(1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 19, 20, 21, 22, 23, 24, 26)

    private val numberOfStates = states.maxOrNull()!! + 1
    private val numberOfSymbols = alphabet.maxOrNull()!! + 1
    private val transitions = Array(numberOfStates) {IntArray(numberOfSymbols)}
    private val values: Array<Int> = Array(numberOfStates) {0}

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
        for (symbol in '0' .. '9') {
            setTransition(1, symbol, 2)
            setTransition(2, symbol, 2)
        }
        setValue(2, INT)

        setTransition(2, '.', 3)
        for (symbol in '0' .. '9') {
            setTransition(3, symbol, 4)
            setTransition(4, symbol, 4)
        }
        setValue(4, FLOAT)

        for (symbol in 'A' .. 'Z') {
            setTransition(1, symbol, 5)
            setTransition(5, symbol, 5)
        }
        for (symbol in 'a' .. 'z') {
            setTransition(1, symbol, 5)
            setTransition(5, symbol, 5)
        }
        for (symbol in '0' .. '9') {
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
        for(symbol in alphabet){
            setTransition(25, symbol.toChar(), 25)
        }
        setTransition(25, '\n', 26)
        setValue(26, SKIP_VALUE)
        setTransition(24, '*', 27)
        for(symbol in alphabet){
            setTransition(27, symbol.toChar(), 27)
        }
        setTransition(27, '*', 28)
        for(symbol in alphabet){
            setTransition(28, symbol.toChar(), 27)
        }
        setTransition(28, '/', 26)
        setTransition(1, '!', 18)
        setTransition(18, '=', 19)
        setTransition(1, ' ', 26)
        setTransition(1, '\n', 26)
        setTransition(1, '\t', 26)
        setTransition(1, '\r', 26)
        setValue(19, NOT_EQUAL)
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

    private fun getValue(): Int {
        var symbol = last ?: stream.read()
        state = automaton.startState

        while (true) {
            updatePosition(symbol)

            val nextState = automaton.next(state, symbol)
            if (nextState == ERROR_STATE) {
                if (automaton.finalStates.contains(state)) {
                    last = symbol
                    if(automaton.value(state) == IDENTIFIER){
                        val reservedWord = RESERVED_WORDS[String(buffer.toByteArray())]
                        if(reservedWord != null){
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

fun main(args: Array<String>) {
    val scanner = StreamScanner(Example, "bend , bikeCoridor, bikeShed, bikeStand, bikeTourPath, box, building, call, const, circ, city, else, elseif, for, func, if, line, mBajk, park, rentBike, return, river, road, to, var".byteInputStream())
    printTokens(scanner)
}