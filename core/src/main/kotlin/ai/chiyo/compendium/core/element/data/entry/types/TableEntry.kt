package ai.chiyo.compendium.core.element.data.entry.types

import ai.chiyo.compendium.core.element.data.entry.Entry

data class TableEntry(override var name: String, var columns: List<String>,
                      var rows: List<List<String>>) : Entry(name) {
    fun build(): String {
        // calculate required padding per column based on longest entry in aforementioned column
        val columnPadding = HashMap<String, Int>()
        for (column in columns) {
            val columnContents = mutableListOf<String>()
            for (row in rows) {
                columnContents.add(row[columns.indexOf(column)])
            }
            columnPadding[column] = columnContents.maxOf { it.length }
        }
        // assemble flat-pack furniture (table)
        return buildString {
            // append column header
            for (column in columns) {
                append(format(column, columnPadding[column]!!))
            }
            // append spacer line
            append("\n").append("-".repeat(((columns.size * 2) + columnPadding.values.sum()) + 1))
                .append("\n")
            // append rows
            for (row in rows) {
                for (data in row) {
                    append(format(data, columnPadding.values.elementAt(row.indexOf(data))))
                }
                append("\n")
            }
        }
    }

    private fun format(input: String, padding: Int): String {
        return "%${(padding + 1) * -1}s| ".format(input.trim())
    }
}