package ru.croco.crocodile

class Note {
    var id = 0
    var word: String? = null
    var description: String? = null
    var levell: Int? = null
    var area: String? = null

    constructor() {}
    constructor(id: Int, word: String?, description: String?, levell: Int?, area: String?) {
        this.id = id
        this.word = word
        this.description = description
        this.levell = levell
        this.area = area
    }

    companion object {
        const val TABLE_NAME = "CROCO"
        const val COLUMN_ID = "id"
        const val COLUMN_WORD = "word"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_LEVEL = "levell"
        const val COLUMN_AREA = "area"

        // Create table SQL query
        const val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_WORD TEXT, " +
                "$COLUMN_DESCRIPTION TEXT, " +
                "$COLUMN_LEVEL INTEGER, " +
                "$COLUMN_AREA INTEGER)"

        const val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}