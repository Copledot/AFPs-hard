//Autores: Fabian Morales, Michael Valenzuela.

var repo = Repositorio()

fun main() {
    repo.afps.add(AFP("Capital", 0.0144))
    repo.afps.add(AFP("Cuprum", 0.0144))
    repo.afps.add(AFP("Habitat", 0.0127))
    repo.afps.add(AFP("Modelo", 0.0058))
    repo.afps.add(AFP("Planvital", 0.0116))
    repo.afps.add(AFP("Provida", 0.0145))
    repo.afps.add(AFP("Uno", 0.0049))

    repo.empleados.add(Empleado(
        "21.587.518-0",
        "Michael Valenzuela",
        1000000.0,
        repo.afps[0],
        Direccion("Tacna", 690, "Peru")))
    repo.empleados.add(Empleado(
        "22.037.834-9",
        "Rodrigo Cofre",
        850000.0,
        repo.afps[1],
        Direccion("Tinguiririca", 420, "Chimbarongo")))
    repo.empleados.add(Empleado(
        "21.782.978-K",
        "Juan Jose Garrido",
        1200000.0,
        repo.afps[2],
        Direccion("Pueblo Tomate", 911, "Chiringuito Chatarra")))

    var opcion = 0
    while (opcion != 7) {
        println("MENU")
        println("1. Listar empleados")
        println("2. Agregar empleado")
        println("3. Generar liquidacion")
        println("4. Listar liquidaciones")
        println("5. Filtrar empleados por AFP")
        println("6. Eliminar empleado")
        println("7. Salir")
        print("Opcion: ")

        opcion = readln().toInt()

        when (opcion) {
            1 -> listarEmpleados()
            2 -> agregarEmpleado()
            3 -> generarLiquidacion()
            4 -> listarLiquidaciones()
            5 -> filtrarPorAFP()
            6 -> eliminarEmpleado()
            0 -> println("Saliendo del programa.")
            else -> println("Opcion invalida")
        }
    }
}

fun listarEmpleados() {
    if (repo.empleados.isEmpty()) {
        println("No hay empleados.")
    } else {
        for (i in repo.empleados) {
            println("${i.rut} - ${i.nombre} - Sueldo: ${i.sueldoBase} - AFP: ${i.afp.nombre}")
        }
    }
}

fun agregarEmpleado() {
    print("RUT: ")
    val rut = readln()
    print("Nombre: ")
    val nombre = readln()
    print("Sueldo base: ")
    val sueldo = readln().toDouble()

    println("Seleccione AFP:")
    for (i in repo.afps.indices) {
        println("${i + 1}. ${repo.afps[i].nombre}")
    }
    val afp = repo.afps[readln().toInt() - 1]

    print("Calle: ")
    val calle = readln()
    print("Numero: ")
    val numero = readln().toInt()
    print("Ciudad: ")
    val ciudad = readln()

    repo.empleados.add(Empleado(rut, nombre, sueldo, afp, Direccion(calle, numero, ciudad)))
    println("Empleado agregado.")
}

fun generarLiquidacion() {
    print("Ingrese RUT: ")
    val rut = readln()
    val emp = repo.empleados.find { it.rut == rut }

    if (emp != null) {
        val liq = LiquidacionSueldo(emp)
        liq.descuentoAfp = emp.sueldoBase * emp.afp.tasa
        liq.descuentoSalud = emp.sueldoBase * 0.07
        liq.descuentoCesantia = emp.sueldoBase * 0.006
        liq.sueldoLiquido = emp.sueldoBase - (liq.descuentoAfp + liq.descuentoSalud + liq.descuentoCesantia)
        repo.liquidaciones.add(liq)
        println("Liquidacion generada. Sueldo liquido: ${liq.sueldoLiquido}")
    } else {
        println("Empleado no encontrado.")
    }
}

fun listarLiquidaciones() {
    if (repo.liquidaciones.isEmpty()) {
        println("No hay liquidaciones.")
    } else {
        var totalDescuentos = 0.0
        for (i in repo.liquidaciones) {
            println("${i.empleado.nombre} - Liquido: ${i.sueldoLiquido}")
            totalDescuentos += i.descuentoAfp + i.descuentoSalud + i.descuentoCesantia
        }
        println("Total descuentos: $totalDescuentos")
    }
}

fun filtrarPorAFP() {
    println("Seleccione AFP:")
    for (i in repo.afps.indices) {
        println("${i + 1}. ${repo.afps[i].nombre}")
    }
    val afp = repo.afps[readln().toInt() - 1]

    val filtrados = repo.empleados.filter { it.afp == afp }
    if (filtrados.isEmpty()) {
        println("No hay empleados en ${afp.nombre}")
    } else {
        for (i in filtrados) {
            println("${i.rut} - ${i.nombre} - Sueldo: ${i.sueldoBase}")
        }
    }
}

fun eliminarEmpleado() {
    print("RUT a eliminar: ")
    val rut = readln()
    val eliminado = repo.empleados.removeIf { it.rut == rut }
    if (eliminado) println("Empleado eliminado.") else println("No encontrado.")
}
