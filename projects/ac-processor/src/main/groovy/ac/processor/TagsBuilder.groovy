package ac.processor

/**
 * Constructs List<String> with script tags. Called by Script.
 * @author nurtai
 *
 */
class TagsBuilder  {

	List<String> tags = []

	@SuppressWarnings('NoDef')
	def propertyMissing(String name) {
		this.tags << name
	}

}
