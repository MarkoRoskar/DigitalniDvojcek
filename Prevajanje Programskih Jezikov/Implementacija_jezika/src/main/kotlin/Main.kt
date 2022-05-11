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

class TaskParser(private val scanner: Scanner) {
    private var last: Token? = null

    fun recognize(): Boolean {
        last = scanner.getToken()
        val status = recognizePROGRAM()
        return if (last!!.value == EOF_VALUE) status
        else false
    }

    private fun recognizePROGRAM() = recognizeFUNCTIONS() && recognizeCITY()

    private fun recognizeFUNCTIONS(): Boolean =
        when (last!!.value) {
            FUNC -> recognizeFUNCTION() && recognizeFUNCTIONS()
            CITY -> true
            else -> false
        }

    private fun recognizeCITY() =
        recognizeTerminal(CITY) && recognizeTerminal(IDENTIFIER) && recognizeTerminal(LB_PAREN)
                && recognizeBLOCKS() && recognizeTerminal(RB_PAREN)

    private fun recognizeBLOCKS() = recognizeBLOCK() && recognizeMOREBLOCKS()

    private fun recognizeMOREBLOCKS(): Boolean =
        when (last!!.value) {
            ROAD, BIKE_PATH, BIKE_TOUR_PATH, BIKE_CORRIDOR, BUILDING, RIVER, PARK, BIKE_STAND, BIKE_SHED, M_BAJK, RENT_BIKE -> recognizeBLOCK() && recognizeMOREBLOCKS()
            RB_PAREN -> true
            else -> false
        }

    private fun recognizeBLOCK(): Boolean =
        when (last!!.value) {
            ROAD -> recognizeROAD()
            BIKE_PATH -> recognizeBIKEPATH()
            BIKE_TOUR_PATH -> recognizeTOURPATH()
            BIKE_CORRIDOR -> recognizeCORRIDOR()
            BUILDING -> recognizeBUILDING()
            RIVER -> recognizeRIVER()
            PARK -> recognizePARK()
            BIKE_STAND, BIKE_SHED, M_BAJK, RENT_BIKE -> recognizeMARKPOINT()
            else -> false
        }

    private fun recognizeROAD() =
        recognizeTerminal(ROAD) && recognizeTerminal(IDENTIFIER) && recognizeTerminal(LB_PAREN)
                && recognizeCOMMANDS() && recognizeTerminal(RB_PAREN)

    private fun recognizeBIKEPATH() =
        recognizeTerminal(BIKE_PATH) && recognizeTerminal(IDENTIFIER) && recognizeTerminal(LB_PAREN)
                && recognizeCOMMANDS() && recognizeTerminal(RB_PAREN)

    private fun recognizeTOURPATH() =
        recognizeTerminal(BIKE_TOUR_PATH) && recognizeTerminal(IDENTIFIER) && recognizeTerminal(LB_PAREN)
                && recognizeCOMMANDS() && recognizeTerminal(RB_PAREN)

    private fun recognizeCORRIDOR() =
        recognizeTerminal(BIKE_CORRIDOR) && recognizeTerminal(IDENTIFIER) && recognizeTerminal(LB_PAREN)
                && recognizeCOMMANDS() && recognizeTerminal(RB_PAREN)

    private fun recognizeBUILDING() =
        recognizeTerminal(BUILDING) && recognizeTerminal(IDENTIFIER) && recognizeTerminal(LB_PAREN)
                && recognizeCOMMANDS() && recognizeTerminal(RB_PAREN)

    private fun recognizeRIVER() =
        recognizeTerminal(RIVER) && recognizeTerminal(IDENTIFIER) && recognizeTerminal(LB_PAREN) && recognizeCOMMANDS()
                && recognizeTerminal(RB_PAREN)

    private fun recognizePARK() =
        recognizeTerminal(PARK) && recognizeTerminal(IDENTIFIER) && recognizeTerminal(LB_PAREN) && recognizeCOMMANDS()
                && recognizeTerminal(RB_PAREN)

    private fun recognizeMARKPOINT(): Boolean =
        when (last!!.value) {
            BIKE_STAND -> recognizeBIKESTAND()
            BIKE_SHED -> recognizeBIKESHED()
            M_BAJK -> recognizeMBAJK()
            RENT_BIKE -> recognizeRENTBIKE()
            else -> false
        }

    private fun recognizeBIKESTAND() =
        recognizeTerminal(BIKE_STAND) && recognizeTerminal(L_PAREN) && recognizeTerminal(INT)
                && recognizeTerminal(R_PAREN) && recognizeTerminal(IDENTIFIER) && recognizeTerminal(LB_PAREN)
                && recognizePOINTORVAR() && recognizeTerminal(RB_PAREN)

    private fun recognizeBIKESHED() =
        recognizeTerminal(BIKE_SHED) && recognizeTerminal(IDENTIFIER) && recognizeTerminal(LB_PAREN)
                && recognizePOINTORVAR() && recognizeTerminal(RB_PAREN)

    private fun recognizeMBAJK() =
        recognizeTerminal(M_BAJK) && recognizeTerminal(IDENTIFIER) && recognizeTerminal(LB_PAREN)
                && recognizePOINTORVAR() && recognizeTerminal(RB_PAREN)

    private fun recognizeRENTBIKE() =
        recognizeTerminal(RENT_BIKE) && recognizeTerminal(IDENTIFIER) && recognizeTerminal(LB_PAREN)
                && recognizePOINTORVAR() && recognizeTerminal(RB_PAREN)

    private fun recognizeCOMMANDS() = recognizeCOMMAND() && recognizeMORECOMMANDS()

    private fun recognizeMORECOMMANDS(): Boolean =
        when (last!!.value) {
            LINE, BEND, BOX, FOR, IF, VAR, CONST, ASSIGN -> recognizeCOMMAND()
            RB_PAREN, RETURN -> true
            else -> false
        }

    private fun recognizeCOMMAND(): Boolean =
        when (last!!.value) {
            LINE -> recognizeLINE()
            BEND -> recognizeBEND()
            BOX -> recognizeBOX()
            FOR -> recognizeFOR()
            IF -> recognizeIF()
            VAR -> recognizeVARIABLE()
            CONST -> recognizeCONST()
            ASSIGN -> recognizeASSIGNMENT()
            else -> false
        }

    private fun recognizeLINE(): Boolean =
        recognizeTerminal(LINE) && recognizeTerminal(L_PAREN) && recognizePOINTORVAR() && recognizeTerminal(COMMA)
                && recognizePOINTORVAR() && recognizeTerminal(COMMA) && recognizeWIDTH() && recognizeTerminal(R_PAREN)

    private fun recognizeBEND(): Boolean =
        recognizeTerminal(BEND) && recognizeTerminal(L_PAREN) && recognizePOINTORVAR() && recognizeTerminal(COMMA)
                && recognizePOINTORVAR() && recognizeTerminal(COMMA) && recognizeANGLE() && recognizeTerminal(R_PAREN)

    private fun recognizeANGLE(): Boolean =
        when (last!!.value) {
            INT -> recognizeTerminal(INT)
            IDENTIFIER -> recognizeTerminal(IDENTIFIER)
            else -> false
        }

    private fun recognizeWIDTH(): Boolean =
        when (last!!.value) {
            INT -> recognizeTerminal(INT)
            IDENTIFIER -> recognizeTerminal(IDENTIFIER)
            else -> false
        }

    private fun recognizeBOX(): Boolean =
        recognizeTerminal(BOX) && recognizeTerminal(L_PAREN) && recognizePOINTORVAR() && recognizeTerminal(COMMA)
                && recognizePOINTORVAR() && recognizeTerminal(R_PAREN)

    private fun recognizePOINTORVAR(): Boolean =
        when (last!!.value) {
            L_PAREN -> recognizePOINT()
            IDENTIFIER -> recognizeTerminal(IDENTIFIER)
            else -> false
        }

    private fun recognizeFOR(): Boolean =
        recognizeTerminal(FOR) && recognizeTerminal(L_PAREN) && recognizeSPR() && recognizeTerminal(TO)
                && recognizeSPR() && recognizeTerminal(R_PAREN) && recognizeTerminal(LB_PAREN) && recognizeCOMMANDS()
                && recognizeTerminal(RB_PAREN)

    private fun recognizeIF(): Boolean =
        recognizeTerminal(IF) && recognizeTerminal(L_PAREN) && recognizeOPERATOR() && recognizeSPR()
                && recognizeTerminal(R_PAREN) && recognizeTerminal(LB_PAREN) && recognizeCOMMANDS()
                && recognizeTerminal(RB_PAREN) && recognizeELSEORELSEIFSELSE()

    private fun recognizeELSEORELSEIFSELSE(): Boolean =
        when (last!!.value) {
            ELSE -> recognizeELSE()
            ELSE_IF -> recognizeELSEIFSELSE()
            LINE, BEND, BOX, FOR, IF, VAR, CONST, IDENTIFIER, RB_PAREN -> true
            else -> false
        }

    private fun recognizeELSEIFSELSE(): Boolean = recognizeELSEIFS() && recognizeELSE()

    private fun recognizeELSEIFS(): Boolean = recognizeELSEIF() && recognizeMOREELSEIFS()

    private fun recognizeMOREELSEIFS(): Boolean =
        when (last!!.value) {
            ELSE_IF -> recognizeELSEIF() && recognizeMOREELSEIFS()
            ELSE -> true
            else -> false
        }

    private fun recognizeELSEIF(): Boolean =
        recognizeTerminal(ELSE_IF) && recognizeTerminal(L_PAREN) && recognizeSPR() && recognizeOPERATOR() && recognizeSPR()
                && recognizeTerminal(R_PAREN) && recognizeTerminal(LB_PAREN) && recognizeCOMMANDS() && recognizeTerminal(
            RB_PAREN
        )

    private fun recognizeELSE(): Boolean =
        recognizeTerminal(ELSE) && recognizeTerminal(LB_PAREN) && recognizeCOMMANDS() && recognizeTerminal(RB_PAREN)

    private fun recognizeOPERATOR(): Boolean =
        when (last!!.value) {
            LESS -> recognizeTerminal(LESS)
            LESS_OR_EQUAL -> recognizeTerminal(LESS_OR_EQUAL)
            MORE_OR_EQUAL -> recognizeTerminal(MORE_OR_EQUAL)
            MORE -> recognizeTerminal(MORE)
            EQUAL -> recognizeTerminal(EQUAL)
            NOT_EQUAL -> recognizeTerminal(NOT_EQUAL)
            else -> false
        }

    private fun recognizeSPR(): Boolean =
        when (last!!.value) {
            FLOAT -> recognizeTerminal(FLOAT)
            INT -> recognizeTerminal(INT)
            IDENTIFIER -> recognizeTerminal(IDENTIFIER)
            else -> false
        }

    private fun recognizePOINT(): Boolean =
        recognizeTerminal(L_PAREN) && recognizeEXPR() && recognizeTerminal(COMMA) && recognizeEXPR()
                && recognizeTerminal(R_PAREN)

    private fun recognizeVARIABLE(): Boolean =
        recognizeTerminal(VAR) && recognizeTerminal(IDENTIFIER) && recognizeTerminal(ASSIGN) && recognizeVALUE()

    private fun recognizeCONST(): Boolean =
        recognizeTerminal(CONST) && recognizeTerminal(IDENTIFIER) && recognizeTerminal(ASSIGN) && recognizeVALUE()

    private fun recognizeASSIGNMENT(): Boolean =
        recognizeTerminal(IDENTIFIER) && recognizeTerminal(ASSIGN) && recognizeVALUE()

    private fun recognizeVALUE(): Boolean =
        when (last!!.value) {
            FLOAT -> recognizeTerminal(FLOAT)
            INT -> recognizeTerminal(INT)
            IDENTIFIER -> recognizeTerminal(IDENTIFIER)
            L_PAREN -> recognizePOINT()
            CALL -> recognizeCALL()
            else -> false
        }

    private fun recognizeCALL(): Boolean =
        recognizeTerminal(CALL) && recognizeTerminal(IDENTIFIER) && recognizeTerminal(L_PAREN) && recognizeCALLARGS()
                && recognizeTerminal(R_PAREN)

    private fun recognizeCALLARGS(): Boolean = recognizeVARIABLENAMEORNUMBER() && recognizeMOREVORN()

    private fun recognizeMOREVORN(): Boolean =
        when (last!!.value) {
            COMMA -> recognizeTerminal(COMMA) && recognizeVARIABLENAMEORNUMBER() && recognizeMOREVORN()
            R_PAREN -> true
            else -> false
        }

    private fun recognizeVARIABLENAMEORNUMBER(): Boolean =
        when (last!!.value) {
            FLOAT -> recognizeTerminal(FLOAT)
            INT -> recognizeTerminal(INT)
            IDENTIFIER -> recognizeTerminal(IDENTIFIER)
            else -> false
        }

    private fun recognizeFUNCTION(): Boolean =
        recognizeTerminal(FUNC) && recognizeTerminal(IDENTIFIER) && recognizeTerminal(L_PAREN) && recognizeARGS() &&
                recognizeTerminal(R_PAREN) && recognizeTerminal(LB_PAREN) && recognizeCOMMANDS() && recognizeRETURN() && recognizeTerminal(
            LB_PAREN
        )

    private fun recognizeARGS(): Boolean =
        when (last!!.value) {
            IDENTIFIER -> recognizeTerminal(IDENTIFIER) && recognizeMOREVARS()
            R_PAREN -> true
            else -> false
        }

    private fun recognizeMOREVARS(): Boolean =
        when (last!!.value) {
            COMMA -> recognizeTerminal(COMMA) && recognizeTerminal(IDENTIFIER) && recognizeMOREVARS()
            R_PAREN -> true
            else -> false
        }

    private fun recognizeRETURN(): Boolean = recognizeTerminal(RETURN) && recognizeRETURNVALUE()

    private fun recognizeRETURNVALUE(): Boolean =
        when (last!!.value) {
            L_PAREN -> recognizePOINT()
            FLOAT -> recognizeTerminal(FLOAT)
            IDENTIFIER -> recognizeTerminal(IDENTIFIER)
            INT -> recognizeTerminal(INT)
            else -> false
        }

    private fun recognizeEXPR(): Boolean = recognizeE()

    private fun recognizeE(): Boolean = recognizeT() && recognizeEE()

    private fun recognizeEE(): Boolean =
        when (last!!.value) {
            PLUS -> recognizeTerminal(PLUS) && recognizeT() && recognizeEE()
            MINUS -> recognizeTerminal(MINUS) && recognizeT() && recognizeEE()
            R_PAREN, COMMA -> true
            else -> false
        }

    private fun recognizeT(): Boolean = recognizeX() && recognizeTT()

    private fun recognizeTT(): Boolean =
        when (last!!.value) {
            TIMES -> recognizeTerminal(TIMES) && recognizeX() && recognizeTT()
            DIVIDE -> recognizeTerminal(DIVIDE) && recognizeX() && recognizeTT()
            R_PAREN, COMMA, PLUS, MINUS -> true
            else -> false
        }

    private fun recognizeX(): Boolean = recognizeY() && recognizeXX()

    private fun recognizeXX(): Boolean =
        when (last!!.value) {
            POW -> recognizeTerminal(POW) && recognizeX()
            R_PAREN, COMMA, PLUS, MINUS, TIMES, DIVIDE -> true
            else -> false
        }

    private fun recognizeY(): Boolean =
        when (last!!.value) {
            MINUS -> recognizeTerminal(MINUS) && recognizeF()
            PLUS -> recognizeTerminal(PLUS) && recognizeF()
            L_PAREN, FLOAT, INT, IDENTIFIER -> recognizeF()
            else -> false
        }

    private fun recognizeF(): Boolean =
        when (last!!.value) {
            L_PAREN -> recognizeTerminal(L_PAREN) && recognizeE() && recognizeTerminal(R_PAREN)
            FLOAT -> recognizeTerminal(FLOAT)
            IDENTIFIER -> recognizeTerminal(IDENTIFIER)
            INT -> recognizeTerminal(INT)
            else -> false
        }


    private fun recognizeTerminal(value: Int) =
        if (last?.value == value) {
            last = scanner.getToken()
            true
        } else false
}

fun main(args: Array<String>) {
    val scanner = StreamScanner(
        Example,
        "bend ; , bikeCoridor, bikeShed, bikeStand, bikeTourPath, box, building, call, const, circ, city, else, elseif, for, func, if, line, mBajk, park, rentBike, return, river, road, to, var".byteInputStream()
    )
    printTokens(scanner)
}