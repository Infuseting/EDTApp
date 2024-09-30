package fr.infuseting.edt_app.util.model
data class Salle(
    val numUniv: Int,
    val nameSalle: String,
    val adeUniv: String,
    val adeResources: Int,
    val adeProjectId: Int
)

data class Prof(
    val numUniv: Int,
    val nomProf: String,
    val adeUniv: String,
    val adeResources: Int,
    val adeProjectId: Int
)
data class Timetable(
    val adeResources: Int,
    val descTT: String,
    val adeProjectId: Int
)

data class University(
    val numUniv: Int,
    val nameUniv: String,
    val adeUniv: String,
    val timetable: List<Timetable>
)