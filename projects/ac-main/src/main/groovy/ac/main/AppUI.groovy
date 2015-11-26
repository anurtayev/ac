package ac.main

import groovy.beans.Bindable
import groovy.util.logging.Slf4j

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan

import ac.processor.ScriptProcessorService
import ac.processor.Script

import com.prajnainc.vaadinbuilder.VaadinBuilder
import com.vaadin.event.ItemClickEvent
import com.vaadin.server.VaadinRequest
import com.vaadin.shared.ui.MarginInfo
import com.vaadin.spring.annotation.SpringUI
import com.vaadin.ui.UI

/**
 * Vaadin interface definition
 * @author nurtai
 *
 */
@Slf4j
@SpringUI
@ComponentScan(basePackages = ['ac.main', 'ac.processor'])
class AppUI extends UI {

	final static String STR_100_PERCENT = '100%'

	@Autowired
	ScriptsService scriptsService

	@Autowired
	ScriptProcessorService scriptProcessorService

	@Override
	@SuppressWarnings('SpaceBeforeOpeningBrace')
	protected void init(VaadinRequest vaadinRequest) {
		content = new VaadinBuilder().build {
			horizontalLayout( margin:true, spacing:true, width:STR_100_PERCENT, height:STR_100_PERCENT ) {
				panel( width:STR_100_PERCENT, height:STR_100_PERCENT, expandRatio:1 ) {
					tree( dataSource:scriptsService.data, itemCaptionPropertyId:'caption', onItemClick:{ ItemClickEvent evt ->
						String scriptStr = scriptsService.getScript(evt.item.getItemProperty('scriptId').value)
						model.script = scriptProcessorService.parse(scriptStr)
					} )
				}
				panel( width:STR_100_PERCENT, height:STR_100_PERCENT, expandRatio:3 ) {
					verticalLayout( margin:new MarginInfo(true)) {
						fieldGroup( id:'scriptForm', dataSource:bind(source:model, sourceProperty:'script')) {
//							textField('id')
							//textField('hidden')
							textField('description')
							//textField('tags')
						}
					}
				}
			}
		}
	}

	static class Model {
		@Bindable Script script = new Script()
	}

	Model model = new Model()
}
