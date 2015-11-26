import java.text.SimpleDateFormat

def logLines = new File( 'c:/logs/ac.log' ).readLines()

def startingEntries = logLines.findAll { line -> line.endsWith( ') - started' ) }

startingEntries.each { line ->
	def le = new LogEntry(
		dateStart: new SimpleDateFormat( "yyyy-mm-dd HH:mm:ss,SSS" ).parse( line.substring( 0, 23 ) ),
		mainScript: line.substring( line.indexOf( 'ac (' ) + 4, line.indexOf( ') - started' ) )
	)
	
	println le.dateStart
	println le.mainScript
}

class LogEntry {
	def mainScript
	def dateStart
	def dateEnd
	def lines
}
