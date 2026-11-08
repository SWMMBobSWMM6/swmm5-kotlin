// Main.kt - command-line runner for the Kotlin port: same arguments and console messages as EPA's
// run/main.c.   runswmm <input file> <report file> [output file]
// Written by Claude (Anthropic) for Robert Dickinson; see README.md.
@file:JvmName("Main")
package swmm5

private fun out(s: String) {
    val b = s.toByteArray(Charsets.ISO_8859_1)
    R.stdout.write(b, 0, b.size)
    R.stdout.flush()
}

fun main(argv: Array<String>) {
    val version = swmm_getVersion()
    val vMajor = version / 10000
    val vMinor = (version - 10000 * vMajor) / 1000
    val vRelease = version - 10000 * vMajor - 1000 * vMinor
    if (argv.isEmpty()) out("\nNot Enough Arguments (See Help --help)\n\n")
    else if (argv.size == 1) {
        val a = argv[0]
        if (a == "--help" || a == "-h")
            out("\n\nSTORMWATER MANAGEMENT MODEL (SWMM) HELP\n\nCOMMANDS:\n\t--help (-h)       SWMM Help\n" +
                "\t--version (-v)    Build Version\n\nRUNNING A SIMULATION:\n" +
                "\t runswmm <input file> <report file> <optional output file>\n\n")
        else if (a == "--version" || a == "-v") out("\n$vMajor.$vMinor.$vRelease\n\n")
        else out("\nUnknown Argument (See Help --help)\n\n")
    } else {
        val start = System.currentTimeMillis()
        out("\n... EPA SWMM $vMajor.$vMinor (Build $vMajor.$vMinor.$vRelease) [Kotlin port]\n")
        swmm_run(R.lit(argv[0]), R.lit(argv[1]), R.lit(if (argv.size > 2) argv[2] else ""))
        var msg = String.format("\n\n... EPA SWMM completed in %.2f seconds.", (System.currentTimeMillis() - start) / 1000.0)
        val err = R.buf(128)
        msg += if (swmm_getError(err, 127) > 0) " There are errors.\n"
        else if (swmm_getWarnings() > 0) " There are warnings.\n"
        else "\n"
        out(msg)
    }
    R.stdout.flush()
}
